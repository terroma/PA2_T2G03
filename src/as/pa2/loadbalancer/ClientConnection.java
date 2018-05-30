package as.pa2.loadbalancer;

import as.pa2.protocol.PiRequest;
import as.pa2.protocol.PiResponse;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author terroma
 */
public class ClientConnection implements Runnable {

    protected Socket tcpSocket;
    protected ObjectInputStream oInStream;
    protected ObjectOutputStream oOutStream;
    protected int clientId;
    protected LinkedBlockingQueue<PiRequest> requestsQueue;
    
    public ClientConnection(LinkedBlockingQueue<PiRequest> requestQueue, Socket tcpSocket, int clientId) {
        this.requestsQueue = requestQueue;
        this.tcpSocket = tcpSocket;
        this.clientId = clientId;
        initStreams();
    }

    private void initStreams() {
        try {
            this.oInStream = new ObjectInputStream(tcpSocket.getInputStream());
            this.oOutStream = new ObjectOutputStream(tcpSocket.getOutputStream());
        } catch (IOException ioe) {
            System.out.println("[!] IOException! ClientConnection["+this.clientId+"]");
            ioe.printStackTrace();
        }
        
    }
    
    @Override
    public void run() {
        while (this.tcpSocket.isConnected()) {
            try {
                PiRequest request = (PiRequest) oInStream.readObject();
                if (request != null) {
                    try {
                        requestsQueue.put(request);
                    } catch (InterruptedException ex) {
                        System.out.println("[!] ClientConnection["+this.clientId+"] interrupted while waiting to put request in list");
                        ex.printStackTrace();
                    }
                }
                
            } catch (IOException ioe) {
                System.out.println("[!] IOException! Client["
                        +clientId+"]");
                ioe.printStackTrace();    
            } catch (ClassNotFoundException ex) {
                System.out.println("[!] ClassNotFoundException! Client["
                        +clientId+"]");
                ex.printStackTrace();
            }
        }
        this.close();
    }
    
    public void sendResponse(PiResponse response) {
        synchronized(this) {
            if(!this.tcpSocket.isConnected())
                return;
            try {
                System.out.println("[*] ClientConnection["+this.clientId+"]: sending response ...");
                this.oOutStream.writeObject(response);
                this.oOutStream.flush();
            } catch (IOException ioe) {
                System.out.println("[!] IOException! ClientConnection["+this.clientId+"]");
                ioe.printStackTrace();
            }
        }
    }
    
    public void close() {
        try {
            this.oInStream.close();
            this.oOutStream.close();
            this.tcpSocket.close();
        } catch (IOException ioe) {
            System.out.println("[!] IOException! ClientConnection["+this.clientId+"]");
            ioe.printStackTrace();
        }
    }
}

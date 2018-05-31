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
    protected final int internalId;
    protected LinkedBlockingQueue<PiRequest> requestsQueue;
    
    public ClientConnection(LinkedBlockingQueue<PiRequest> requestQueue, Socket tcpSocket,final int internalId) {
        this.requestsQueue = requestQueue;
        this.tcpSocket = tcpSocket;
        this.internalId = internalId;
        initStreams();
    }

    private void initStreams() {
        try {
            this.oInStream = new ObjectInputStream(tcpSocket.getInputStream());
            this.oOutStream = new ObjectOutputStream(tcpSocket.getOutputStream());
        } catch (IOException ioe) {
            System.out.println("[!] IOException! ClientConnection["+this.internalId+"]");
            ioe.printStackTrace();
        }
    }
    
    @Override
    public void run() {
        while ( true ) {
            try {
                PiRequest request = (PiRequest) oInStream.readObject();
                System.out.println("[*] ClientConnection handling request "+request.toString());
                if (this.clientId == 0)
                    this.clientId = request.getClientId();
                    
                if (request != null) {
                    try {
                        request.setClientId(internalId);
                        requestsQueue.put(request);
                        System.out.println("[*] ClientConnection request added to list...");
                    } catch (InterruptedException ex) {
                        System.out.println("[!] ClientConnection["+this.internalId+"] interrupted while waiting to put request in list");
                        ex.printStackTrace();
                    }
                }       
            } catch (IOException ioe) {
                System.out.println("[!] IOException! Client["
                        +clientId+"]");
                this.close();
                ioe.printStackTrace();    
            } catch (ClassNotFoundException ex) {
                System.out.println("[!] ClassNotFoundException! Client["
                        +clientId+"]");
                ex.printStackTrace();
            }
        }
    }
    
    public void sendResponse(PiResponse response) {
        synchronized(this) {
            if(!this.tcpSocket.isConnected())
                return;
            try {
                response.setClientId(clientId);
                System.out.println("[*] ClientConnection["+this.internalId+"]: sending response ...");
                this.oOutStream.writeObject(response);
                this.oOutStream.flush();
            } catch (IOException ioe) {
                System.out.println("[!] IOException! ClientConnection["+this.internalId+"]");
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

package as.pa2.loadbalancer;

import as.pa2.gui.MonitorLBGUI;
import as.pa2.protocol.PiRequest;
import as.pa2.protocol.PiResponse;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.LinkedBlockingQueue;

/**
 *
 * @author Bruno Assunção 89010
 * @author Hugo Chaves  90842
 * 
 */
public class ClientConnection implements Runnable {

    protected Socket tcpSocket;
    protected ObjectInputStream oInStream;
    protected ObjectOutputStream oOutStream;
    protected int clientId;
    protected final int internalId;
    protected LinkedBlockingQueue<PiRequest> requestsQueue;
    protected ConcurrentHashMap<Integer, ClientConnection> clientConnections;
    protected boolean isStopped;
    protected MonitorLBGUI mlb;
    
    public ClientConnection(LinkedBlockingQueue<PiRequest> requestQueue,ConcurrentHashMap<Integer,ClientConnection> clientConnections, Socket tcpSocket,final int internalId, MonitorLBGUI mlb) {
        this.requestsQueue = requestQueue;
        this.tcpSocket = tcpSocket;
        this.internalId = internalId;
        this.isStopped = false;
        this.mlb = mlb;
        this.clientConnections = clientConnections;
        initStreams();
    }

    private void initStreams() {
        try {
            this.oInStream = new ObjectInputStream(tcpSocket.getInputStream());
            this.oOutStream = new ObjectOutputStream(tcpSocket.getOutputStream());
        } catch (IOException ioe) {
            mlb.updateLogs("[!] Client [" + clientId + "] connection closed.");
        }
    }
    
    @Override
    public void run() {
        while ( !isStopped() ) {
            try {
                PiRequest request = (PiRequest) oInStream.readObject();
                //System.out.println("[*] ClientConnection handling request "+request.toString());
                if (this.clientId == 0)
                    this.clientId = request.getClientId();
                    
                if (request != null) {
                    try {
                        //request.setClientId(internalId);
                        clientConnections.put(request.getClientId(), this);
                        requestsQueue.put(request);
                        //System.out.println("[*] ClientConnection request added to list...");
                    } catch (InterruptedException ex) {
                        //System.out.println("[!] ClientConnection["+this.internalId+"] interrupted while waiting to put request in list");
                        //ex.printStackTrace();
                    }
                }       
            } catch (IOException ioe) {
                mlb.updateLogs("[!] Client [" + clientId + "] connection closed.");
                this.stop();
                //ioe.printStackTrace();    
            } catch (ClassNotFoundException ex) {
                System.out.println("[!] ClassNotFoundException! Client["
                        +clientId+"]");
                //ex.printStackTrace();
            }
        }
    }
    
    public void sendResponse(PiResponse response) {
        synchronized(this) {
            if(!this.tcpSocket.isConnected())
                return;
            try {
                response.setClientId(clientId);
                //System.out.println("[*] ClientConnection["+this.internalId+"]: sending response ...");
                if (mlb != null)
                    mlb.updateLogs("Sending response to Client: " + clientId);
                
                //System.out.println("Sending response to Client: " + clientId);
                this.oOutStream.writeObject(response);
                this.oOutStream.flush();
            } catch (IOException ioe) {
                mlb.updateLogs("[!] Client [" + clientId + "] connection closed.");
            }
        }
    }
    
    private synchronized boolean isStopped() {
        return this.isStopped;
    }
    
    public synchronized void stop() {
        this.isStopped = true;
        try {
            this.tcpSocket.close();
        } catch (IOException ioe) {
            throw new RuntimeException("Error closing LoadBalancer client connection",ioe);
        }
    }
    
    public void close() {
        try {
            //this.oInStream.close();
            //this.oOutStream.close();
            this.tcpSocket.close();
        } catch (IOException ioe) {
            mlb.updateLogs("[!] Client [" + clientId + "] connection closed.");
        }
    }
}

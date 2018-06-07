package as.pa2.loadbalancer;

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
public class ServerConnection implements Runnable {

    private static final boolean TEST = false;
    protected Socket tcpSocket;
    protected ObjectInputStream oInStream;
    protected ObjectOutputStream oOutStream;
    // could be final
    protected String serverId;
    protected ConcurrentHashMap<Integer, ClientConnection> clientConnections;
    protected ConcurrentHashMap<PiRequest,PiResponse> handledRequests;
    protected LinkedBlockingQueue<PiRequest> requestQueue;
    protected PiRequest request;
    protected boolean isStopped;

    public ServerConnection(ConcurrentHashMap<Integer, ClientConnection> clientConnections, 
            ConcurrentHashMap<PiRequest,PiResponse> handledRequests, 
            LinkedBlockingQueue<PiRequest> requestQueue,
            Socket tcpSocket, String serverId, PiRequest request) {
        this.clientConnections = clientConnections;
        this.tcpSocket = tcpSocket;
        this.serverId = serverId;
        this.request = request;
        this.handledRequests = handledRequests;
        this.requestQueue = requestQueue;
        this.isStopped = false;
        //initStreams();
    }

    
    @Override
    public void run() {
        //updateDebugLogs("[*] ServerConnection[" +this.serverId+ "]: started ...");
        while ( !isStopped() ) {
            try {
                this.oOutStream = new ObjectOutputStream(this.tcpSocket.getOutputStream());
                this.oOutStream.writeObject(this.request);
                this.oOutStream.flush();
                
            } catch (IOException ex) {
                updateDebugLogs("[!] ServerConnection[" + this.serverId + "]: Failed to send request ...");
                updateDebugLogs("[!] ServerConnection[" + this.serverId + "]: Server Down ...");
                requestQueue.add(request);
                System.out.println("Servidor nao foi encontrado addicionou a lista");
                this.stop();
            }
            try {
                this.oInStream = new ObjectInputStream(this.tcpSocket.getInputStream());
                PiResponse response = (PiResponse) this.oInStream.readObject();
                if (response != null) {
                    //updateDebugLogs("[*] ServerConnection[" + this.serverId + "]: response recieved ...");
                    clientConnections.get(response.getClientId()).sendResponse(response);
                    //updateDebugLogs("[*] ServerConnection[" + this.serverId + "]: response sent ...");
                    handledRequests.put(request, response);
                    this.stop();
                }
            } catch (IOException ex) {
                updateDebugLogs("[!] ServerConnection[" + this.serverId + "]: Failed to receive response ...");
                updateDebugLogs("[!] ServerConnection[" + this.serverId + "]: Server Down ...");
                requestQueue.add(request);
                this.stop();
            } catch (ClassNotFoundException ex) {
                updateDebugLogs("[!] ServerConnection[" + this.serverId + "]: Failed converting object to PiResponse ...");
            }
        }
    }

    private synchronized boolean isStopped() {
        return this.isStopped;
    }

    public synchronized void stop() {
        this.isStopped = true;
        /*
        try {
            this.oInStream.close();
            this.oOutStream.close();
        } catch (IOException ioe) {
            updateDebugLogs("[!] IOException! ServerConnection[" + this.serverId + "]");
            ioe.printStackTrace();
        }*/
    }
    
    private void updateDebugLogs(String s) {
        if (TEST) {
            System.out.println(s);
        }
    }
    
    
}

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package as.pa2.server;

import as.pa2.protocol.PiRequest;
import as.pa2.protocol.PiResponse;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.concurrent.LinkedBlockingQueue;

/**
 *
 * @author terroma
 */
public class RequestHandler implements Runnable {

    protected Socket clientSocket;
    protected Server server;
    protected boolean isStopped;
    protected ObjectInputStream oInStream;
    protected ObjectOutputStream oOutStream;
    protected LinkedBlockingQueue<RequestHandler> requestQueue;
    
    private boolean running;
    private boolean processing = false;
    private int threadNumber;
    private int queueSize;
    
    public RequestHandler(Socket clientSocket, Server server, LinkedBlockingQueue<RequestHandler> requestQueue, int queueSize) {
        this.clientSocket = clientSocket;
        this.server = server;
        this.requestQueue = requestQueue;
        this.queueSize = queueSize;
        this.isStopped = false;
    }
    
    @Override
    public void run() {
        try {
            synchronized ( this ) {
                System.out.println(Thread.currentThread().getId());
            }
            this.oInStream = 
                    new ObjectInputStream(clientSocket.getInputStream());
            this.oOutStream =
                    new ObjectOutputStream(clientSocket.getOutputStream());
            
            long time = System.currentTimeMillis();
            while( !isStopped() ) {
                //System.out.println("Está à espera");
                PiRequest request = (PiRequest) oInStream.readObject();
                PiResponse response = null;
                if(request!=null){
                    System.out.println("queueSize: "+requestQueue.size());
                    if (requestQueue.size() > queueSize) {
                        response = new PiResponse(request.getClientId(), request.getRequestId(), 3, request.getPrecision(), request.getDelay(), 0);
                        oOutStream.writeObject(response);
                        oOutStream.flush();
                        server.getServerGUI().updateLogs("Server: " + server.getHost() + " can't compute request.");
                        requestQueue.remove(this);
                        break;
                    }
                    this.processing = true;
                    server.sendStatistics((int)Thread.currentThread().getId(), request.getRequestId());
                    //System.out.println("ThreadId: "+Thread.currentThread().getId()+" requestID: "+request.getRequestId());
                    server.getServerGUI().updateLogs("Server: " + server.getHost() + " received request: [" + request.toString() + " ]");
                    server.getServerGUI().updateLogs("Server: " + server.getHost() + " is computing request");
                    Double pi = new Pi().compute(request.getPrecision(), request.getDelay());
                    response = new PiResponse(request.getClientId(), request.getRequestId(), 2, request.getPrecision(), request.getDelay(), pi);
                    server.getServerGUI().updateLogs("Server: " + server.getHost() + " finish computing and is trying to send result.");
                    oOutStream.writeObject(response);
                    server.getServerGUI().updateLogs("Server: " + server.getHost() + " sended result: [" + response.toString() + " ]");
                    oOutStream.flush();
                    this.processing = false;
                    requestQueue.remove(this);
                    break;
                }
            }
            //this.stop();
        } catch (IOException ioe) {
            System.out.println("Closing client connection ");
            this.isStopped = true;
            try {
                clientSocket.close();
            } catch (IOException ex) {
                ex.getMessage();
            }
        } catch (ClassNotFoundException ex) {
            ex.getMessage();
        }
    }
    
    public boolean isProcessing() {
        return this.processing;
    }
    
    private synchronized boolean isStopped() {
        return this.isStopped;
    }
    
    public synchronized void stop() {
        this.isStopped = true;
        try {
            oInStream.close();
            oOutStream.close();
            clientSocket.close();
        } catch (IOException ex) {
            ex.getMessage();
        }
    }
}

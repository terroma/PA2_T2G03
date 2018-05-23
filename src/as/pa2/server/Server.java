/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package as.pa2.server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 *
 * @author bruno
 */
public class Server implements Runnable{

    protected int serverPort;
    protected ServerSocket serverSocket;
    protected boolean isStopped;
    protected Thread runningThread;
    protected ExecutorService threadPool;
    
    protected int serverId;
    protected UUID uniqueClientId = UUID.randomUUID();
    
    /* default server constructor */
    public Server() {
        this.serverId = uniqueClientId.hashCode();
        System.out.println("[*] Starting Server["+serverId+"] ...");
        this.serverPort = 8080;
        this.serverSocket = null;
        this.isStopped = false;
        this.runningThread = null;
        this.threadPool = Executors.newFixedThreadPool(10);
    }
    
    @Override
    public void run() {
        synchronized( this ) {
            this.runningThread = Thread.currentThread();
        }
        openServerSocket();
        System.out.println("[*] Server["+serverId+"] Connected ...");
        
        while (!isStopped()) {
            Socket clientSocket = null;
            
            try {
                clientSocket = this.serverSocket.accept();
                System.out.println("[*] Server["+serverId+"] "
                    + "Accepted Connection: "+clientSocket.getInetAddress().toString());
            } catch (IOException ioe) {
                if (isStopped()) {
                    System.out.println("Server Stopped.");
                    break;
                }
                throw new RuntimeException(
                        "Error accepting client connection.",ioe);
            }
            /*
            this.threadPool.execute(
                        new RequestHandler(clientSocket, this.serverId));
            */
        }
        this.threadPool.shutdown();
        System.out.println("Server Stopped.");
    }
    
    private synchronized boolean isStopped() {
        return this.isStopped;
    }
    
    private void openServerSocket() {
        try {
            this.serverSocket = new ServerSocket(this.serverPort);
        } catch (IOException ioe) {
            throw new RuntimeException(
                    "Cannot open port "+this.serverPort+"!", ioe);
        }
    }
    
    public static void main(String[] args) {
        Server s = new Server();
        s.run();
    }
}

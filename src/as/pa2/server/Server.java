/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package as.pa2.server;

import as.pa2.gui.ServerGUI;
import as.pa2.loadbalancer.ClientConnection;
import as.pa2.loadbalancer.LoadBalancer;
import as.pa2.protocol.PiRequest;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author bruno
 */
public class Server implements Serializable, Runnable {

    protected int serverPort;
    protected transient ServerSocket serverSocket;
    protected boolean isStopped;
    protected transient Thread runningThread;
    protected transient ExecutorService threadPool;
    
    protected int serverId;
    protected UUID uniqueClientId = UUID.randomUUID();
    
    private String host;
    private volatile String id;
    private volatile boolean isAliveFlag;
    private volatile boolean readyToServe = true;
    
    protected transient Socket monitorSocket;
    
    private String monitorIp;
    private int monitorPort;
    private int loadBalancerPort;
    private int queueSize;
    
    protected transient ConcurrentHashMap<Integer,Socket> clientConnections = new ConcurrentHashMap<Integer,Socket>();
    
    protected transient LinkedBlockingQueue<RequestHandler> requestQueue;
    
    protected transient ObjectOutputStream monitorOutStream;
    
    private transient ServerGUI serverGUI;
    
    /* default server constructor */
    public Server() {
        this.serverId = uniqueClientId.hashCode();
        System.out.println("[*] Starting Server["+serverId+"] ...");
        this.serverPort = 8080;
        this.serverSocket = null;
        this.isStopped = false;
        this.runningThread = null;
        this.threadPool = Executors.newFixedThreadPool(10);
        this.host = "127.0.0.1"; // or locahost
    }
    
    public Server(String host, int port, String monitorIp, int monitorPort, int loadBalancerPort, int queueSize) {
        this.host = host;
        this.serverPort = port;
        this.id = host + ":" + port;
        isAliveFlag = false;
        this.serverSocket = null;
        this.isStopped = false;
        this.runningThread = null;
        this.monitorIp = monitorIp;
        this.monitorPort = monitorPort;
        this.loadBalancerPort = loadBalancerPort;
        this.queueSize = queueSize;
        
        this.requestQueue = new LinkedBlockingQueue<RequestHandler>();
        this.threadPool = Executors.newFixedThreadPool(10);
        System.out.println(host + " : " + port);
        System.out.println("[*] Starting Server["+id+"] ...");
    }
    
    public Server(String id) {
        setId(id);
        isAliveFlag = false;
    }
    
    public Server(ServerGUI serverGUI){
        this.serverGUI = serverGUI;
    }
    
    @Override
    public void run() {
        synchronized( this ) {
            this.runningThread = Thread.currentThread();
        }
        openServerSocket();
        System.out.println("[*] Server["+id+"] Connected ...");
        notifyMonitor(monitorIp, monitorPort);
        (new Thread(new Runnable() {
            @Override
            public void run() {
                while (true) {
                    ServerSocket srvSckt = null;
                    try {
                        srvSckt = new ServerSocket(2000 , 10, InetAddress.getByName(host));
                        System.out.println("[*] Server["+serverId+"] Accepting Ping! ");
                        srvSckt.accept();
                        //System.out.println("[*] Server["+serverId+"] Closing ping socket! ");
                        //srvSckt.close();
                    } catch (IOException ex) {
                        //System.out.println("[*] Server["+serverId+"] Error openning ping socket! ");
                    }
                }
            }
        })).start();

        while ( !isStopped() ) {
            Socket clientSocket = null;
            
            try {
                clientSocket = this.serverSocket.accept();
                System.out.println("[*] Server["+serverId+"] "
                    + "Accepted Connection: "+clientSocket.getInetAddress().getHostAddress()
                        +":"+clientSocket.getPort());
                
                RequestHandler requestHandler = new RequestHandler(clientSocket, this, this.requestQueue, this.queueSize);
                this.requestQueue.add(requestHandler);
                this.threadPool.execute(requestHandler);
                
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
        this.stop();
        System.out.println("Server Stopped.");
    }
    
    public void sendStatistics(int threadId, int requestId) throws IOException {
        synchronized (monitorOutStream) {     
            String toSend = "Server["+id+"]: ThreadId: "+threadId+" processing requestId: "+requestId;
            monitorOutStream.writeUTF(toSend);
            monitorOutStream.flush();
        }
    }
    
    private void notifyMonitor(String monitorIp, int monitorPort) {
        try {
            System.out.println("[*] Server["+this.id+"]: openning monitor socket.");
            this.monitorSocket = new Socket(monitorIp, monitorPort);
            System.out.println("[*] Server["+this.id+"]: monitor socket openned.");
            monitorOutStream = new ObjectOutputStream(monitorSocket.getOutputStream());
            monitorOutStream.writeObject(this);
            monitorOutStream.flush();
            System.out.println("[*] Server["+this.id+"]: monitor notified.");
            //oOutStream.close();
            //System.out.println("[*] Server["+this.id+"]: closing monitor connection.");
            //this.monitorSocket.close();
        } catch (IOException ex) {
            System.out.println("[!] Server["+this.id+"]: failed connection to monitor!"+ex.getMessage());
        }
    }
    
    private synchronized boolean isStopped() {
        return this.isStopped;
    }
    
    public synchronized void stop() {
        this.isStopped = true;
        try {
            System.out.println("Server Stopped!");
            this.serverSocket.close();
            this.monitorSocket.close();
        } catch (IOException ioe) {
            throw new RuntimeException("Error closing server",ioe);
        }
    }
    
    private void openServerSocket() {
        try {
            this.serverSocket = new ServerSocket(this.serverPort, 100, InetAddress.getByName(this.host));
        } catch (IOException ioe) {
            throw new RuntimeException(
                    "Cannot open port "+this.serverPort+"!", ioe);
        }
    }
    
    public void setAlive(boolean isAliveFlag) {
        this.isAliveFlag = isAliveFlag;
    }
    
    public boolean isAlive() {
        return isAliveFlag;
    }
    
    public String getId() {
        return id;
    }
    
    /* hostPort combination */
    public void setId(String id) {
        this.id = id;
    }
    
    public String getHost() {
        return host;
    }
    
    public void setHost(String host) {
        if (host != null) {
            this.host = host;
            id = host + ":" + serverPort;
        }
    }
    
    public int getPort() {
        return serverPort;
    }
    
    public void setPort(int port) {
        this.serverPort = port;
        
        if (host != null) {
            id = host + ":" + port;
        }
    }
    
    public String getHostPort() {
        return host + ":" + serverPort;
    }
    
    public final boolean isReadyToServe() {
        return readyToServe;
    }
    
    public final void setReadyToServe(boolean readyToServe) {
        this.readyToServe = readyToServe;
    }

    public String getMonitorIp() {
        return monitorIp;
    }

    public void setMonitorIp(String monitorIp) {
        this.monitorIp = monitorIp;
    }

    public int getMonitorPort() {
        return monitorPort;
    }

    public void setMonitorPort(int monitorPort) {
        this.monitorPort = monitorPort;
    }

    public int getLoadBalancerPort() {
        return loadBalancerPort;
    }

    public void setLoadBalancerPort(int loadBalancerPort) {
        this.loadBalancerPort = loadBalancerPort;
    }

    public int getQueueSize() {
        return queueSize;
    }

    public void setQueueSize(int queueSize) {
        this.queueSize = queueSize;
    }
    
    
    
    @Override
    public String toString() {
        return this.getId();
    }
    
    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (!(obj instanceof Server))
            return false;
        Server svc = (Server) obj;
        return svc.getId().equals(this.getId());
    }
    
    @Override
    public int hashCode() {
        int hash = 7;
        hash = 31 * hash + (null == this.getId() ? 0 : this.getId().hashCode());
        return hash;
    }
    
    public static void main(String[] args) {
        Server s = new Server("127.0.0.5", 5000, "127.0.0.2", 5000,0,3);
        s.run();
    }
}

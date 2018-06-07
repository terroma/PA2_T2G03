/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package as.pa2.server;

import as.pa2.gui.ServerGUI;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Bruno Assunção 89010
 * @author Hugo Chaves  90842
 * 
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
    
    private transient Thread heartBeatThread;
    
    public Server(String host, int port, String monitorIp, int monitorPort, int queueSize) {
        this.host = host;
        this.serverPort = port;
        this.id = host + ":" + port;
        isAliveFlag = false;
        this.serverSocket = null;
        this.isStopped = false;
        this.runningThread = null;
        this.monitorIp = monitorIp;
        this.monitorPort = monitorPort;
        this.queueSize = queueSize;
        
        this.requestQueue = new LinkedBlockingQueue<RequestHandler>();
        this.threadPool = Executors.newFixedThreadPool(10);
    }
    
    public Server(String id) {
        setId(id);
        isAliveFlag = false;
    }
    
    public Server(ServerGUI serverGUI){
        this.serverGUI = serverGUI;
        this.isAliveFlag = false;
        this.isStopped = false;
        this.requestQueue = new LinkedBlockingQueue<RequestHandler>();
        this.threadPool = Executors.newFixedThreadPool(10);
    }
    
    @Override
    public void run() {
        this.isStopped = false;
        updateLogs("Starting Server ["+host+"]!");
        System.out.println("Starting Server ["+host+"]!");
        synchronized( this ) {
            this.runningThread = Thread.currentThread();
        }
        openServerSocket();
        updateLogs("Server ["+host+"] Connected.");
        System.out.println("Server ["+host+"] Connected.");
        notifyMonitor(monitorIp, monitorPort);
        
        heartBeatThread = new Thread(new Runnable() {
            @Override
            public void run() {
                ServerSocket srvSckt = null;
                while (true) {
                    try {
                        srvSckt = new ServerSocket(2000 , 10, InetAddress.getByName(host));
                        srvSckt.accept();
                    } catch (IOException ex) {
                        //System.out.println("[*] Server["+id+"] Error openning ping socket! ");
                    }
                }
            }
        });
        heartBeatThread.start();
        
        while ( !isStopped() ) {
            Socket clientSocket = null;
            
            try {
                clientSocket = this.serverSocket.accept();
                
                RequestHandler requestHandler = new RequestHandler(clientSocket, this, this.requestQueue, this.queueSize);
                this.requestQueue.add(requestHandler);
                this.threadPool.execute(requestHandler);
                
            } catch (IOException ioe) {
                if (isStopped()) {
                    updateLogs("Server Stopped!");
                    break;
                }
                throw new RuntimeException(
                        "Error accepting client connection.",ioe);
            } 
        }
        this.threadPool.shutdown();
        System.out.println("Shutting down pool ...");
    }
    
    public void sendStatistics(int threadId, int requestId) throws IOException {
        synchronized (monitorOutStream) {     
            String toSend = "Server["+id+"]: Thread: "+threadId+" processing request: "+requestId;
            monitorOutStream.writeUTF(toSend);
            monitorOutStream.flush();
        }
    }
    
    private void notifyMonitor(String monitorIp, int monitorPort) {
        try {
            this.monitorSocket = new Socket(monitorIp, monitorPort);
            monitorOutStream = new ObjectOutputStream(monitorSocket.getOutputStream());
            monitorOutStream.writeObject(this);
            monitorOutStream.flush();
        } catch (IOException ex) {
            updateLogs("[!] Server ["+this.id+"]: failed connection to monitor! "+ex.getMessage());
        }
    }
    
    private synchronized boolean isStopped() {
        return this.isStopped;
    }
    
    public synchronized void stop() {
        this.isStopped = true;
        try {
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

    public ServerGUI getServerGUI() {
        return serverGUI;
    }

    public void setServerGUI(ServerGUI serverGUI) {
        this.serverGUI = serverGUI;
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
    
    private void updateLogs(String s) {
        if (serverGUI != null) {
            serverGUI.updateLogs(s);
        }
    }
    
    @Override
    public int hashCode() {
        int hash = 7;
        hash = 31 * hash + (null == this.getId() ? 0 : this.getId().hashCode());
        return hash;
    }
    
}

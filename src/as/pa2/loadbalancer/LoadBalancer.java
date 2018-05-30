/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package as.pa2.loadbalancer;

import as.pa2.loadbalancer.strategies.IFRule;
import as.pa2.loadbalancer.strategies.RoundRobinRule;
import as.pa2.monitor.Monitor;
import as.pa2.protocol.PiRequest;
import as.pa2.server.Server;
import java.io.IOException;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.LinkedBlockingQueue;

/**
 * Load-Balancer Implementation.
 *
 * @author terroma
 */
public class LoadBalancer implements IFLoadBalancer, Runnable{
    
    private final static IFRule DEFAULT_RULE = new RoundRobinRule();
    private static final String DEFAULT_NAME = "lb-default";
    private static final String PREFIX = "load-balancer_";
    
    protected IFRule rule = DEFAULT_RULE;
    
    protected String name = DEFAULT_NAME;
    
    protected Monitor monitor;
    
    protected String ip;
    protected int port;
    protected ServerSocket socket;
    
    protected boolean isStopped;
    
    protected ExecutorService clientConnnectionsPool;
    
    protected LinkedBlockingQueue<PiRequest> requestQueue = new LinkedBlockingQueue<PiRequest>();
    protected ConcurrentHashMap<Integer,ClientConnection> clientConnections = new ConcurrentHashMap<Integer, ClientConnection>();
    
    public LoadBalancer() {
        this.name = DEFAULT_NAME;
        this.isStopped = false;
        setRule(DEFAULT_RULE);
        this.clientConnnectionsPool = Executors.newFixedThreadPool(10);
    }
    
    public LoadBalancer(String name, IFRule rule) {
        this.name = name;
        this.isStopped = false;
        setRule(rule);    
        this.clientConnnectionsPool = Executors.newFixedThreadPool(10);
    }
     
    @Override
    public Server chooseServer(Object key) {
        if (rule == null) {
            return null;
        } else {
            try {
                return rule.choose(key);
            } catch (Exception e) {
                System.out.printf("LoadBalancer [{}]: Error choosing server for "
                        + "key {}", name, key, e);
                return null;
            }
        }
    }

    public String choose(Object key) {
        if (rule == null) {
            return null;
        } else {
            try {
                Server server = rule.choose(key);
                return ((server == null) ? null : server.getId());
            } catch (Exception e) {
                System.out.printf("LoadBalancer [{}]: Error choosing server for "
                        + "key {}", name, key, e);
                return null;
            }
        }
    }

    @Override
    public List<Server> getReachableServers() {
        return monitor.getReachableServers();
    }

    @Override
    public List<Server> getAllServers() {
        return monitor.getReachableServers();
    }
    
    
    public IFRule getRule() {
        return rule;
    }
    
    public void setRule(IFRule rule) {
        if (rule != null) {
            this.rule = rule;
        } else {
            this.rule = new RoundRobinRule();
        }
        if (this.rule.getLoadBalancer() != this) {
            this.rule.setLoadBalancer(this);
        }
    }
    
    public String getName() {
        return name;
    }
    
    //TODO try this
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("{LoadBalancer:name=")
                .append(this.getName())
    //            .append(", current list of servers=").append(this.allServersList)
                .append("}");
        return sb.toString();
    }

    @Override
    public void run() {
        openClientsSocket();
        System.out.println("[*] LoadBalancer Started ...");
        int clientCount = 0;
        while (!isStopped()) {
            /* handle client connections */
            Socket clientSocket = null;
            try {
                clientSocket = this.socket.accept();
                System.out.println("[*] LoadBalancer recieved new client Connection.");
                ClientConnection newConnection = new ClientConnection(requestQueue, clientSocket, clientCount++);
                clientConnections.put(clientCount, newConnection);
                this.clientConnnectionsPool.execute(newConnection);
                
            } catch (IOException ioe) {
                if (isStopped()) {
                    System.out.println("[*] LoabBalancer Stopped!");
                    break;
                }
                throw new RuntimeException("[!] LoadBalancer: Error accepting connections from clients.",ioe);
            }
        }
    }
    
    private synchronized boolean isStopped() {
        return this.isStopped;
    }
    
    public synchronized void stop() {
        this.isStopped = true;
        try {
            System.out.println("LoabBalancer Stopped!");
            this.socket.close();
        } catch (IOException ioe) {
            throw new RuntimeException("Error closing LoadBalancer",ioe);
        }
    }
    
    private void openClientsSocket() {
        try {
            this.socket = new ServerSocket(this.port, 100, InetAddress.getByName(this.ip));
        } catch (IOException ioe) {
            throw new RuntimeException(
                    "Cannot open port "+this.port+"!", ioe);
        }
    }
    
}

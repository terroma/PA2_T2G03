/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package as.pa2.monitor;

import as.pa2.loadbalancer.availability.IFPing;
import as.pa2.loadbalancer.availability.IFPingStrategy;
import as.pa2.loadbalancer.listeners.ServerListChangeListener;
import as.pa2.loadbalancer.listeners.ServerStatusChangeListener;
import as.pa2.server.Server;
import as.pa2.server.ServerComparator;
import java.net.ServerSocket;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Timer;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

/**
 *
 * @author bruno
 */
public class Monitor implements Runnable{

    protected int monitorPort;
    protected ServerSocket monitorSocket;
    
    protected IFPingStrategy pingStrategy = DEFAULT_PING_STRATEGY;
    protected IFPing ping = null;
    
    protected volatile List<Server> allServersList = Collections.synchronizedList(new ArrayList<Server>());
    protected volatile List<Server> upServersList = Collections.synchronizedList(new ArrayList<Server>());
    
    protected ReadWriteLock allServerLock = new ReentrantReadWriteLock();
    protected ReadWriteLock upServerLock = new ReentrantReadWriteLock();
    
    protected Timer lbTimer = null;
    protected int pingIntervalSeconds = 10;
    protected int maxTotalPingTimeSeconds = 5;
    protected Comparator<Server> serverComparator = new ServerComparator();
    
    protected AtomicBoolean pingInProgress = new AtomicBoolean(false);
    
    private List<ServerListChangeListener> changeListeners = new CopyOnWriteArrayList<ServerListChangeListener>(); 
    private List<ServerStatusChangeListener> serverStatusListeners = new CopyOnWriteArrayList<ServerStatusChangeListener>();
    
    
    
    public Monitor(int monitorPort, ServerSocket monitorSocket) {
        this.monitorPort = monitorPort;
        this.monitorSocket = monitorSocket;
    }
    
    
    @Override
    public void run() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
}

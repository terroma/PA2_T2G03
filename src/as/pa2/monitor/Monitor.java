/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package as.pa2.monitor;

import as.pa2.gui.MonitorLBGUI;
import as.pa2.monitor.availability.IFPing;
import as.pa2.monitor.availability.IFPingStrategy;
import as.pa2.monitor.availability.SerialPingStrategy;
import as.pa2.monitor.listeners.ServerListChangeListener;
import as.pa2.monitor.listeners.ServerStatusChangeListener;
import as.pa2.server.Server;
import as.pa2.server.ServerComparator;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import static java.util.Collections.singleton;
import java.util.Comparator;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author bruno
 */
public class Monitor extends AbstractMonitor implements Runnable {

    private final static SerialPingStrategy DEFAULT_PING_STRATEGY = new SerialPingStrategy();
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
    
    protected String ip;
    protected int port;
    protected ServerSocket monitorSocket;
    protected boolean isStopped;
    
    private MonitorLBGUI monitorLBGui = null;
    
    public Monitor(String ip, int port) {
        this.ip = ip;
        this.port = port;
        this.isStopped = false;
        setupPingTask();
    }
    
    public Monitor(String ip, int port, IFPing ping, IFPingStrategy pingStrategy) {
        this.ip = ip;
        this.port = port;
        this.ping = ping;
        this.pingStrategy = pingStrategy;
        this.isStopped = false;
        setupPingTask();
    }
    
    public Monitor(MonitorLBGUI monitorLbGui){
        this.monitorLBGui = monitorLbGui;
    }
    
    /**
     * Method used to aquire lock for allServersList
     * 
     * @param write boolean to aquire read or write Lock
     * @return Lock
     */
    public Lock lockAllServersList(boolean write) {
        Lock aproposLock = write ? allServerLock.writeLock() : allServerLock.readLock();
        aproposLock.lock();
        return aproposLock;
    }
    
    /**
     * Method used to aquire lock for upServersList
     * 
     * @param write boolean to aquire read or write Lock
     * @return Lock
     */
    public Lock lockUpServersList(boolean write) {
        Lock aproposLock = write ? upServerLock.writeLock() : upServerLock.readLock();
        aproposLock.lock();
        return aproposLock;
    }
    
    @Override
    public void run() {
        openMonitorSocket();
        monitorLBGui.updateLogs("Monitor Started!");
        
        Socket serverSocket = null;
        while (!isStopped()) {
            try {
                serverSocket = this.monitorSocket.accept();
                System.out.println(monitorSocket.toString());
                monitorLBGui.updateLogs("Monitor: accepted connection from server: " + serverSocket.toString());
                ObjectInputStream oInStream =
                        new ObjectInputStream(serverSocket.getInputStream());
                
                Server newServer = (Server) oInStream.readObject();
                if (newServer != null) {
                    addServer(newServer);
                }
                //System.out.println(allServersList.toString());
            } catch (IOException ioe) {
                if (isStopped()) {
                    monitorLBGui.updateLogs("Monitor Stopped!");
                    try {
                        if(serverSocket!=null){
                           serverSocket.close(); 
                        }
                    } catch (IOException ex) {
                        Logger.getLogger(Monitor.class.getName()).log(Level.SEVERE, null, ex);
                    }
                    //break;
                }
            } catch (ClassNotFoundException ex) {
                Logger.getLogger(Monitor.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    private void openMonitorSocket() {
        try {
            this.monitorSocket = new ServerSocket(this.port, 100, InetAddress.getByName(this.ip));
        } catch (IOException ioe) {
            throw new RuntimeException(
                    "Cannot open port "+this.port+"!", ioe);
        }
    }
    
    private synchronized boolean isStopped() {
        return this.isStopped;
    }
    
    public List<Server> getServerList(boolean availableOnly) {
        return (availableOnly ? getReachableServers() : getAllServers());
    }
    
    @Override
    public List<Server> getServerList(ServerGroup serverGroup) {
        switch (serverGroup) {
            case ALL:
                return allServersList;
            case STATUS_UP:
                return upServersList;
            case STATUS_NOT_UP:
                ArrayList<Server> notAvailableServers = new ArrayList<Server>(
                        allServersList);
                ArrayList<Server> upServers = new ArrayList<Server>(
                        upServersList);
                notAvailableServers.removeAll(upServers);
                return notAvailableServers;
        }
        return new ArrayList<Server>();
    }

    /**
     * Add a server to the 'allServers' list.
     * give a server a greater share by adding it more than once.
     * 
     * @param newServer 
     */
    public void addServer(Server newServer) {
        // added condition to not duplicate server
        if (newServer != null && !allServersList.contains(newServer)) {
            try {
                ArrayList<Server> newList = new ArrayList<Server>();
                newList.addAll(allServersList);
                newList.add(newServer);
                setServersList(newList);
            } catch (Exception e) {
                monitorLBGui.updateLogs("Monitor: Error adding newServer "+newServer.getId());
            }
        }
    }
    
    @Override
    public void addServers(List<Server> newServers) {
        if (newServers != null && newServers.size() > 0 ) {
            try {
                ArrayList<Server> newList = new ArrayList<Server>();
                newList.addAll(allServersList);
                newList.addAll(newServers);
                setServersList(newList);
            } catch (Exception e) {
                monitorLBGui.updateLogs("Monitor: Error adding newServers "+newServers.size());
            }
        }
    }

    public void markServerDown(String host, int port) {
        boolean triggered = false;
        
        String id = host + ":" + port;
        
        Lock writeLock = upServerLock.writeLock();
        writeLock.lock();
        try {
            final List<Server> changedServers = new ArrayList<Server>();
            
            for (Server svr : upServersList) {
                if (svr.isAlive() && (svr.getId().equals(id))) {
                    triggered = true;
                    svr.setAlive(false);
                    changedServers.add(svr);
                }
            }
            
            if (triggered) {
                monitorLBGui.updateLogs("Monitor: markServerDown called on " + id);
                notifyServerStatusChangeListener(changedServers);
            }
        } finally {
            writeLock.unlock();
        }
    }
    
    @Override
    public void markServerDown(Server server) {
        if (server == null || !server.isAlive()) {
            return;
        }
        server.setAlive(false);
        //forceQuickPing();
        
        notifyServerStatusChangeListener(singleton(server));
    }
    

    @Override
    public List<Server> getReachableServers() {
        return Collections.unmodifiableList(upServersList);
    }

    @Override
    public List<Server> getAllServers() {
        return Collections.unmodifiableList(allServersList);
    }
    
    public void addServerListChangeListener(ServerListChangeListener listener) {
        changeListeners.add(listener);
    }
    
    public void removeServerListChangeListener(ServerListChangeListener listener) {
        changeListeners.remove(listener);
    }
    
    public void addServerStatusChangeListener(ServerStatusChangeListener listener) {
        serverStatusListeners.add(listener);
    }
    
    public void removeServerStatusChangeListener(ServerStatusChangeListener listener) {
        serverStatusListeners.remove(listener);
    }
    
    public boolean isPingInProgress() {
        return pingInProgress.get();
    }
    
    
    public void setServersList(List serversList) {
        Lock writeLock = allServerLock.writeLock();        
        ArrayList<Server> newServers = new ArrayList<Server>();
        writeLock.lock();
        try {
            ArrayList<Server> allServers = new ArrayList<Server>();
            for (Object server : serversList) {
                if (server == null) {
                    continue;
                }
                
                if (server instanceof String) {
                    server = new Server((String) server);
                }
                
                if (server instanceof Server) {
                    allServers.add((Server) server);
                } else {
                    throw new IllegalArgumentException(
                            "Type String or Server expected, instead found: "
                            +server.getClass());
                }
            }
            boolean listChanged = false;
            if (!allServersList.equals(allServers)) {
                listChanged = true;
                if (changeListeners != null && changeListeners.size() > 0) {
                    // should be immutable
                    final List<Server> oldList = Collections.unmodifiableList(allServersList);
                    final List<Server> newList = Collections.unmodifiableList(allServers);
                    for (ServerListChangeListener listener : changeListeners) {
                        try {
                            listener.serverListChanged(oldList, newList);
                        } catch (Exception e) {
                            monitorLBGui.updateLogs("LoadBalancer Error invoking server list change listener");       
                        }
                    }
                }
            }
            // this will reset readyToServe flag to true on all servers
            // regardless wether previous connections are success or not
            allServersList = allServers;
            if (canSkipPing()) {
                for (Server s: allServersList) {
                    s.setAlive(true);
                }
                upServersList = allServersList;
            } else if (listChanged) {
                forceQuickPing();
            }
        } finally {
            writeLock.unlock();
        }
    }
    
    /**
     * Method to get number of up servers or all servers count.
     * 
     * @param onlyAvailable 
     * @return up/all servers count
     */
    public int getServerCount(boolean onlyAvailable) {
        if (onlyAvailable) {
            return upServersList.size();
        } else {
            return allServersList.size();
        }
    }
    
    public IFPing getPing() {
        return ping;
    }
    
    public void setPing(IFPing ping) {
        if (ping != null) {
            if (!ping.equals(this.ping)) {
                this.ping = ping;
                setupPingTask();
            }
        } else {
            this.ping = null;
            lbTimer.cancel();
        }
    }

    public MonitorLBGUI getMonitorLBGui() {
        return monitorLBGui;
    }

    public void setMonitorLBGui(MonitorLBGUI monitorLBGui) {
        this.monitorLBGui = monitorLBGui;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }
    
    public Server getServerByIndex(int index, boolean availableOnly) {
        try {
            return (availableOnly ? upServersList.get(index) : allServersList.get(index));
        } catch (Exception e) {
            monitorLBGui.updateLogs("Monitor: Server not found running getServerByIndex("+index+","+availableOnly+")");
        }   return null;
    }
    
    private void notifyServerStatusChangeListener(final Collection<Server> changedServers) {
        if (changedServers != null && !changedServers.isEmpty() && !serverStatusListeners.isEmpty()) {
            for (ServerStatusChangeListener listener : serverStatusListeners) {
                try {
                    listener.serverStatusChanged(changedServers);
                } catch (Exception e) {
                    monitorLBGui.updateLogs("Monitor: Error invoking server status change listener" + e);
                }
            }
        }
    }
    
    public synchronized void shutdown() {
        cancelPingTask();
        this.isStopped = true;
        try {
            this.monitorSocket.close();
        } catch (IOException ioe) {
            throw new RuntimeException(
                    "Error shutingdown monitor", ioe);
        }
    }
    
    /*--------------------- PING PART OF LOADBALANCER ---------------------*/
    public void cancelPingTask() {
        if (lbTimer != null) {
            lbTimer.cancel();
        }
    }
    
    public int getMaxTotalPingTime() {
        return maxTotalPingTimeSeconds;
    }
    
    public void setMaxTotalPingTime(int maxTotalPingTimeSeconds) {
        if (maxTotalPingTimeSeconds < 1) {
            return;
        }
        this.maxTotalPingTimeSeconds = maxTotalPingTimeSeconds;
    }
    
    public int getPingInterval() {
        return pingIntervalSeconds;
    }
    
    public void setPingInterval(int pingIntervalSeconds) {
        if (pingIntervalSeconds < 1) {
            return;
        }
        this.pingIntervalSeconds = pingIntervalSeconds;
        setupPingTask();
    }
    /**
     * Force an immediate ping, if we're not currently pinging and don't
     * have a quick-ping already scheduled.
     */
    public void forceQuickPing() {
        if (canSkipPing()) {
            return;
        }        
        try {
            new Pinger(pingStrategy).runPinger();
        } catch (Exception e) {
            monitorLBGui.updateLogs("Monitor: Error running forceQuickPing()" + e);
        }
    }
    
    private boolean canSkipPing() {
        return ping == null;
    }
    
    private void setupPingTask() {
        if (canSkipPing()) {
            return;
        }
        if (lbTimer != null) {
            lbTimer.cancel();
        }
        lbTimer = new Timer("Monitor-HeartBeatTimer", true);
        lbTimer.schedule(new PingTask(), 0, pingIntervalSeconds * 1000);
        System.out.println("[*] Monitor: setupPingTask ...");
    }
    
    /**
     * TimerTask that keeps runs every X seconds to check the status of each
     * server/node in the Server List
     */
    class PingTask extends TimerTask {
        @Override
        public void run() {
            try {
                new Pinger(pingStrategy).runPinger();
            } catch (Exception e) {
                System.out.println("Monitor: Error pinging.");
                e.printStackTrace();
            }
        }
    }
    
    /**
     * Class that contains the mechanism to "ping" all the instances of
     * servers/nodes
     */
    class Pinger {
        
        private final IFPingStrategy pingerStrategy;
        
        public Pinger(IFPingStrategy pingerStrategy) {
            this.pingerStrategy = pingerStrategy;
        }
        
        public void runPinger() throws Exception {
            if (!pingInProgress.compareAndSet(false, true)) {
                return; // Ping in progress
            }
            //System.out.println("[*] Monitor: pinging ...");
            // we get to Ping
            Server[] allServers = null;
            boolean[] results = null;
            
            Lock allLock = null;
            Lock upLock = null;
            
            try {
                /**
                 * The readLock should be free unless an addServer operation
                 * is going on ...
                 */
                allLock = allServerLock.readLock();
                allLock.lock();
                allServers = allServersList.toArray(new Server[allServersList.size()]);
                allLock.unlock();
                
                int numCandidates = allServers.length;
                results = pingerStrategy.pingServers(ping, allServers);
                
                final List<Server> newUpList = new ArrayList<Server>();
                final List<Server> changedServers = new ArrayList<Server>();
                
                for (int i=0; i < numCandidates; i++) {
                    boolean isAlive = results[i];
                    Server server = allServers[i];
                    boolean oldIsAlive = server.isAlive();
                    
                    server.setAlive(isAlive);
                    
                    if (oldIsAlive != isAlive) {
                        changedServers.add(server);
                        //System.out.printf("Monitor: Server [{}] status "
                        //    +"changed to {}", server.getId(), (isAlive ? "ALIVE" : "DEAD"));
                    }
                    
                    if (isAlive) {
                        newUpList.add(server);
                    }
                    upLock = upServerLock.writeLock();
                    upLock.lock();
                    upServersList = newUpList;
                    upLock.unlock();
                    
                    notifyServerStatusChangeListener(changedServers);
                }
            } finally {
                pingInProgress.set(false);
                monitorLBGui.updateServerList(getAllServers());
            }
        }
    }
    
    public static void main(String[] args) {
        Monitor m = new Monitor("127.0.0.2", 5000);
        m.run();
    } 
}

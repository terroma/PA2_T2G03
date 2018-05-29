/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package as.pa2.loadbalancer;

import as.pa2.monitor.availability.IFPing;
import as.pa2.monitor.availability.IFPingStrategy;
import as.pa2.monitor.availability.SerialPing;
import as.pa2.monitor.availability.SerialPingStrategy;
import as.pa2.monitor.listeners.ServerListChangeListener;
import as.pa2.monitor.listeners.ServerStatusChangeListener;
import as.pa2.loadbalancer.strategies.IFRule;
import as.pa2.loadbalancer.strategies.RoundRobinRule;
import as.pa2.monitor.AbstractMonitor;
import as.pa2.monitor.Monitor;
import as.pa2.server.Server;
import as.pa2.server.ServerComparator;
import java.net.ServerSocket;
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

/**
 * Load-Balancer Implementation.
 *
 * @author terroma
 */
public class LoadBalancer implements IFLoadBalancer {
    
    private final static IFRule DEFAULT_RULE = new RoundRobinRule();
    private static final String DEFAULT_NAME = "lb-default";
    private static final String PREFIX = "load-balancer_";
    
    protected IFRule rule = DEFAULT_RULE;
    
    protected String name = DEFAULT_NAME;
    
    protected Monitor monitor;
    
    protected String ip;
    protected int port;
    protected ServerSocket socket;
    
    public LoadBalancer() {
        this.name = DEFAULT_NAME;
        setRule(DEFAULT_RULE);
    }
    
    public LoadBalancer(String name, IFRule rule) {
        this.name = name;
        setRule(rule);        
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
    
}

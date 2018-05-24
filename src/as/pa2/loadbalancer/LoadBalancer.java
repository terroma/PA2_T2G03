/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package as.pa2.loadbalancer;

import as.pa2.loadbalancer.availability.IFPing;
import as.pa2.loadbalancer.availability.IFPingStrategy;
import as.pa2.loadbalancer.availability.SerialPingStrategy;
import as.pa2.loadbalancer.strategies.IFRule;
import as.pa2.loadbalancer.strategies.RoundRobinRule;
import as.pa2.server.Server;
import java.util.List;

/**
 * Load-Balancer Implementation.
 *
 * @author terroma
 */
public class LoadBalancer extends AbstractLoadBalancer {
    
    private final static IFRule DEFAULT_RULE = new RoundRobinRule();
    private final static SerialPingStrategy DEFAULT_PING_STRATEGY = new SerialPingStrategy();
    private static final String DEFAULT_NAME = "default";
    private static final String PREFIX = "load-balancer_";
    
    protected IFRule rule = DEFAULT_RULE;
    protected IFPingStrategy pingStrategy = DEFAULT_PING_STRATEGY;
    protected IFPing ping = null;
    
    @Override
    public List<Server> getServerList(ServerGroup serverGroup) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void addServers(List<Server> newServers) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public Server chooseServer(Object key) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void markServerDown(Server server) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public List<Server> getReachableServers() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public List<Server> getAllServers() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
}

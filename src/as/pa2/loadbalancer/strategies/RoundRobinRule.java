/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package as.pa2.loadbalancer.strategies;

import as.pa2.loadbalancer.IFLoadBalancer;
import as.pa2.server.Server;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

/**
 *
 * @author Bruno Assunção 89010
 * @author Hugo Chaves  90842
 * 
 */

public class RoundRobinRule extends AbstractLoadBalancerRule {
    
    private AtomicInteger nextServerCyclicCounter;
    private static final boolean AVAILABLE_ONLY_SERVERS = true;
    private static final boolean ALL_SERVERS = false;
    
    public RoundRobinRule() {
        nextServerCyclicCounter = new AtomicInteger(0);
    }
    
    public RoundRobinRule(IFLoadBalancer lb) {
        this();
        setLoadBalancer(lb);
    }
    
    public Server choose(IFLoadBalancer lb, Object key) {
        if (lb == null) {
            System.out.println("Null Load-Balancer!!!");
            return null;
        }
        
        Server server = null;
        int count = 0;
        while (server == null && count++ < 10) {
            List<Server> reachableServers = lb.getReachableServers();
            List<Server> allServers = lb.getAllServers();
            int upCount = reachableServers.size();
            int serverCount = allServers.size();
            
            if ((upCount == 0) || (serverCount == 0)) {
                System.out.println("No servers available from load-balancer: "+lb);
                return null;
            }
            
            int nextServerIndex = incrementAndGetModulo(serverCount);
            server = allServers.get(nextServerIndex);
            
            if (server == null) {
                /* Transient */
                Thread.yield();
                continue;
            }
            
            if (server.isAlive() && (server.isReadyToServe())) {
                return (server);
            }
            
            server = null;
        }
        
        if (count >= 20) {
            System.out.println("No available alive servers after 20 tries"
                    + "from load-balancer: "+lb);
        }
        return server;
    }
 
    /**
     * Inspired by implementation of {@link AtomicInteger#incrementAndGet()}
     * 
     * @param modulo The modulo bound the value of the counter.
     * @return The next value.
     */
    private int incrementAndGetModulo(int modulo) {
        for(;;) {
            int current = nextServerCyclicCounter.get();
            int next = (current + 1) % modulo;
            if (nextServerCyclicCounter.compareAndSet(current, next))
                return next;
        }
    }

    @Override
    public Server choose(Object key) {
        return choose(getLoadBalancer(), key);
    }
}

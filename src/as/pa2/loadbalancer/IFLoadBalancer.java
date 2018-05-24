/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package as.pa2.loadbalancer;

import as.pa2.server.Server;
import java.util.List;

/**
 * Interface that defines operations for a software load-balancer.
 *
 * @author terroma
 */
public interface IFLoadBalancer {
    
    /**
     * Initial list of servers.
     * 
     * @param newServers new servers to add
     */
    public void addServers(List<Server> newServers);
    
    /**
     * Choose a server from load-balancer.
     * 
     * @param key An object that the load-balancer may use to determine
     * which server to return. null if the load-balancer does not use
     * this parameter.
     * @return server chosen
     */
    public Server chooseServer(Object key);
    
    /**
     * To be called by the clients of the load-balancer to notify that
     * a Server is down else, the load-balancer will think its still Alive
     * until the next Ping cycle.
     * 
     * @param server 
     */
    public void markServerDown(Server server);
    
    /**
     * @return Only the servers that are up and reachable.
     */
    public List<Server> getReachableServers();
    
    /**
     * @return All known servers, both reachable and unreachable
     */
    public List<Server> getAllServers();
}

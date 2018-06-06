/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package as.pa2.loadbalancer.strategies;

import as.pa2.loadbalancer.IFLoadBalancer;
import as.pa2.server.Server;

/**
 * Interface that defines a "Rule" for a load-balancer. A Rule can be thought of
 * as a Strategy for load-balancing.
 * EX: Round Robin, Response Time based etc.
 *
 * @author Bruno Assunção 89010
 * @author Hugo Chaves  90842
 * 
 */

public interface IFRule {
    
    /**
     * Choose one alive server from lb.allServers or 
     * lb.upServers according to key.
     * 
     * @param key
     * @return chosen Server object. null is returned 
     * if none server is available. 
     */
    public Server choose(Object key);
    
    public void setLoadBalancer(IFLoadBalancer lb);
    
    public IFLoadBalancer getLoadBalancer();
}

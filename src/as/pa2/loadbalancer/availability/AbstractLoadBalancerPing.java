/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package as.pa2.loadbalancer.availability;

import as.pa2.loadbalancer.AbstractLoadBalancer;
import as.pa2.loadbalancer.IFLoadBalancer;
import as.pa2.server.Server;

/**
 * Class that provides the basic implementation of determining the 
 * suitability of a Server (node).
 *
 * @author terroma
 */
public abstract class AbstractLoadBalancerPing implements IFPing {
    
    private IFLoadBalancer lb;
    
    @Override
    public void setLoadBalancer(IFLoadBalancer lb) {
        this.lb = lb;
    }
    
    @Override
    public IFLoadBalancer getLoadBalancer() {
        return lb;
    }
}

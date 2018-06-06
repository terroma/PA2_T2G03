/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package as.pa2.loadbalancer.strategies;

import as.pa2.loadbalancer.IFLoadBalancer;

/**
 * Class that provides a default implementation for 
 * getting and setting load-balancer.
 *
 * @author Bruno Assunção 89010
 * @author Hugo Chaves  90842
 * 
 */

public abstract class AbstractLoadBalancerRule implements IFRule {
    
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

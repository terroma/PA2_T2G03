/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package as.pa2.monitor.availability;

import as.pa2.loadbalancer.IFLoadBalancer;
import as.pa2.monitor.IFMonitor;
import as.pa2.server.Server;

/**
 * Interface that defines how we "Ping" a server to check if its alive.
 *
 * @author terroma
 */
public interface IFPing {
    
    /**
     * Checks whether the given <coce>Server</code> is "alive"
     * i.e. should be considered a candidate for load-balancing.
     * 
     * @param server
     * @return 
     */
    public boolean isAlive(Server server);
    
    public void setMonitor(IFMonitor monitor);
    
    public IFMonitor getMonitor();
}

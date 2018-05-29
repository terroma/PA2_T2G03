/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package as.pa2.monitor.availability;

import as.pa2.monitor.availability.IFPing;
import as.pa2.server.Server;

/**
 * Defines the strategy, used to ping all servers registered.
 *
 * @author terroma
 */
public interface IFPingStrategy {
    
    public boolean[] pingServers(IFPing ping, Server[] servers);
}

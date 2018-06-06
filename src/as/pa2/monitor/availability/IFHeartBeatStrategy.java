/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package as.pa2.monitor.availability;

import as.pa2.server.Server;
import as.pa2.monitor.availability.IFHeartBeat;

/**
 * Defines the strategy, used to ping all servers registered.
 *
 * @author Bruno Assunção 89010
 * @author Hugo Chaves  90842
 * 
 */

public interface IFHeartBeatStrategy {
    
    public boolean[] pingServers(IFHeartBeat ping, Server[] servers);
}

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package as.pa2.monitor.listeners;

import as.pa2.server.Server;
import java.util.Collection;

/**
 *
 * @author terroma
 */
public interface ServerStatusChangeListener {
 
    /**
     * @param servers the servers that had their status changed
     */
    public void serverStatusChanged(Collection<Server> servers);
}

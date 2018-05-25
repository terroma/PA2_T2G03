/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package as.pa2.loadbalancer.listeners;

import as.pa2.server.Server;
import java.util.List;

/**
 *
 * @author terroma
 */
public interface ServerListChangeListener {
    
    public void serverListChanged(List<Server> oldList, List<Server> newList);
}

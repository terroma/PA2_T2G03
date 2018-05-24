/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package as.pa2.loadbalancer;

import as.pa2.server.Server;
import java.util.List;

/**
 * 1. A List of Servers (nodes) that are potentially bucketed
 *      based on a specific criteria.
 * 2. A Class that defines and implements a LoadBalancing Strategy
 *      via <code>IFRule</code>
 * 3. A Class that defines and implements a mechanism to determine
 *      the suitability/availability of the servers/nodes in the List.
 *
 * @author terroma
 */
public abstract class AbstractLoadBalancer implements IFLoadBalancer {
    
    public enum ServerGroup {
        ALL,
        STATUS_UP,
        STATUS_NOT_UP
    }
    
    /**
     * List of servers that this load-balancer knows about.
     * 
     * @param serverGroup Servers grouped by status, ex {@link ServerGroup#STATUS_UP}
     */
    public abstract List<Server> getServerList(ServerGroup serverGroup);
}

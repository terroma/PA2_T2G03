package as.pa2.monitor;

import as.pa2.server.Server;
import java.util.List;

/**
 *
 * @author terroma
 */
public interface IFMonitor {
    
    /**
     * Initial list of servers.
     * 
     * @param newServers new servers to add
     */
    public void addServers(List<Server> newServers);
    
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

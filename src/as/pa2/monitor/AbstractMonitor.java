package as.pa2.monitor;

import as.pa2.server.Server;
import java.util.List;

/**
 *
 * @author Bruno Assunção 89010
 * @author Hugo Chaves  90842
 * 
 */

public abstract class AbstractMonitor implements IFMonitor {
    
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

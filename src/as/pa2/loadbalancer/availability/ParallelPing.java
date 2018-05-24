/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package as.pa2.loadbalancer.availability;

import as.pa2.server.Server;
import java.io.IOException;
import java.net.InetAddress;
import java.util.concurrent.Callable;

/**
 *
 * @author terroma
 */
public class ParallelPing extends AbstractLoadBalancerPing implements Callable<Boolean> {

    private String ipToPing;
    
    public ParallelPing(String ipToPing) {
        this.ipToPing = ipToPing;
    }
    
    @Override
    public Boolean call() {
        InetAddress addr = null;
        boolean result = false;
        try {
            addr = InetAddress.getByName(ipToPing);
            result = addr.isReachable(5000) ? true : false;
            return result;
        } catch (IOException e) {
            e.printStackTrace();
            return result;
        }
    }
    
    
    @Override
    public boolean isAlive(Server server) {
        // TODO
        return true;
    }
}

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package as.pa2.loadbalancer.availability;

import as.pa2.server.Server;
import java.io.IOException;
import java.net.InetAddress;

/**
 *
 * @author terroma
 */
public class SerialPing extends AbstractLoadBalancerPing {

    @Override
    public boolean isAlive(Server server) {
        InetAddress addr = null;
        boolean alive = false;
        try {
            addr = InetAddress.getByName(server.getHost());
            alive = addr.isReachable(5000) ? true : false;
            return alive;
        } catch (IOException e) {
            e.printStackTrace();
            return alive;
        }
    }
    
}

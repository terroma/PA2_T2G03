/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package as.pa2.monitor.availability;

import as.pa2.server.Server;

/**
 * Serial Pinging implementation.
 *
 * @author terroma
 */
public class SerialPingStrategy implements IFPingStrategy {

    @Override
    public boolean[] pingServers(IFPing ping, Server[] servers) {
        int numCandidates = servers.length;
        boolean[] results = new boolean[numCandidates];
        System.out.println("PingTask executing "
                +numCandidates+" servers configured");
        for (int i=0; i < numCandidates; i++) {
            results[i] = false;
            try {
                if (ping != null) {
                    results[i] = ping.isAlive(servers[i]);
                    System.out.println("PingResult server:"+servers[i].getId()+" result: "+results[i]);
                }
            } catch (Exception e) {
                System.out.println("Exception while pinging Server: "
                        +servers[i]);
                e.printStackTrace();
            }
        }
        return results;
    }

}

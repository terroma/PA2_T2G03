/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package as.pa2.monitor.availability;

import as.pa2.server.Server;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Parallel Ping Implementation.
 *
 * @author terroma
 */
public class ParallelPingStategy implements IFPingStrategy {

    @Override
    public boolean[] pingServers(IFPing ping, Server[] servers) {
        int numCandidates = servers.length;
        boolean[] results = null;
        System.out.println("PingTask executing "
                +numCandidates+" servers configured");
        
        if (numCandidates > 0) {
            ExecutorService executor = Executors.newFixedThreadPool(numCandidates);
            List<Future<Boolean>> list = new ArrayList<Future<Boolean>>(numCandidates);
            Callable<Boolean> callable = null;
            for (int i=0; i < numCandidates; i++) {
                callable = new ParallelPing(servers[i].getHost(),servers[i].getPort());
                Future<Boolean> future = executor.submit(callable);
                list.add(future);
            }
            executor.shutdown();
        
            try {
                 results = toPrimitiveArray(list);
            } catch (InterruptedException | ExecutionException ex) {
                System.out.println("Error converting list to array!"+ex.getLocalizedMessage());
            }
        }
        return results;
    }
    
    private boolean[] toPrimitiveArray(final List<Future<Boolean>> booleanList) throws InterruptedException, ExecutionException {
        final boolean[] primitives = new boolean[booleanList.size()];
        int index = 0;
        for(Future<Boolean> obj : booleanList) {
            primitives[index++] = obj.get();
        }
        return primitives;
    }
}

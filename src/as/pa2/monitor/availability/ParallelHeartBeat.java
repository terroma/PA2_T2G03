/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package as.pa2.monitor.availability;

import as.pa2.server.Server;
import java.io.IOException;
import java.net.ConnectException;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.NoRouteToHostException;
import java.net.Socket;
import java.net.SocketTimeoutException;
import java.util.concurrent.Callable;

/**
 *
 * @author Bruno Assunção 89010
 * @author Hugo Chaves  90842
 * 
 */

public class ParallelHeartBeat extends AbstractMonitorHeartBeat implements Callable<Boolean> {

    private String ipToPing;
    private int portToPing;
    
    public ParallelHeartBeat() {
        
    }
    
    public ParallelHeartBeat(String ipToPing, int portToPing) {
        this.ipToPing = ipToPing;
        this.portToPing = portToPing;
    }
    
    @Override
    public Boolean call() {
        InetAddress addr = null;
        boolean result = false;
        try {
            addr = InetAddress.getByName(ipToPing);
            result = hasService(addr, portToPing);
            //System.out.println("PingResult server:"+ipToPing+" result: "+result);
            return result;
        } catch (IOException e) {
            e.printStackTrace();
            return result;
        }
    }
    
    private boolean hasService(InetAddress host, int port) throws IOException {
        boolean alive = false;
        Socket sock = new Socket();
        
        try {
            sock.connect(new InetSocketAddress(host, port), 200);
            if (sock.isConnected()) {
                sock.close();
                alive = true;
            }
        } catch (ConnectException | NoRouteToHostException | SocketTimeoutException ex) {  }
        return alive;
    }
    
    @Override
    public boolean isAlive(Server server) {
        // TODO
        return true;
    }
}

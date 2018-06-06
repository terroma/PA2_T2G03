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

/**
 *
 * @author Bruno Assunção 89010
 * @author Hugo Chaves  90842
 * 
 */

public class SerialHeartBeat extends AbstractMonitorHeartBeat {

    @Override
    public boolean isAlive(Server server) {
        InetAddress addr = null;
        boolean alive = false;
        try {
            addr = InetAddress.getByName(server.getHost());
            alive = hasService(addr, 2000);
            return alive;
        } catch (IOException e) {
            e.printStackTrace();
            return alive;
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
}
package as.pa2.loadbalancer;

import java.io.IOException;
import java.net.InetAddress;
import java.net.ServerSocket;

public class LoadBalancerProcess extends LoadBalancer implements Runnable {

    protected String ip;
    protected int listenningPort;
    protected ServerSocket listenningSocket;
    
    protected boolean isStopped;
    protected Thread runningThread;
    
    public LoadBalancerProcess(String ip, int port) {
        this.ip = ip;
        this.listenningPort = port;
    }
    
    @Override
    public void run() {
        
    }    
    
    private void openListenningSocket() {
        try {
            this.listenningSocket = new ServerSocket(this.listenningPort, 100, InetAddress.getByName(this.ip));
        } catch(IOException ioe) {
            throw new RuntimeException(
                    "Cannot open listenning port "+this.listenningPort, ioe);
        } 
    }
    
    private synchronized boolean isStopped() {
        return this.isStopped;
    }
    
    public synchronized void stop() {
        this.isStopped = true;
        try {
            this.listenningSocket.close();
        } catch (IOException ioe) {
            throw new RuntimeException(
                    "Error closing load-balancer-process!",ioe);
        }
    }
}

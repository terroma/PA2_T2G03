package as.pa2.client;

import as.pa2.gui.ClientGUI;
import as.pa2.protocol.PiRequest;
import as.pa2.protocol.PiResponse;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.Socket;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.UUID;

/**
 *
 * @author Bruno Assunção 89010
 * @author Hugo Chaves  90842
 * 
 */

public class Client {

    private static final boolean TEST = false;
    private InetAddress connectedAddress;
    private Socket tcpSocket;
    private int loadBalancerPort;
    private String loadBalancerIP;
    private ObjectInputStream oInStream;
    private ObjectOutputStream oOutStream;
    
    private int clientId;
    private UUID uniqueClientId = UUID.randomUUID();
    
    private ClientGUI clientGUI;
    
    /*
    // default client constructor 
    public Client() throws IOException {
        this.clientId = uniqueClientId.hashCode();
        initClient("127.0.0.1",3000);
    }
    */
    
    public Client(String loadBalancerIP, int loadNBalancerPort) {
        this.clientId = uniqueClientId.hashCode();
        this.loadBalancerIP = loadBalancerIP;
        this.loadBalancerPort = loadNBalancerPort;
        initClient(loadBalancerIP, loadNBalancerPort);
    }
    
    public Client(ClientGUI clienteGui){
        this.clientGUI = clienteGui;
        this.clientId = uniqueClientId.hashCode();
    }
    
    public void init() {
        initClient(loadBalancerIP, loadBalancerPort);
    }
    
    private void initClient(String host, int port) {
        try {
            updateLogs("Starting Client ["+clientId+"]. \n");
            updateDebugLogs("Starting Client ["+clientId+"]...");
            this.connectedAddress = Inet4Address.getByName(host);
            this.loadBalancerPort = port;
            this.tcpSocket = new Socket(host, port);
            
            updateLogs("Client ["+clientId+"] Connected on port:"+loadBalancerPort + "\n");
            updateDebugLogs("Client ["+clientId+"] Connected on port:"+loadBalancerPort);
            
            this.oOutStream = new ObjectOutputStream(tcpSocket.getOutputStream());

            (new Thread(new InputListeningThread())).start();

        } catch (SocketException se) {
            updateLogs("[!] Client ["+clientId+"] failed to connect, please check if LoadBalancer is running. \n");
        } catch (UnknownHostException uhe) {
            updateLogs("[!] UnknownHostException! Client["+clientId+"] \n");
            uhe.printStackTrace();
        } catch (IOException ioe) {
            updateLogs("[!] IOException! Client["+clientId+"] \n");
            ioe.printStackTrace();
        }

    }
    
    private void openSocket(String ip, int port) {
        try {
            this.tcpSocket = new Socket(ip, port);
        } catch (Exception e) {
            updateLogs("Error openning socket"+e.getMessage() + "\n");
        }
    }
    
    public void sendMessage(PiRequest request) throws IOException {
        synchronized(this) {
            if(!this.tcpSocket.isConnected())
                return;
            try {
                updateLogs("Client["+clientId+"] Sending Request: \n");
                updateDebugLogs("Client["+clientId+"] Sending Request: ");
                updateLogs("[ "+request.toString() + " ] \n");
                updateDebugLogs("[ "+request.toString() + " ] ");
                this.oOutStream.writeObject(request);
                this.oOutStream.flush();
            } catch (IOException ioe) {
                updateLogs("[!] IOException! Client["+clientId+"] \n");
                ioe.printStackTrace();
            }
            
        }
    }
    
    public void stop() {
        try {
            this.oInStream.close();
            this.oOutStream.close();
            this.tcpSocket.close();
        } catch (IOException ex) {
            updateLogs("[!] IOException :"+ex.getMessage() + "\n");
        }
    }
    
    private class InputListeningThread implements Runnable {

        public InputListeningThread() { }

        @Override
        public void run() {
            try {
                oInStream = new ObjectInputStream(tcpSocket.getInputStream());

                while (true) {
                    PiResponse response = (PiResponse) oInStream.readObject();
                    if (response != null) {
                        updateLogs("Client ["+clientId+"] Received response: \n");
                        updateDebugLogs("Client ["+clientId+"] Received response: ");
                        updateLogs("[ "+ response.toString() + " ] \n");
                        updateDebugLogs("[ "+ response.toString() + " ] ");
                    }
                }
            } catch (IOException ioe) {
                updateLogs("[!] Connection Failed, check LoadBalancer status.");
                //ioe.printStackTrace();
            } catch (ClassNotFoundException ex) {
                updateLogs("[!] ClassNotFoundException! Client["+clientId+"] \n");
                //ex.printStackTrace();
            }
        }
    }

    public int getLoadBalancerPort() {
        return loadBalancerPort;
    }

    public void setLoadBalancerPort(int loadBalancerPort) {
        this.loadBalancerPort = loadBalancerPort;
    }

    public String getLoadBalancerIP() {
        return loadBalancerIP;
    }

    public void setLoadBalancerIP(String loadBalancerIP) {
        this.loadBalancerIP = loadBalancerIP;
    }

    public int getClientId() {
        return clientId;
    }

    public void setClientId(int clientId) {
        this.clientId = clientId;
    }
    
     private void updateLogs(String s) {
        if (clientGUI != null) {
            clientGUI.updateLogs(s);
        }
    }
    
    private void updateDebugLogs(String s) {
        if (TEST) {
            System.out.println(s);
        }
    }
    
     
}

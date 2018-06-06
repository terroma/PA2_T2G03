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

public class Client {

    private InetAddress connectedAddress;
    private Socket tcpSocket;
    private int loadBalancerPort;
    private String loadBalancerIP;
    private ObjectInputStream oInStream;
    private ObjectOutputStream oOutStream;
    
    private int clientId;
    private UUID uniqueClientId = UUID.randomUUID();
    
    private ClientGUI clientGUI;
    
    /* default client constructor */
    public Client() throws IOException {
        this.clientId = uniqueClientId.hashCode();
        initClient("127.0.0.1",3000);
    }
    
    public Client(String loadBalancerIP, int loadNBalancerPort) {
        this.clientId = uniqueClientId.hashCode();
        this.loadBalancerIP = loadBalancerIP;
        this.loadBalancerPort = loadNBalancerPort;
        initClient(loadBalancerIP, loadNBalancerPort);
    }
    
    public Client(ClientGUI clienteGUI){
        this.clientGUI = clientGUI;
        System.out.println(this.clientGUI);
    }
    
    public void init() {
        initClient(loadBalancerIP, loadBalancerPort);
    }
    
    private void initClient(String host, int port) {
        try {
            System.out.println("[*] Starting Client["+clientId+"] ...");
            this.connectedAddress = Inet4Address.getByName(host);
            this.loadBalancerPort = port;
            this.tcpSocket = new Socket(host, port);
            
            System.out.println("[*] Client["+clientId+"] Connected on port:"+loadBalancerPort);
            
            this.oOutStream = new ObjectOutputStream(tcpSocket.getOutputStream());

            (new Thread(new InputListeningThread())).start();

        } catch (SocketException se) {
            System.out.println("[!] SocketException! Client["+clientId+"]");
            se.printStackTrace();
        } catch (UnknownHostException uhe) {
            System.out.println("[!] UnknownHostException! Client["+clientId+"]");
            uhe.printStackTrace();
        } catch (IOException ioe) {
            System.out.println("[!] IOException! Client["+clientId+"]");
            ioe.printStackTrace();
        }

    }
    
    private void openSocket(String ip, int port) {
        try {
            this.tcpSocket = new Socket(ip, port);
        } catch (Exception e) {
            System.out.println("Error openning socket"+e.getMessage());
        }
    }
    
    public void sendMessage(PiRequest request) throws IOException {
        synchronized(this) {
            if(!this.tcpSocket.isConnected())
                return;
            try {
                System.out.println("[*] Client["+clientId+"] Sending request...");
                System.out.println("[*] Request: "+request.toString());
                this.oOutStream.writeObject(request);
                this.oOutStream.flush();
            } catch (IOException ioe) {
                System.out.println("[!] IOException! Client["+clientId+"]");
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
            System.out.println("[!] IOException :"+ex.getMessage());
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
                        System.out.println("[*] Client["+clientId+"] Received response...");
                        System.out.println("[*] Response: "+response.toString());
                    }
                }
            } catch (IOException ioe) {
                System.out.println("[!] IOException! Client["
                        +clientId+"]");
                ioe.printStackTrace();
            } catch (ClassNotFoundException ex) {
                System.out.println("[!] ClassNotFoundException! Client["
                        +clientId+"]");
                ex.printStackTrace();
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
    
    public static void main(String[] args) throws IOException {
        Client c = new Client();
    }
}

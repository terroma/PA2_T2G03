/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package as.pa2.protocol;

/**
 *
 * @author bruno
 */
public class ConnectionRequest {
    
    private int serverID;
    private String serverIP;
    private int serverPort;

    public ConnectionRequest(int serverID, String serverIP, int serverPort) {
        this.serverID = serverID;
        this.serverIP = serverIP;
        this.serverPort = serverPort;
    }

    public int getServerID() {
        return serverID;
    }

    public void setServerID(int serverID) {
        this.serverID = serverID;
    }

    public String getServerIP() {
        return serverIP;
    }

    public void setServerIP(String serverIP) {
        this.serverIP = serverIP;
    }

    public int getServerPort() {
        return serverPort;
    }

    public void setServerPort(int serverPort) {
        this.serverPort = serverPort;
    }

    @Override
    public String toString() {
        return "ConnectionRequest{" + "serverID=" + serverID + ", serverIP=" + serverIP + ", serverPort=" + serverPort + '}';
    }
    
}

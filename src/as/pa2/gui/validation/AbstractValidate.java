/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package as.pa2.gui.validation;

import java.io.IOException;
import java.net.Socket;
import java.util.regex.Pattern;

/**
 *
 * @author Bruno Assunção 89010
 * @author Hugo Chaves  90842
 * 
 */
public class AbstractValidate implements IFValidate{

    
    private static final Pattern PATTERN = Pattern.compile(
        "^(([01]?\\d\\d?|2[0-4]\\d|25[0-5])\\.){3}([01]?\\d\\d?|2[0-4]\\d|25[0-5])$");
   
     
    @Override
    public boolean validateIP(final String ip) {
        return PATTERN.matcher(ip).matches();
    }

    @Override
    public boolean validatePort(final String port) {
        int portInt;
        try{
            portInt = Integer.parseInt(port);
        }catch(Exception e){
            System.out.println("Error converting String to Int");
            return false;
        }
        if(portInt>=65535 || portInt<=1024){
            return false;
        }
        return true;
    }

    @Override
    public boolean validateQueueSize(String queueSize) {
        int queueSizeInt;
        try{
            queueSizeInt = Integer.parseInt(queueSize);
        }catch(Exception e){
            return false;
        }
        if(queueSizeInt<1){
            return false;
        }
        return true;
    }

    @Override
    public boolean validateMLBfields(String monitorIP, String loadBalancerIP, String clientPort, String serverPort) {
        if(monitorIP.equals(loadBalancerIP)){
            if(clientPort.equals(serverPort)){
                return false;
            }
        }
        return true;
    }

    @Override
    public boolean validateUsedIpPort(String ip, int port) {
        Socket sock = null;
        try {
            sock = new Socket(ip, port);
        } catch (IOException ex) {
            return true;
        }
        return false;
    }
    
    
    
}

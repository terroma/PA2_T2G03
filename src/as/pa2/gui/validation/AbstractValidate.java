/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package as.pa2.gui.validation;

import java.util.regex.Pattern;

/**
 *
 * @author bruno
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
    
    
    
}

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package as.pa2.protocol;

import java.io.Serializable;

/**
 *
 * @author Bruno Assunção 89010
 * @author Hugo Chaves  90842
 * 
 */

public class PiResponse extends PiRequest implements Serializable{
    
    private double pi;
    
    public PiResponse(int clientId, int requestId, int code,
            long precision, int delay, double pi) {
        super(clientId, requestId, code, precision, delay);
        this.pi = pi;
    }   

    public double getPi() {
        return pi;
    }

    public void setPi(double pi) {
        this.pi = pi;
    }
    
    @Override
    public String toString() {
        if (this.getCode() == 3) {
            return String.join(" | ",
                Integer.toString(this.getClientId()),
                Integer.toString(this.getRequestId()),
                String.format("%02d", this.getCode()),
                Long.toString(this.getPrecision()),
                Integer.toString(this.getDelay()));
        }
        return String.join(" | ",
                Integer.toString(this.getClientId()),
                Integer.toString(this.getRequestId()),
                String.format("%02d", this.getCode()),
                Long.toString(this.getPrecision()),
                Integer.toString(this.getDelay()),
                Double.toString(this.getPi()));
    }
}

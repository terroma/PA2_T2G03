/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package as.pa2.protocol;

import java.io.Serializable;

/**
 *
 * @author terroma
 */
public class PiRequest implements Serializable {
    
    private int clientId;
    private int requestId;
    private int code;
    private long precision;
    private int delay;
    
    public PiRequest(int clientId, int requestId, int code, long precision, int delay) {
        this.clientId = clientId;
        this.requestId = requestId;
        this.code = code;
        this.precision = precision;
        this.delay = delay;
    }

    public int getClientId() {
        return clientId;
    }

    public void setClientId(int clientId) {
        this.clientId = clientId;
    }

    public int getRequestId() {
        return requestId;
    }

    public void setRequestId(int requestId) {
        this.requestId = requestId;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public long getPrecision() {
        return precision;
    }

    public void setPrecision(long precision) {
        this.precision = precision;
    }

    public int getDelay() {
        return delay;
    }

    public void setDelay(int delay) {
        this.delay = delay;
    }
    
    @Override
    public String toString() {
        return String.join(" | ", 
                Integer.toString(this.getClientId()),
                Integer.toString(this.getRequestId()),
                String.format("%02d", this.getCode()),
                Long.toString(this.getPrecision()),
                Integer.toString(this.getDelay()));
    }
}

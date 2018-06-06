/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package as.pa2.monitor.availability;

import as.pa2.monitor.IFMonitor;

/**
 * Class that provides the basic implementation of determining the 
 * suitability of a Server (node).
 *
 * @author Bruno Assunção 89010
 * @author Hugo Chaves  90842
 * 
 */

public abstract class AbstractMonitorHeartBeat implements IFHeartBeat {
    
    private IFMonitor monitor;
    
    @Override
    public void setMonitor(IFMonitor monitor) {
        this.monitor = monitor;
    }
    
    @Override
    public IFMonitor getMonitor() {
        return monitor;
    }
}

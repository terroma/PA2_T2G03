/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package as.pa2.server;

import java.io.Serializable;
import java.util.Comparator;

/**
 *
 * @author Bruno Assunção 89010
 * @author Hugo Chaves  90842
 * 
 */

public class ServerComparator implements Comparator<Server>, Serializable {

    private static final long serialVersionUID = 1L;
    
    @Override
    public int compare(Server t, Server t1) {
        return t.getHostPort().compareTo(t1.getId());
    }  
}

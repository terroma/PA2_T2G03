/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package as.pa2.server;

import as.pa2.protocol.PiRequest;
import as.pa2.protocol.PiResponse;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author terroma
 */
public class RequestHandler implements Runnable, Serializable {

    protected Socket clientSocket;
    protected int serverId;
    
    public RequestHandler(Socket clientSocket, int serverId) {
        this.clientSocket = clientSocket;
        this.serverId = serverId;
    }
    
    @Override
    public void run() {
        try {
            ObjectInputStream oInStream = 
                    new ObjectInputStream(clientSocket.getInputStream());
            ObjectOutputStream oOutStream =
                    new ObjectOutputStream(clientSocket.getOutputStream());
            
            long time = System.currentTimeMillis();
            while( true ) {
                System.out.println("Está à espera");
                PiRequest request = (PiRequest) oInStream.readObject();
                
                if(request!=null){
                    System.out.println("Recebeu um request");
                    Double pi = new Pi().compute(request.getPrecision(), request.getDelay());
                    System.out.println("calculou o pi");
                    PiResponse response = new PiResponse(request.getClientId(), request.getRequestId(), 2, request.getPrecision(), request.getDelay(), pi);
                    System.out.println("Tenta enviar");
                    oOutStream.writeObject(response);
                    System.out.println("Enviou");
                }
            }
            
        } catch (IOException ioe) {
            ioe.printStackTrace();
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(RequestHandler.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
}

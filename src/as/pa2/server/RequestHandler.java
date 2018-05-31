/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package as.pa2.server;

import as.pa2.protocol.PiRequest;
import as.pa2.protocol.PiResponse;
import java.io.EOFException;
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
public class RequestHandler implements Runnable {

    protected Socket clientSocket;
    protected int serverId;
    protected boolean isStopped;
    protected ObjectInputStream oInStream;
    protected ObjectOutputStream oOutStream;
    
    public RequestHandler(Socket clientSocket, int serverId) {
        this.clientSocket = clientSocket;
        this.serverId = serverId;
        this.isStopped = false;
    }
    
    @Override
    public void run() {
        try {
            this.oInStream = 
                    new ObjectInputStream(clientSocket.getInputStream());
            this.oOutStream =
                    new ObjectOutputStream(clientSocket.getOutputStream());
            
            long time = System.currentTimeMillis();
            while( !isStopped() ) {
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
                    oOutStream.flush();
                }
            }
            
        } catch (IOException ioe) {
            System.out.println("Closing client connection ");
            this.isStopped = true;
            try {
                clientSocket.close();
            } catch (IOException ex) {
                ex.getMessage();
            }
        } catch (ClassNotFoundException ex) {
            ex.getMessage();
        }
    }
    
    private synchronized boolean isStopped() {
        return this.isStopped;
    }
    
    public synchronized void stop() {
        this.isStopped = true;
        try {
            oInStream.close();
            oOutStream.close();
            clientSocket.close();
        } catch (IOException ex) {
            ex.getMessage();
        }
    }
}

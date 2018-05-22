/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package as.pa2.client;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;

/**
 *
 * @author bruno
 */
public class Client {

    private Socket sk;
    private BufferedReader sin;
    private PrintStream sout;
    private BufferedReader stdin;
    

    public void startClient() throws IOException{
        System.out.println("ola");
        sk=new Socket("127.0.0.1",5000);
        sin=new BufferedReader(new InputStreamReader(sk.getInputStream()));
        sout=new PrintStream(sk.getOutputStream());
        stdin=new BufferedReader(new InputStreamReader(System.in));
        String s;
        
        while ( true){
            System.out.print("Client : ");
            s=stdin.readLine();
            sout.println(s);

            s=sin.readLine();
            System.out.print("Server : "+s+"\n");    
        }
    }
    
    public void endClient() throws IOException{
        sk.close();
        sin.close();
        sout.close();
        stdin.close();
    }
       
}

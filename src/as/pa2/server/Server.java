/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package as.pa2.server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Scanner;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author bruno
 */
public class Server {

    public int port;
    public ServerSocket server=null;
    public Socket client=null;
    public ExecutorService pool = null;
    public int clientcount=0;
    public BufferedReader cin;
    public Socket cliente;
    public PrintStream cout;
    /*
    public static void main(String[] args) throws IOException {
        Server serverobj=new Server(5000);
        serverobj.startServer();
    }*/
    
    public Server(int port){
        this.port=port;
        pool = Executors.newFixedThreadPool(5);
    }

    public void startServer(String monitorIP, String monitorPort, String LoadBPort, String queueSize) throws IOException {
        
        
        
        
        server=new ServerSocket(5000);
        System.out.println("Server Started");
        while(true){
            client=server.accept();
            clientcount++;
            ServerThread runnable= new ServerThread(client,clientcount,this);
            pool.execute(runnable);
        }
        
    }

    public static class ServerThread implements Runnable {
        
        Server server=null;
        Socket client=null;
        BufferedReader cin;
        PrintStream cout;
        Scanner sc=new Scanner(System.in);
        int id;
        String s;
        
        ServerThread(Socket client, int count ,Server server ) throws IOException {
            
            this.client=client;
            this.server=server;
            this.id=count;
            System.out.println("Connection "+id+"established with client "+client);
            
            cin=new BufferedReader(new InputStreamReader(client.getInputStream()));
            cout=new PrintStream(client.getOutputStream());
        
        }

        @Override
        public void run() {
           int x=1;
        try{
        while(true){
            s=cin.readLine();

                    System.out.print("Client("+id+") :"+s+"\n");
                    System.out.print("Server : ");
                    //s=stdin.readLine();
                        s=sc.nextLine();
                    if (s.equalsIgnoreCase("bye"))
                    {
                        cout.println("BYE");
                        x=0;
                        System.out.println("Connection ended by server");
                        break;
                    }
                    cout.println(s);
            }
            cin.close();
            client.close();
            cout.close();
            if(x==0) {
                System.out.println( "Server cleaning up." );
                System.exit(1);
            }
        } 
        catch(IOException ex){
            System.out.println("Error : "+ex);
        }
            
 		
        }
    }
    
    public void disconnect() throws IOException{
        System.out.println("Connection ended by server");
        cin.close();
        client.close();
        cout.close();
            
    }
    
}

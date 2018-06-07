/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package useCases;

import as.pa2.client.Client;
import as.pa2.loadbalancer.LoadBalancer;
import as.pa2.protocol.PiRequest;
import as.pa2.server.Server;
import java.io.IOException;
import static java.lang.Thread.sleep;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;


/**
 *
 * @author Bruno Assunção 89010
 * @author Hugo Chaves  90842
 * 
 */

public class BasicFunctioningJUnitTest {
    
    public BasicFunctioningJUnitTest() {
    }
    
    @BeforeClass
    public static void setUpClass() {
    }
    
    @AfterClass
    public static void tearDownClass() {
    }
    
    @Before
    public void setUp() {
    }
    
    @After
    public void tearDown() {
    }

    // TODO add test methods here.
    // The methods must be annotated with annotation @Test. For example:
    //
    // @Test
    // public void hello() {}
    @Test
    public void basicFunctioning() throws IOException, InterruptedException {
        Server server1 = new Server("127.0.0.1", 5000, "127.0.0.2", 5000, 20);
        Server server2 = new Server("127.0.0.3", 5000, "127.0.0.2", 5000, 20);
        Server server3 = new Server("127.0.0.4", 5000, "127.0.0.2", 5000, 20);
        
        LoadBalancer lb = new LoadBalancer("127.0.0.5", 5000, "127.0.0.2", 5000);
        
        ExecutorService pool = Executors.newFixedThreadPool(4);
        pool.execute(lb);
        sleep(1000);
        pool.execute(server1);
        pool.execute(server2);
        pool.execute(server3);
        
        sleep(1000);
        
        Client client1 = new Client("127.0.0.5", 5000);
        Client client2 = new Client("127.0.0.5", 5000);
        
        PiRequest r1 = new PiRequest(client1.getClientId(), 1, 01, 7, 7);
        PiRequest r2 = new PiRequest(client2.getClientId(), 1, 01, 9, 9);
        PiRequest r3 = new PiRequest(client1.getClientId(), 2, 01, 14, 10);
        PiRequest r4 = new PiRequest(client2.getClientId(), 2, 01, 17, 12);
        PiRequest r5 = new PiRequest(client1.getClientId(), 3, 01, 3, 15);
        PiRequest r6 = new PiRequest(client2.getClientId(), 3, 01, 20, 17);
        sleep(1000);
        client1.sendMessage(r1);
        client2.sendMessage(r2);
        client1.sendMessage(r3);
        client2.sendMessage(r4);
        client1.sendMessage(r5);
        client2.sendMessage(r6);
        
        sleep(20000);
        
        pool.shutdown();
        System.out.println("Pool Shutdown");
    }
    
}

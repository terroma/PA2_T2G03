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
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author terroma
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
        
        (new Thread(lb)).start();
        
        (new Thread(server1)).start();
        (new Thread(server2)).start();
        (new Thread(server3)).start();
        
        Client client1 = new Client("127.0.0.5", 5000);
        Client client2 = new Client("127.0.0.5", 5000);
        
        PiRequest r1 = new PiRequest(1, 1, 01, 7, 7);
        PiRequest r2 = new PiRequest(1, 2, 01, 9, 9);
        PiRequest r3 = new PiRequest(1, 3, 01, 14, 10);
        PiRequest r4 = new PiRequest(2, 1, 01, 17, 12);
        PiRequest r5 = new PiRequest(2, 2, 01, 3, 15);
        PiRequest r6 = new PiRequest(2, 3, 01, 20, 17);
        
        
        
        
        
        client1.sendMessage(r1);
        client1.sendMessage(r2);
        client1.sendMessage(r3);
        client2.sendMessage(r4);
        client2.sendMessage(r5);
        client2.sendMessage(r6);
        
        sleep(10000);
    }
    
}

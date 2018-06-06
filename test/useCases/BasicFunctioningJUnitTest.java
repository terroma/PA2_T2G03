/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package useCases;

import as.pa2.client.Client;
import as.pa2.loadbalancer.LoadBalancer;
import as.pa2.server.Server;
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
    public void basicFunctioning() {
        Server server1 = new Server("127.0.0.1", 5000, "127.0.0.2", 5000, 5000, 20);
        Server server2 = new Server("127.0.0.3", 5000, "127.0.0.2", 5000, 5000, 20);
        Server server3 = new Server("127.0.0.4", 5000, "127.0.0.2", 5000, 5000, 20);
        
        LoadBalancer lb = new LoadBalancer("127.0.0.5", 5000, "127.0.0.2", 5000);
        
        Client client1 = new Client("127.0.0.5", 5000);
        Client client2 = new Client("127.0.0.5", 5000);
        Client client3 = new Client("127.0.0.5", 5000);
    }
    
}

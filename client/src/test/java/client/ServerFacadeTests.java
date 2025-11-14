package client;

import org.junit.jupiter.api.*;
import server.Server;
//how to import server facade
//what t\]o put in that one server facade test
//test and debug
//javalin of server facade end
//is it ok that these are in client and not shared? what about client?


public class ServerFacadeTests {

    private static Server server;

    @BeforeAll
    public static void init() {
        server = new Server();
        var port = server.run(0);
        System.out.println("Started test HTTP server on " + port);
        ServerFacade serverFacade = new ServerFacade();
    }

    @AfterAll
    static void stopServer() {
        server.stop();
    }

//be visible
    //
    @Test
    public void sampleTest() {
        Assertions.assertTrue(true);
    }

    @Test
    public void addUserTest() {
        
    }

}

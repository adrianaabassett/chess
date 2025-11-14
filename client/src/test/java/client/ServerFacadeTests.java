package client;

import dataaccess.exceptions.ResponseException;
import model.AuthData;
import model.UserData;
import org.junit.jupiter.api.*;
import server.Server;

import static org.junit.jupiter.api.Assertions.*;
//how to import server facade
//what t\]o put in that one server facade test
//test and debug
//javalin of server facade end
//is it ok that these are in client and not shared? what about client?


public class ServerFacadeTests {

    private static Server server;
    ServerFacade serverFacade = new ServerFacade();

    @BeforeAll
    public static void init() {
        server = new Server();
        var port = server.run(8080);
        System.out.println("Started test HTTP server on " + port);
        ServerFacade serverFacade = new ServerFacade();
        /////  UserData userData = new Gson().fromJson(ctx.body(),UserData.class);
    }

    @AfterAll
    static void stopServer() {
        server.stop();
    }

//be visible
    //
    @Test
    public void sampleTest() {
        assertTrue(true);
    }

    @Test
    @DisplayName("login")
    public void addUserTestPositive() throws ResponseException {
        AuthData authData = serverFacade.loginUser(new UserData("usern","pass","em"));
        assertNotNull(authData.authToken());
    }

    @Test
    @DisplayName("already logged in")
    public void addUserTestNegative() throws ResponseException{
        serverFacade.loginUser(new UserData("usern","pass","em"));
        AuthData authData = serverFacade.loginUser(new UserData("usern","pass","em"));
        assertNull(authData.authToken());
    }

    @Test
    @DisplayName("already logged in")
    public void clearPositive() throws ResponseException{
        serverFacade.clear();
        assertDoesNotThrow();
    }


}

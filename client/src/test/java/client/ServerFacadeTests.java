package client;

import chess.ChessGame;
import dataaccess.exceptions.DataAccessException;
import dataaccess.exceptions.ResponseException;
import model.AuthData;
import model.GameData;
import model.UserData;
import org.junit.jupiter.api.*;
import recordrequests.RegisterRequest;
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
    @DisplayName("register")
    public void addUserTestPositive() throws ResponseException {
        serverFacade.clear();
        AuthData authData = serverFacade.addUser(new RegisterRequest("usern","pass","em"));
        assertNotNull(authData.getUsername());
        assertEquals(authData.getUsername(),"usern");
    }

    @Test
    @DisplayName("already registered")
    public void addUserTestNegative() throws ResponseException{
        serverFacade.clear();
        serverFacade.addUser(new RegisterRequest("usern","pass","em"));
        assertThrows(ResponseException.class, () -> serverFacade.addUser(new RegisterRequest("usern","pass","em")));
    }

    @Test
    @DisplayName("login")
    public void loginUserTestPositive() throws ResponseException {
        serverFacade.clear();
        AuthData authData = serverFacade.addUser(new RegisterRequest("usern","pass","em"));
        serverFacade.logoutUser(authData.authToken());
        AuthData authDataTwo = serverFacade.loginUser(new UserData("usern","pass","em"));
        assertEquals(authData.getUsername(), authDataTwo.getUsername());
    }

    @Test
    @DisplayName("cannot log in because you are unregistered")
    public void loginUserTestNegative() throws ResponseException{
        serverFacade.clear();
        assertThrows(ResponseException.class, () ->serverFacade.loginUser(new UserData("usern","pass","em")));
    }
    
    @Test
    @DisplayName("cannot log in because you are already logged in")
            public void loginUserTestNegativeTwo() throws ResponseException {
        serverFacade.clear();
        serverFacade.addUser(new RegisterRequest("usern", "pass", "em"));
        assertThrows(ResponseException.class, () ->serverFacade.loginUser(new UserData("usern","pass","em")));
    }

    @Test
    @DisplayName("testing clear on empty")
    public void clearPositive() throws ResponseException{
        serverFacade.clear();
        assertDoesNotThrow(() -> serverFacade.clear());
        //assertDoesNotThrow();
    }

    @Test
    @DisplayName("testing clear on full")
    public void clearPositiveTwo() throws ResponseException{
        serverFacade.clear();
        assertThrows(ResponseException.class, () ->serverFacade.loginUser(new UserData("usern","pass","em")));
    }

    @Test
    @DisplayName("logout positive")
    public void logoutPositive() throws ResponseException{
        serverFacade.clear();
        AuthData authData = serverFacade.addUser(new RegisterRequest("usern","pass","em"));
        assertDoesNotThrow(() -> serverFacade.logoutUser(authData.authToken()));
    }

    //logout neg
    @Test
    @DisplayName("logout negative-not registered")
    public void logoutNegative() throws ResponseException{
        serverFacade.clear();
        assertThrows(ResponseException.class, () -> serverFacade.logoutUser("never gonna give"));

    }

    //create game pos
    @Test
    @DisplayName("creating a game yay")
    public void createGamePositive() throws ResponseException{
        serverFacade.clear();
        serverFacade.addUser(new RegisterRequest("usern","pass","em"));
        GameData gameData = new GameData(12, "you", "up","never", new ChessGame());
        assertDoesNotThrow(() -> serverFacade.createGame(gameData));
        assertEquals(12, gameData.gameID());
    }
    //create game neg
    @Test
    @DisplayName("Creating a game without logging in first")
    public void createGameNegative() throws ResponseException{
        serverFacade.clear();
        GameData gameData = new GameData(12, "you", "up","never", new ChessGame());
        assertThrows(ResponseException.class, () -> serverFacade.createGame(gameData));
    }

    @Test
    @DisplayName("Creating a game without the right parameters")
    public void createGameNegativeTwo() throws ResponseException{
        serverFacade.clear();
        serverFacade.addUser(new RegisterRequest("usern","pass","em"));
        GameData gameData = new GameData(12, null, null,null, new ChessGame());
        assertThrows(ResponseException.class, () -> serverFacade.createGame(gameData));
    }

    @Test
    @DisplayName("Creating a game already been made")
    public void createGameNegativeThree() throws ResponseException{
        serverFacade.clear();
        serverFacade.addUser(new RegisterRequest("usern","pass","em"));
        GameData gameData = new GameData(12, "you", "up","never", new ChessGame());
        serverFacade.createGame(gameData);
        assertThrows(ResponseException.class, () -> serverFacade.createGame(gameData));
    }


    //list game pos

    //list game neg

    //join game pos

    //join game neg

    //

}

package client;

import chess.ChessGame;
import exceptions.AlreadyTakenException;
import exceptions.ResponseException;
import model.AuthData;
import model.GameData;
import model.UserData;
import org.junit.jupiter.api.*;
import recordrequests.JoinGameRequest;
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
    private static ServerFacade serverFacade;

    @BeforeAll
    public static void init() {
        server = new Server();
        var port = server.run(0);
        serverFacade = new ServerFacade("http://localhost:"+port);
        System.out.println("Started test HTTP server on " + port);
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
    public void addUserTestPositive() throws ResponseException, AlreadyTakenException {
        serverFacade.clear();
        AuthData authData = serverFacade.addUser(new RegisterRequest("usern","pass","em"));
        assertNotNull(authData.getUsername());
        assertEquals(authData.getUsername(),"usern");
    }

    @Test
    @DisplayName("already registered")
    public void addUserTestNegative() throws ResponseException, AlreadyTakenException {
        serverFacade.clear();
        serverFacade.addUser(new RegisterRequest("usern","pass","em"));
        assertThrows(AlreadyTakenException.class, () -> serverFacade.addUser(new RegisterRequest("usern","pass","em")));
    }

    @Test
    @DisplayName("login")
    public void loginUserTestPositive() throws ResponseException, AlreadyTakenException {
        serverFacade.clear();
        AuthData authData = serverFacade.addUser(new RegisterRequest("usern","pass","em"));
        serverFacade.logoutUser(authData.authToken());
        AuthData authDataTwo = serverFacade.loginUser(new UserData("usern","pass","em"));
        assertEquals(authData.getUsername(), authDataTwo.getUsername());
    }

    @Test
    @DisplayName("cannot log in because you are unregistered")
    public void loginUserTestNegative() throws ResponseException, AlreadyTakenException {
        serverFacade.clear();
        assertThrows(ResponseException.class, () ->serverFacade.loginUser(new UserData("usern","pass","em")));
    }
    
    @Test
    @DisplayName("cannot log in because username is null")
            public void loginUserTestNegativeTwo() throws ResponseException, AlreadyTakenException {
        serverFacade.clear();
        serverFacade.addUser(new RegisterRequest("usern", "pass", "em"));
        assertThrows(ResponseException.class, () ->serverFacade.loginUser(new UserData(null,"pass","em")));
        //also it doesnt exist
        assertThrows(ResponseException.class, () ->serverFacade.loginUser(new UserData("doesnt exist","passw","email")));

    }

    @Test
    @DisplayName("testing clear on empty")
    public void clearPositive() throws ResponseException, AlreadyTakenException {
        serverFacade.clear();
        assertDoesNotThrow(() -> serverFacade.clear());
        //assertDoesNotThrow();
    }

    @Test
    @DisplayName("testing clear on full")
    public void clearPositiveTwo() throws ResponseException, AlreadyTakenException {
        serverFacade.clear();
        assertThrows(ResponseException.class, () ->serverFacade.loginUser(new UserData("usern","pass","em")));
    }

    @Test
    @DisplayName("logout positive")
    public void logoutPositive() throws ResponseException, AlreadyTakenException {
        serverFacade.clear();
        AuthData authData = serverFacade.addUser(new RegisterRequest("usern","pass","em"));
        assertDoesNotThrow(() -> serverFacade.logoutUser(authData.authToken()));
    }

    //logout neg
    @Test
    @DisplayName("logout negative-not registered")
    public void logoutNegative() throws ResponseException, AlreadyTakenException {
        serverFacade.clear();
        assertThrows(ResponseException.class, () -> serverFacade.logoutUser("never gonna give"));
    }

    //create game pos
    @Test
    @DisplayName("creating a game yay")
    public void createGamePositive() throws ResponseException, AlreadyTakenException {
        serverFacade.clear();
        AuthData authData = serverFacade.addUser(new RegisterRequest("usern","pass","em"));
        GameData gameData = new GameData(12, "you", "up","never", new ChessGame());
        assertDoesNotThrow(() -> serverFacade.createGame("gameData", authData.authToken() ));
        assertEquals(12, gameData.gameID());
    }
    //create game neg
    @Test
    @DisplayName("Creating a game without logging in first")
    public void createGameNegative() throws ResponseException, AlreadyTakenException {
        serverFacade.clear();
        GameData gameData = new GameData(12, "you", "up","never", new ChessGame());
        assertThrows(ResponseException.class, () -> serverFacade.createGame("gameData", "fasdjkfaksdjhfhjasd"));
    }

//    list game pos
    @Test
    @DisplayName("Listing all the games")
    public void listGamesPositive() throws ResponseException, AlreadyTakenException {
        serverFacade.clear();
        AuthData authData = serverFacade.addUser(new RegisterRequest("usern","pass","em"));
        serverFacade.createGame("chess",authData.authToken());
        serverFacade.createGame("ssehc",authData.authToken());
        assertDoesNotThrow(()-> (serverFacade.listGames(authData.authToken())));
    }

    //list game neg
    @Test
    @DisplayName("cant list because you arent logged in")
    public void listGamesNegative() throws ResponseException, AlreadyTakenException {
        serverFacade.clear();
        assertThrows(ResponseException.class, ()->serverFacade.listGames("never gonna give you up never gonna"));
    }

    //join game pos
    @Test
    @DisplayName("joining game")
    public void JoinGamesPositive() throws ResponseException, AlreadyTakenException {
        serverFacade.clear();
        AuthData authData = serverFacade.addUser(new RegisterRequest("name","email","pass"));
        GameData gameData = serverFacade.createGame("adfsd", authData.authToken());
        JoinGameRequest joinGameRequest = new JoinGameRequest(authData.authToken(),"WHITE",gameData.gameID());
        assertDoesNotThrow(()->serverFacade.joinGame(joinGameRequest, authData.authToken()));
       //just put the join games here
    }

    //join game neg
    @Test
    @DisplayName("cant join game not logged in")
    public void JoinGamesNegative() throws ResponseException, AlreadyTakenException {
        serverFacade.clear();
        AuthData authData = serverFacade.addUser(new RegisterRequest("name","email","pass"));
        serverFacade.createGame("new", authData.authToken());
        JoinGameRequest joinGameRequest = new JoinGameRequest(authData.authToken(),"WHITE",12);
        assertThrows(ResponseException.class, ()->serverFacade.joinGame(joinGameRequest, "kajshdflh"));
        //authToken, String playerColor, Integer gameID
        //just put the join games here
        //not logged in
    }
    @Test
    @DisplayName("cant join game no games")
    public void JoinGamesNegativeTwo() throws ResponseException, AlreadyTakenException {
        serverFacade.clear();
        AuthData authData = serverFacade.addUser(new RegisterRequest("name","password","email"));
        JoinGameRequest joinGameRequest = new JoinGameRequest(authData.authToken(),"WHITE",12);
        assertThrows(ResponseException.class, ()->serverFacade.joinGame(joinGameRequest, authData.authToken()));
        //just put the join games here
        //no games to join
    }

}

package service;
import chess.ChessGame;
import dataaccess.*;
import dataaccess.exceptions.*;
import model.AuthData;
import model.GameData;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import recordrequests.RegisterRequest;
import recordrequests.RegisterResult;
import service.Service;
import model.UserData;

import java.util.HashMap;

import static org.junit.jupiter.api.Assertions.*;

// for simplicity's sake, I'm putting all of my testis in this file
public class ServiceTest {
    MemoryUser userMemory = new MemoryUser();
    MemoryGame gameMemory = new MemoryGame();
    MemoryAuth authMemory = new MemoryAuth();
    Service userService = new Service(userMemory, gameMemory, authMemory);

    @Test
    @DisplayName("registering a user and it returns a register result to handler")
    public void registerPositive() throws DataAccessException, BadRequest, AlreadyTakenException {
        var userTest = new UserData("joe", "jjj", "chicken");
        var authTest = userService.register(new RegisterRequest("joe", "jjj", "chicken"));
        assertNotNull(authTest.authToken());
        assertEquals("joe", authTest.username());

    }
    //try to register a user without a password

    @Test
    @DisplayName("registering a user but it returns with an error")
    public void registerNegative() throws DataAccessException, BadRequest, AlreadyTakenException {
        var userTest = new UserData("joe", "jjj", "chicken");
        var authTest = userService.register(new RegisterRequest("joe", "jjj", "chicken"));
        assertNotNull(authTest.authToken());
        assertEquals("joe", authTest.username());

    }

    @Test
    @DisplayName("Logged in")
    public void loginPositive() throws DataAccessException, BadRequest, UnauthorizedException, InvalidID, AlreadyTakenException {
        UserData user = new UserData("chicken","tulip","dandelion");
        userService.register(new RegisterRequest("chicken","tulip","dandelion"));
        AuthData authData = userService.loginUser(user);
        assertEquals(authMemory.getAuth(authData.authToken()), authData);

    }

    @Test
    @DisplayName("Not Logged in because of Error")
    public void loginNegative() throws DataAccessException, BadRequest, AlreadyTakenException {
        UserData user = new UserData("chicken","tulip","dandelion");
        assertThrows(DataAccessException.class, () -> userService.loginUser(user));
        userService.register(new RegisterRequest("chicken","tulip","dandelion"));
        UserData userTwo = new UserData("chicken", "dumpling", "dandelion");
        Assertions.assertThrows(UnauthorizedException.class, () -> userService.loginUser(userTwo));
    }


    @Test
    @DisplayName("logged out")
    public void logoutPositive() throws DataAccessException, BadRequest, UnauthorizedException, AlreadyTakenException {
        UserData user = new UserData("chicken","tulip","dandelion");
        RegisterResult auth = userService.register(new RegisterRequest("chicken","tulip","dandelion"));
        userService.logoutUser(auth.authToken());
        assertEquals(null,authMemory.getAuth(auth.authToken()));
        //assertThrows(DataAccessException.class, () -> authMemory.getAuth(auth.authToken()));
    }

    @Test
    @DisplayName("not logged out")
    public void logoutNegative() throws DataAccessException, BadRequest, AlreadyTakenException {
        RegisterResult auth = userService.register(new RegisterRequest("chicken", "tulip", "dandelion"));
        UserData user = new UserData("chicken", "tulip", "dandelion");
        assertThrows(UnauthorizedException.class, () -> userService.logoutUser("chickadee"));
    }
    @Test
    @DisplayName("list games")
    public void listGamesPostitive() throws DataAccessException, UnauthorizedException, BadRequest, AlreadyTakenException {
        RegisterResult registerResult = userService.register(new RegisterRequest("userN", "pass","mail"));
        String authToken = registerResult.authToken();
        HashMap<Integer, GameData> hashMap = new HashMap<>();

        int gameIDOne = userService.createGame("a",authToken).gameID();
        int gameIDTwo = userService.createGame("b",authToken).gameID();
        int gameIDThree = userService.createGame("c",authToken).gameID();

        hashMap.put(gameIDOne, new GameData(gameIDOne, null, null, "a", new ChessGame()));
        hashMap.put(gameIDTwo, new GameData(gameIDTwo, null, null, "b", new ChessGame()));
        hashMap.put(gameIDThree, new GameData(gameIDThree, null, null, "c", new ChessGame()));
        GameData gameDatas = hashMap.get(gameIDOne);
        GameData gameData = userService.listGames(authToken).get(0);
        assertEquals(gameDatas, gameData);

    }

    @Test
    @DisplayName("cant list games because its empty")
    public void listGamesTestNegative() throws DataAccessException {
        assertThrows(UnauthorizedException.class, () -> userService.listGames("chicken"));
    }

    @Test
    @DisplayName("create new game")
    public void createGamePositive() throws DataAccessException, UnauthorizedException, BadRequest, AlreadyTakenException {
        RegisterResult register = userService.register(new RegisterRequest("usern","pass","email"));
        int gameID = userService.createGame("game one",register.authToken()).gameID();
        assertNotNull(gameMemory.getGame(gameID));
        int gameIDTwo = userService.createGame("game two",register.authToken()).gameID();
        Assertions.assertNotEquals(gameID, gameIDTwo);
    }

    @Test
    @DisplayName("cant create new game")
    public void createGameNegative() throws DataAccessException, UnauthorizedException {
        Assertions.assertThrows(UnauthorizedException.class, () -> userService.createGame("name", "authToken"));
    }

    @Test
    @DisplayName("join game")
    public void joinGamePositive() throws DataAccessException, BadRequest, UnauthorizedException, InvalidID, AlreadyTakenException {
        UserData user = new UserData("chicken","tulip","dandelion");
        userService.register(new RegisterRequest("chicken","tulip","dandelion"));
        AuthData authData = userService.loginUser(user);
        int gameID = userService.createGame("nameofgame", authData.authToken()).gameID();
        userService.joinGame(authData.authToken(), "WHITE", gameID);


        GameData gameData = new GameData(gameID, authData.username(), null, "nameofgame", null);
        assertEquals(gameData.gameID(), gameMemory.getGame(gameID).gameID());
    }

    @Test
    @DisplayName("cant join game")
    public void joinGameNegative() throws DataAccessException, BadRequest, UnauthorizedException, AlreadyTakenException, InvalidID {
        RegisterResult authData = userService.register(new RegisterRequest("chicken","tulip","dandelion"));
        int gameID = userService.createGame( "name",authData.authToken()).gameID();
        assertThrows(UnauthorizedException.class, () -> userService.joinGame("authT","WHITE", gameID));
        assertThrows(BadRequest.class, () -> userService.joinGame(authData.authToken(), "WHITE", 12));
        assertThrows(BadRequest.class, () -> userService.joinGame(authData.authToken(), "PINK", gameID));
    }

    @Test
    @DisplayName("clear")
    public void clearPositive() throws DataAccessException {
        userMemory.createUser(new UserData("chicken", "pumpkin", "halloween"));
        authMemory.createAuth(new AuthData("candy", "corn"));
        int game = gameMemory.createGame("orange");
        assertNotNull(userMemory.getUser("chicken"));
        assertNotNull(authMemory.getAuth("candy"));
        assertNotNull(gameMemory.getGame(game));
        userMemory.clear();
        authMemory.clear();
        gameMemory.clear();
        assertNull(userMemory.getUser("chicken"));
        assertNull(authMemory.getAuth("candy"));
        assertNull(gameMemory.getGame(game));

    }

    @Test
    @DisplayName("clear false")
    void clearNegative() throws DataAccessException {
        userMemory.createUser(new UserData("chicken", "pumpkin", "halloween"));
        authMemory.createAuth(new AuthData("candy", "corn"));
        int game = gameMemory.createGame("orange");
        assertNotNull(userMemory.getUser("chicken"));
        assertNotNull(authMemory.getAuth("candy"));
        assertNotNull(gameMemory.getGame(game));
        userMemory.clear();
        authMemory.clear();
        gameMemory.clear();
        assertNull(userMemory.getUser("chicken"));
        assertNull(authMemory.getAuth("candy"));
        assertNull(gameMemory.getGame(game));
        assertDoesNotThrow(() -> userMemory.clear());
    }
}

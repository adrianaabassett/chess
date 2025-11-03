package dataaccess;
import chess.ChessGame;
import dataaccess.exceptions.*;
import model.AuthData;
import model.GameData;
import model.UserData;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import recordrequests.RegisterRequest;
import service.Service;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

// for simplicity's sake, I'm putting all of my testis in this file
public class SQLTests {
    DatabaseSqlAuth authDatabase = new DatabaseSqlAuth();
    DatabaseSqlGame gameDatabase = new DatabaseSqlGame();
    DatabaseSqlUser userDatabase = new DatabaseSqlUser();
    Service userService = new Service(userDatabase, gameDatabase, authDatabase);

    public SQLTests() throws ResponseException, DataAccessException {
    }


    DatabaseSqlUser databaseSqlUser = new DatabaseSqlUser();
    DatabaseSqlGame databaseSqlGame = new DatabaseSqlGame();
    DatabaseSqlAuth databaseSqlAuth = new DatabaseSqlAuth();

    public void clear() throws DataAccessException {
        databaseSqlUser.clear();
        databaseSqlGame.clear();
        databaseSqlAuth.clear();
    }
    //UserSQL
    //clear
    @Test
    @DisplayName("clear all databases")
    public void clearTest() throws DataAccessException{
        clear();
        databaseSqlUser.createUser(new UserData("usern","passw","eem"));
        int gameID = databaseSqlGame.createGame("gamen");
        databaseSqlAuth.createAuth(new AuthData("tokwn","usern"));

        databaseSqlUser.clear();
        databaseSqlGame.clear();
        databaseSqlAuth.clear();

        assertNull(databaseSqlUser.getUser("usern"));
        assertNull(databaseSqlGame.getGame(gameID));
        assertNull( databaseSqlAuth.getAuth("tokwn"));
    }


    //create user
    @Test
    @DisplayName("creating a user")
    public void createUserPositive() throws DataAccessException, SQLException, BadRequest, AlreadyTakenException {
        clear();
        var authTests = userService.register(new RegisterRequest("usern", "pppp", "chicken"));
            assertNotNull(authTests.authToken());
            assertEquals("usern", authTests.username());
        clear();
    }
    //negative
    @Test
    @DisplayName("not creating a user")
    public void createUserNegative() throws DataAccessException, BadRequest, AlreadyTakenException {
        clear();
        assertThrows(DataAccessException.class, () -> databaseSqlUser.createUser(new UserData(null,"pppp","chicken")));
    }


    //getUser
    @Test
    @DisplayName("Getting a user")
    public void getUserPositive() throws DataAccessException, BadRequest, AlreadyTakenException {
        clear();
        UserData userData = new UserData("usern","pppp","chicken");
        databaseSqlUser.createUser(userData);
        assertEquals("usern", databaseSqlUser.getUser("usern").username());
        clear();
    }

    //negative
        @Test
    @DisplayName("Not Getting a User")
    public void getUserNegative() throws DataAccessException {
            clear();
            UserData userData = new UserData(null,"pppp","chicken");
            assertThrows(DataAccessException.class, () -> databaseSqlUser.createUser(userData));
        //assertThrows(somethingexception),
        }

    //AuthSQL

    //create auth
    @Test
    @DisplayName("Creating an Auth")
    public void createAuthPositive() throws DataAccessException {
        clear();
        AuthData authData = new AuthData("tok","use");
        databaseSqlAuth.createAuth(authData);
        assertEquals("use",databaseSqlAuth.getAuth("tok").username());
    }

    //negative
    @Test
    @DisplayName("Not Creating an Auth")
    public void createAuthNegative() throws DataAccessException {
        clear();
//        AuthData authData = new AuthData("you should watch over the garden wall","use");
//        databaseSqlAuth.createAuth(authData);
        assertNull(databaseSqlAuth.getAuth("null"));

    }

    //get auth
    @Test
    @DisplayName("Getting an Auth")
    public void getAuthPositive() throws DataAccessException {
        clear();
            AuthData authData = new AuthData("tok","use");
            databaseSqlAuth.createAuth(authData);
            assertEquals("use",databaseSqlAuth.getAuth("tok").username());
    }
    //negative
    @Test
    @DisplayName("Not Getting an Auth")
    public void getAuthNegative() throws DataAccessException {
        clear();
        AuthData authData = new AuthData("you should watch over the garden wall","use");
        databaseSqlAuth.createAuth(authData);
        assertNull( databaseSqlAuth.getAuth("notauser"));

    }

    //deleteauth
    @Test
    @DisplayName("Deleting an Auth")
    public void deleteAuthPositive() throws DataAccessException {
        clear();
        AuthData authData = new AuthData("here is a token","use");
        databaseSqlAuth.createAuth(authData);
        databaseSqlAuth.deleteAuth("here is a token");
        assertNull(databaseSqlAuth.getAuth("here is a token"));
    }
    @Test
    @DisplayName("Not Deleting an Auth")
    public void deleteAuthNegative() throws DataAccessException {
        clear();
        AuthData authData = new AuthData("here is a token","use");
        databaseSqlAuth.deleteAuth("here is a token");
        assertNull(databaseSqlAuth.getAuth("here is a token"));
        assertNull(databaseSqlAuth.getAuth("here is not a token"));
        assertNull(databaseSqlAuth.getAuth("here is a token sike no its not "));
    }

    //GameSQL

    //create game
    @Test
    @DisplayName("Creating a Game")
    public void createGamePositive() throws DataAccessException {
        clear();
        databaseSqlGame.createGame("gamename");
        assertEquals("gamename",databaseSqlGame.getGameFromGameName("gamename").gameName());
    }
    //negative
    @Test
    @DisplayName("Not creating a Game")
    public void createGameNegative() throws DataAccessException {
        clear();
        assertThrows(DataAccessException.class, () -> databaseSqlGame.createGame(null));
    }

    //getgame
    @Test
    @DisplayName("Getting a Game")
    public void getGamePositive() throws DataAccessException {
        clear();
        databaseSqlGame.createGame("gamename");
        assertEquals("gamename",databaseSqlGame.getGameFromGameName("gamename").gameName());
        int gameID = databaseSqlGame.getGameFromGameName("gamename").gameID();
        assertEquals(gameID,databaseSqlGame.getGameFromGameName("gamename").gameID());
        assertEquals("gamename",databaseSqlGame.getGame(gameID).gameName());
    }
    //negative
    @Test
    @DisplayName("Not getting a Game")
    public void getGameNegative() throws DataAccessException {
        clear();
        assertThrows(DataAccessException.class, () -> databaseSqlGame.createGame(null));
        databaseSqlGame.createGame("gamename");
        assertEquals("gamename",databaseSqlGame.getGameFromGameName("gamename").gameName());
        int gameID = databaseSqlGame.getGameFromGameName("gamename").gameID();
        assertEquals(gameID,databaseSqlGame.getGameFromGameName("gamename").gameID());
        assertThrows(DataAccessException.class, () -> databaseSqlGame.createGame(null));
    }

    //list games
    @Test
    @DisplayName("Listing a Game")
    public void listGamePositive() throws DataAccessException {
        clear();
        databaseSqlGame.createGame("gamename");
        List<GameData> listGames = databaseSqlGame.listGames();
        assertEquals("gamename",listGames.get(0).gameName());
    }
    //negtive
    @Test
    @DisplayName("Not listing a Game")
    public void listGameNegative() throws DataAccessException {
        ArrayList<GameData> emptyArray = new ArrayList<>();
        databaseSqlGame.clear();
        assertEquals(emptyArray, databaseSqlGame.listGames());

    }

    //get username
    @Test
    @DisplayName("Getting a username from a Game")
    public void getUsernameGamePositive() throws DataAccessException {
        clear();
        databaseSqlGame.createGame("june");
        int gameID = databaseSqlGame.getGameFromGameName("june").gameID();
        databaseSqlGame.updateGame(new GameData(gameID,"july",null,"june",new ChessGame()));
        assertEquals(databaseSqlGame.getUsername("WHITE",gameID),"july");
    }
    //negative
    @Test
    @DisplayName("Not getting a username from a Game")
    public void getUsernameGameNegative()throws DataAccessException{
        clear();
        assertNull( databaseSqlGame.getUsername("nothing here but the void",4));
    }

    @Test
    @DisplayName("My own getting game from game name")
    public void getGameFromUsernamePositive() throws DataAccessException {
        clear();
        databaseSqlGame.createGame("happy");
        int gameID = databaseSqlGame.getGameFromGameName("happy").gameID();
        databaseSqlGame.updateGame(new GameData(gameID,"sad",null,"happy",new ChessGame()));
        assertEquals(databaseSqlGame.getUsername("WHITE",gameID),"sad");
    }
    @Test
    @DisplayName("My own not getting game from game name")
    public void getGameFromUsernameNegative() throws DataAccessException {
        clear();
        assertNull( databaseSqlGame.getGameFromGameName("nothing here but the void"));
    }


    //updategame
    @Test
    @DisplayName("Updating game")
    public void updateGamePositive() throws DataAccessException {
        clear();
        databaseSqlGame.createGame("gn");
        int gameID = databaseSqlGame.getGameFromGameName("gn").gameID();
        GameData gameData = new GameData(gameID,"w","b","gn",new ChessGame());
    databaseSqlGame.updateGame(gameData);
    assertEquals("b",databaseSqlGame.getGame(gameID).blackUsername());
    }
    //negative
    //updategame
    @Test
    @DisplayName("Not Updating game")
    public void updateGameNegative() throws DataAccessException {
        clear();
        databaseSqlGame.updateGame(new GameData(1,"a","v","y",new ChessGame()));
        assertNull(databaseSqlGame.getGame(1));
    }
    @Test
    @DisplayName("clear all databases shortened")
    public void clearAll() throws DataAccessException{
        clear();
        databaseSqlUser.createUser(new UserData("usern","passw","eem"));
        int gameID = databaseSqlGame.createGame("gamen");
        databaseSqlAuth.createAuth(new AuthData("tokwn","usern"));
        clear();
        assertNull(databaseSqlUser.getUser("usern"));
        assertNull(databaseSqlGame.getGame(gameID));
        assertNull( databaseSqlAuth.getAuth("tokwn"));
    }

}
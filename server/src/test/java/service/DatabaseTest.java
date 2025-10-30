package service;

import chess.ChessGame;
import dataaccess.*;
import dataaccess.exceptions.*;
import model.AuthData;
import model.GameData;
import model.UserData;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import recordrequests.RegisterRequest;
import recordrequests.RegisterResult;

import java.sql.SQLException;
import java.util.HashMap;

import static org.junit.jupiter.api.Assertions.*;

// for simplicity's sake, I'm putting all of my testis in this file
public class DatabaseTest {
    DatabaseSqlAuth authDatabase = new DatabaseSqlAuth();
    DatabaseSqlGame gameDatabase = new DatabaseSqlGame();
    DatabaseSqlUser userDatabase = new DatabaseSqlUser();
    Service userService = new Service(userDatabase, gameDatabase, authDatabase);

    public DatabaseTest() throws ResponseException, DataAccessException {
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
        databaseSqlUser.createUser(new UserData("usern","passw","eem"));
        int gameID = databaseSqlGame.createGame("gamen");
        databaseSqlAuth.createAuth(new AuthData("tokwn","usern"));

        databaseSqlUser.clear();
        databaseSqlGame.clear();
        databaseSqlAuth.clear();

        assertNull(databaseSqlUser.getUser("usern"));
        assertNull(databaseSqlGame.getGame(gameID));
        assertNull(databaseSqlAuth.getAuth("tokwn"));
    }


    //create user
    @Test
    @DisplayName("creating a user")
    public void createUserPositive() throws DataAccessException, SQLException {
        clear();
        databaseSqlUser.createUser(new UserData("usernn", "passww","eemm"));

        clear();
    }

    // negative
//    @Test
//    @DisplayName("not creating a user")
//    public void createUserNegative() throws DataAccessException, SQLException {
//        clear();
//        try (var conn = DatabaseManager.getConnection()){
//            assertThrows() conn.prepareStatement("SELECT Username, Password, Email FROM user WHERE Username=?")){
//                ps.setString(1,"usernn");
//                try(var resultSet = ps.executeQuery()){
//                    while (resultSet.next()){
//                        assertThrows(resultSet.getString("p"));
//                    }
//                }
//            }
//
//        }
//        clear();}

    //getUser
        @Test
    @DisplayName("Getting a User")
    public void getUserNegative(){
        //assertThrows(somethingexception),
        }

    //negative


    //AuthSQL
    //clear

    //create auth
    //negative

    //get auth
    //negative

    //deleteauth
    //negative


    //GameSQL
    //clear

    //create game
    //negative

    //getgame
    //negative

    //list games
    //negtive

    //get username
    //negative

    //updategame
    //negative

//    @Test
//    @DisplayName("registering a user and it returns a register result to handler")
//    public void registerPositive() throws DataAccessException, BadRequest, AlreadyTakenException {
//        var authTests = userService.register(new RegisterRequest("joe", "jjj", "chicken"));
//        assertNotNull(authTests.authToken());
//        assertEquals("joe", authTests.username());
//
//    }
//    //try to register a user without a password

//
}

package dataaccess;

import chess.ChessGame;
import com.google.gson.Gson;
import dataaccess.exceptions.DataAccessException;
import dataaccess.exceptions.ResponseException;
import model.GameData;
import model.UserData;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Random;

public class DatabaseSqlGame implements GameDAO {

    public DatabaseSqlGame() throws ResponseException, DataAccessException {
        configureDatabase();
    }
    private void configureDatabase() throws ResponseException, DataAccessException {
        DatabaseManager.createDatabase();
        try (Connection conn = DatabaseManager.getConnection()) {
            for (String statement : createStatements) {
                try (var preparedStatement = conn.prepareStatement(statement)) {
                    preparedStatement.executeUpdate();
                }
            }
        } catch (SQLException ex) {
            throw new ResponseException("Error with configuring the database");
        }
    }
//    private void configureDatabase() throws ResponseException, DataAccessException {
//        DatabaseManager.createDatabase();
//        try (Connection conn = DatabaseManager.getConnection()) {
//            for (String statement : createStatements) {
//                try (var preparedStatement = conn.prepareStatement(statement)) {
//                    preparedStatement.executeUpdate();
//                }
//            }
//        } catch (SQLException ex) {
//            throw new ResponseException("failed to configure database");
//        }
//    }

    private final String[] createStatements = {
            """
            CREATE TABLE IF NOT EXISTS  game (
              `GameID` int NOT NULL AUTO_INCREMENT,
              `WhiteUserName` varchar(256) NOT NULL,
              `BlackUserName` varchar(256) NOT NULL,
              `GameName` varchar(256) NOT NULL,
              `Game` TEXT,
              PRIMARY KEY (`GameID`)
            ) 
            """
            //gson serializes it into a string and json serializes it back into a chess game
    };

    public void clear() throws DataAccessException {
        var statement = "TRUNCATE game";
        try (var conn = DatabaseManager.getConnection();
             var preparedStatement = conn.prepareStatement(statement)) {
            preparedStatement.executeUpdate();
        } catch (SQLException ex) {
            throw new DataAccessException("failed to clear Game", ex);
        }
    }
    private int generateRandomNumber(){
        Random random = new Random();
        return random.nextInt(1,1000000);
    }
    public int createGame(String gameName) throws DataAccessException {
        try(var conn = DatabaseManager.getConnection()){
            try(var ps = conn.prepareStatement("INSERT INTO game (GameID, WhiteUsername, BlackUsername, GameName, Game) VALUES(?, ?, ?, ?, ?)")){
                int gameID = generateRandomNumber();
                ps.setInt(1,gameID);
                ps.setString(2, null);
                ps.setString(3,null);
                ps.setString(4,gameName);
                ps.setString(5,null);
                ps.executeUpdate();
                return gameID;
            } catch (SQLException e) {
                throw new DataAccessException("Create Game Failed");
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        //how to return that int
    }

    public GameData getGame(int gameID) throws DataAccessException {
        try (Connection connection = DatabaseManager.getConnection()) {
            try (PreparedStatement ps = connection.prepareStatement("SELECT GameID, WhiteUsername, BlackUsername, GameName, Game FROM game WHERE GameID=?")) {
                ps.setInt(1,gameID);
                try (ResultSet rs = ps.executeQuery()) {
                    if(rs.next()){
                        return new GameData(rs.getInt("GameID"), rs.getString("WhiteUsername"), rs.getString("BlackUsername"), rs.getString("GameName"), new Gson().fromJson(rs.getString("Game"), ChessGame.class));
                    }
                    else{
                        return null;
                    }
                }
            }
        } catch (Exception e) {
            throw new DataAccessException("error getting Game from database");
        }

    }

    public List<GameData> listGames() throws DataAccessException {
        ArrayList<GameData> newGames = new ArrayList<>();
        try (Connection conn = DatabaseManager.getConnection()) {
            try (var statement = conn.prepareStatement("SELECT GameID, WhiteUsername, BlackUsername, GameName,Game from game")){
                try (var rs = statement.executeQuery()){
                    while(rs.next()){
                        var gameID = rs.getInt("GameID");
                        var whiteUsername = rs.getString("WhiteUsername");
                        var blackUsername = rs.getString("BlackUsername");
                        var gameName = rs.getString("GameName");
                        var chessGame = new Gson().fromJson(rs.getString("Game"), ChessGame.class);
                        newGames.add(new GameData(gameID, whiteUsername,blackUsername,gameName,chessGame));
                    }
                }
        }catch(SQLException e){
            return null;
    }
        return newGames;
    } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }


    public String getUsername(String playerColor, int gameID) throws DataAccessException {
        try (Connection connection = DatabaseManager.getConnection()) {
            String statement;
            if(playerColor.equals("WHITE")){
                statement = "SELECT WhiteUserName FROM game WHERE GameID=?";
            }
            else{
                statement = "SELECT BlackUserName FROM game WHERE GameID=?";
            }
            try (PreparedStatement ps = connection.prepareStatement(statement)) {
                try (ResultSet rs = ps.executeQuery()) {
                    if(rs.next()&&playerColor.equals("WHITE")){
                        return rs.getString("WhiteUserName");
                    }
                    else if(rs.next()&&playerColor.equals("BLACK")){
                        return rs.getString("BlackUserName");
                    }
                    else{
                        return null;
                    }
                }
            }
        } catch (Exception e) {
            throw new DataAccessException("error getting Username from game database");
        }
    }

    public void updateGame(GameData game) throws DataAccessException {
        try(var conn = DatabaseManager.getConnection()){
            var statement = "UPDATE game SET WhiteUsername=?, BlackUsername=?, GameName=?, Game=? WHERE gameID=?";
            try(var ps = conn.prepareStatement(statement)){
                ps.setString(1,game.whiteUsername());
                ps.setString(2,game.blackUsername());
                ps.setString(3, game.gameName());
                String gameVars = new Gson().toJson(game.game());
                ps.setString(4,gameVars);
                ps.setInt(5,game.gameID());

            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}


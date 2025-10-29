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
            throw new ResponseException("failed to configure database");
        }
    }

    private final String[] createStatements = {
            """
            CREATE TABLE IF NOT EXISTS  game (
              `GameID` int NOT NULL AUTO_INCREMENT,
              `WhiteUserName` varchar(256) NOT NULL,
              `BlackUserName` varchar(256) NOT NULL,
              `GameName` varchar(256) NOT NULL
              `Game` String,
              PRIMARY KEY (`GameID`)
            ) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci
            """
            //gson serializes it into a string and json serializes it back into a chess game
    };

    public void clear() throws DataAccessException {

    }

    public int createGame(String gameName) throws DataAccessException {
        try(var conn = DatabaseManager.getConnection()){
            try(var ps = conn.prepareStatement("INSERT INTO game (GameID, WhiteUsername, BlackUsername, GameName, Game) VALUES(?, ?, ?, ?, ?)")){
                ps.executeUpdate();
            } catch (SQLException e) {
                throw new DataAccessException("Create Game Failed");
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        //how to return that int
        return 0;
    }

    public GameData getGame(int gameID) throws DataAccessException {
        try (Connection connection = DatabaseManager.getConnection()) {
            try (PreparedStatement ps = connection.prepareStatement("SELECT GameID, WhiteUsername, BlackUsername, GameName, Game FROM game WHERE GameID=?")) {
                //capitalize
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
            try (PreparedStatement ps = connection.prepareStatement("SELECT Username, Password, Email FROM user WHERE Username=?")) {
                //capitalize
                try (ResultSet rs = ps.executeQuery()) {
                    if(rs.next()){
                        return rs.getString("Username");
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


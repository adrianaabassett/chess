package dataaccess;

import chess.ChessGame;
import com.google.gson.Gson;
import exceptions.DataAccessException;
import exceptions.ResponseException;
import model.GameData;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class DatabaseSqlGame implements GameDAO {

    public DatabaseSqlGame() throws ResponseException, DataAccessException {
        configureDatabaseGame();
    }
    private void configureDatabaseGame() throws ResponseException, DataAccessException {
        DatabaseManager.createDatabase();
        try (Connection conn = DatabaseManager.getConnection()) {
            for (String statement : createStatements) {
                try (var preparedStatement = conn.prepareStatement(statement)) {
                    preparedStatement.executeUpdate();
                }
            }
        } catch (SQLException ex) {
            throw new ResponseException("Error with configuring the game database");
        }
    }

    private final String[] createStatements = {
            """
            CREATE TABLE IF NOT EXISTS  gameTable (
              `GameID` int NOT NULL AUTO_INCREMENT,
              `WhiteUserName` varchar(256),
              `BlackUserName` varchar(256),
              `GameName` varchar(256) NOT NULL,
              `Game` TEXT,
              PRIMARY KEY (`GameID`)
            ) 
            """
            //gson serializes it into a string and json serializes it back into a chess game
    };

    public void clear() throws DataAccessException {
        var statement = "TRUNCATE gameTable";
        try (var conn = DatabaseManager.getConnection();
             var preparedStatement = conn.prepareStatement(statement)) {
            preparedStatement.executeUpdate();
        } catch (SQLException ex) {
            throw new DataAccessException("failed to clear GameTable", ex);
        }
    }
    private int generateRandomNumber(){
        Random random = new Random();
        return random.nextInt(1,1000000);
    }
    public int createGame(String gameName) throws DataAccessException {
        try(var conn = DatabaseManager.getConnection()){
            try(var ps = conn.prepareStatement("INSERT INTO gameTable (GameID, WhiteUsername, BlackUsername, GameName, Game) VALUES(?, ?, ?, ?, ?)")){
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
            String statement = "SELECT GameID, WhiteUsername, BlackUsername, GameName, Game FROM gameTable WHERE GameID=?";
            try (PreparedStatement ps = connection.prepareStatement(statement)) {
                ps.setInt(1,gameID);
                try (ResultSet rs = ps.executeQuery()) {
                    if(rs.next()){
                        int gameIDFromData=rs.getInt("GameID");
                        String wUser = rs.getString("WhiteUsername");
                        String bUser = rs.getString("BlackUsername");
                        String gN = rs.getString("GameName");
                        return new GameData(gameIDFromData, wUser, bUser, gN, new Gson().fromJson(rs.getString("Game"), ChessGame.class));
                    }
                    else{
                        return null;
                    }
                }
            }
        } catch (Exception e) {
            throw new DataAccessException("error getting GameTable from database");
        }

    }

    public List<GameData> listGames() throws DataAccessException {
        ArrayList<GameData> newGames = new ArrayList<>();
        try (Connection conn = DatabaseManager.getConnection()) {
            try (var statement = conn.prepareStatement("SELECT GameID, WhiteUsername, BlackUsername, GameName,Game from gameTable")){
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
                statement = "SELECT WhiteUserName FROM gameTable WHERE GameID=?";
            }
            else{
                statement = "SELECT BlackUserName FROM gameTable WHERE GameID=?";

            }
            try (PreparedStatement ps = connection.prepareStatement(statement)) {
                ps.setInt(1,gameID);
                try (ResultSet rs = ps.executeQuery()) {
                    boolean nextIs= false;
                    if(rs.next()){
                        nextIs = true;}
                    if(nextIs && playerColor.equals("WHITE")){
                        return rs.getString("WhiteUserName");
                    }
                    else if(nextIs && playerColor.equals("BLACK")){
                        return rs.getString("BlackUserName");
                    }
                    else if(nextIs){
                        return null;

                        }
                    }
            }
        } catch (Exception e) {
            throw new DataAccessException("error getting Username from gameTable database");
        }
        return null;
    }

    public void updateGame(GameData game) throws DataAccessException {
        try(var conn = DatabaseManager.getConnection()){
            var statement = "UPDATE gameTable SET WhiteUsername=?, BlackUsername=?, GameName=?, Game=? WHERE gameID=?";
            try(var ps = conn.prepareStatement(statement)){
                ps.setString(1,game.whiteUsername());
                ps.setString(2,game.blackUsername());
                ps.setString(3, game.gameName());
                String gameVars = new Gson().toJson(game.game());
                ps.setString(4,gameVars);
                ps.setInt(5,game.gameID());
                ps.executeUpdate();
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
    public GameData getGameFromGameName(String gameName) throws DataAccessException {
        try (Connection connection = DatabaseManager.getConnection()) {
            String statement = "SELECT GameID, WhiteUsername, BlackUsername, GameName, Game FROM gameTable WHERE GameName=?";
            try (PreparedStatement preparedStatement = connection.prepareStatement(statement)) {
                preparedStatement.setString(1,gameName);
                try (ResultSet resultSet = preparedStatement.executeQuery()) {
                    if(resultSet.next()){
                        int gameInt = resultSet.getInt("GameID");
                        String whiteVal = resultSet.getString("WhiteUsername");
                        String blackVal = resultSet.getString("BlackUsername");
                        String gameVal =  resultSet.getString("GameName");
                        return new GameData(gameInt, whiteVal, blackVal,gameVal, new Gson().fromJson(resultSet.getString("Game"), ChessGame.class));
                    }
                    else{
                        return null;
                    }
                }
            }
        } catch (Exception e) {
            throw new DataAccessException("error getting GameTable from database");
        }
    }
}
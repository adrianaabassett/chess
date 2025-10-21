package dataaccess;

import chess.ChessGame;
import model.GameData;
import model.UserData;

import java.util.HashMap;
import java.util.UUID;
import java.util.Random;

public class MemoryGame implements GameDAO {
    private final HashMap<String, GameData> games = new HashMap<>();
    private int generateRandomNumber(){
        Random random = new Random();
        return random.nextInt(1,1000000);
    }
    public void clear(){
        games.clear();
    }
    public int createGame(String gameName) throws DataAccessException{
        int gameID = generateRandomNumber();
        GameData newGame = new GameData(gameID,null,null,gameName,new ChessGame());
        games.put(String.valueOf(gameID),newGame);
        return gameID;
    }

    public GameData getGame(int gameID) throws DataAccessException{
        return games.get(String.valueOf(gameID));
    }

    public Hashmap<String, GameData>listGames() throws DataAccessException{
        return games;
    }

    void updateGame(GameData game) throws DataAccessException{
        games.remove(game.gameID());
        games.put(game);
    }

    private final HashMap<String, UserData> games = new HashMap<>();

    public void createUser(UserData userData){
        users.put(userData.username(),userData);
    }
    public UserData getUser(String username){
        if(users.containsKey(username)){
            return users.get(username);
        }
        return null;
    }
}

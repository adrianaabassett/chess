package dataaccess;

import chess.ChessGame;
import model.GameData;
import java.util.HashMap;
import java.util.Random;

public class MemoryGame implements GameDAO {
    private final HashMap<String, GameData> games = new HashMap<>();
    private int generateRandomNumber(){
        Random random = new Random();
        return random.nextInt(1,1000000);
    }
    @Override
    public void clear(){
        games.clear();
    }
    @Override
    public int createGame(String gameName) throws DataAccessException{
        int gameID = generateRandomNumber();
        GameData newGame = new GameData(gameID,null,null,gameName,new ChessGame());
        games.put(String.valueOf(gameID),newGame);
        return gameID;
    }
    @Override
    public GameData getGame(int gameID) throws DataAccessException{
        return games.get(String.valueOf(gameID));
    }
    @Override
    public Hashmap<String, GameData> listGames() throws DataAccessException{
        return games;
    }

    @Override
    void updateGame(GameData game) throws DataAccessException{
        games.remove(String.valueOf(game.gameID()));
        games.put(String.valueOf(game.gameID()),game);
    }
    @Override
    public String getUsername(String playerColor, int gameID) throws DataAccessException{
        var game = getGame(gameID);
        if(playerColor.equals("WHITE")){
            return game.whiteUsername();
        }
        else if(playerColor.equals("BLACK")){
            return game.blackUsername();
        }
        else{
            return null;
        }
    }

}

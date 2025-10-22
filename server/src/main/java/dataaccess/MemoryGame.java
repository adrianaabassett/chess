package dataaccess;

import chess.ChessGame;
import model.GameData;
import java.util.HashMap;
import java.util.Random;

public class MemoryGame implements GameDAO {
    private final HashMap<Integer, GameData> games = new HashMap<>();
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
        games.put(gameID,newGame);
        return gameID;
    }
    @Override
    public GameData getGame(int gameID) throws DataAccessException{
        return games.get(gameID);
    }
    @Override
    public HashMap<Integer, GameData> listGames() throws DataAccessException{
        return games;
    }

    @Override
    public void updateGame(GameData game) throws DataAccessException{
        games.remove(game.gameID());
        games.put(game.gameID(),game);
    }

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

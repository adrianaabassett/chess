package dataaccess;
import dataaccess.DataAccessException;
import model.GameData;
import java.util.Collection;

public interface GameDAO {
    void clear() throws DataAccessException;
    int createGame(String gameName) throws DataAccessException;
    GameData getGame(int gameID) throws DataAccessException;
    Hashmap<GameData>listGames() throws DataAccessException;
    void updateGame(GameData game) throws DataAccessException;

}

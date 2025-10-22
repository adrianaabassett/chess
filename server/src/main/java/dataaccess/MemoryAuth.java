package dataaccess;

import dataaccess.exceptions.DataAccessException;
import model.AuthData;

import java.util.HashMap;

public class MemoryAuth implements AuthDAO {
    private final HashMap<String, AuthData> authDatas = new HashMap<>();
    @Override
    public void clear() throws DataAccessException {
        authDatas.clear();
    }
    @Override
    public void createAuth(AuthData authData) throws DataAccessException{
        authDatas.put(authData.getAuthToken(),authData);
    }
    @Override
    public AuthData getAuth(String authToken) throws DataAccessException{
        return authDatas.get(authToken);
    }
    @Override
    public void deleteAuth(String authToken) throws DataAccessException{
        authDatas.remove(authToken);
    }

}

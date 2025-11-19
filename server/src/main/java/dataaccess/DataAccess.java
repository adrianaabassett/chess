package dataaccess;
import exceptions.DataAccessException;
import model.*;

public abstract interface DataAccess {
    UserData getUser(String username) throws DataAccessException;
    void createUser(UserData userData) throws DataAccessException;

    void clear();
}

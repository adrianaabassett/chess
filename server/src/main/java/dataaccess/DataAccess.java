package dataaccess;
import dataaccess.exceptions.DataAccessException;
import model.*;

public abstract interface DataAccess {
    UserData getUser(String username) throws DataAccessException;
    void createUser(UserData userData) throws DataAccessException;

    void clear();
}

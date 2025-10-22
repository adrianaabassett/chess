package dataaccess;
import dataaccess.exceptions.DataAccessException;
import model.*;

public abstract interface DataAccess {
    UserData getUser(String username) throws DataAccessException;
    void createUser(UserData userData) throws DataAccessException;
   // UserList listUsers() throws DataAccessException;
    boolean checkUser(String username, String password) throws DataAccessException;
    void deleteUser(String username) throws DataAccessException;
    void clear();
    //void deleteAllPets() throws DataAccessException;
    //are all of these throwing errors?


        ///gets  user by username
        /// finds userdata by username
        //returns userdata, nothing, or an error
        //where do i create the database?

    ///gets  user by username
    /// finds userdata by username
    //returns userdata, nothing, or an error


    //adds userdata
    //adds auth data
    //void clear();
    //void createUser(UserData user);
    //Userdata getUser(String username);
}

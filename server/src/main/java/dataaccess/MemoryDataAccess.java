package dataaccess;
import model.User;
import model.*;

import javax.xml.crypto.Data;
import java.util.HashMap;
import java.util.Map;

public class MemoryDataAccess implements DataAccess {
    private final HashMap<String, User> users = new HashMap<>();
    private final HashMap<String, AuthData> authentication = new HashMap<>();
    private final HashMap<String, GameData> games = new HashMap<>();

    @Override
    public User getUser(AuthData authData)throws DataAccessException{
        if (authentication.get(authData.username()).authToken() == authData.authToken()){
            return authentication.get(authData.getUsername());
        }
        return null;
    }
    public User getUser(String username) throws DataAccessException {
        //for (Map.Entry<String, User> current:users.entrySet()){

        //username is the key
        return users.get(username);
        //returns null if theres nothing there
        //throw new DataAccessException("Theres no user with this name");
        ///gets  user by username
        /// finds userdata by username
        //returns userdata, nothing, or an error
        //where do i create the database?
    }

    @Override
    public void createUser(User newUser) throws DataAccessException {
        //try {
           if(getUser(newUser.getUsername())!=null){
               users.put(newUser.getUsername(), newUser);
           }
        //} catch (DataAccessException exception) {
            else{
                throw new DataAccessException("The user you are trying to delete does not exist");
           }
        //}
        return;
    }
    public AuthData getAuth(String username){
        return authentication.get(username);
    }
    public void createAuth(AuthData auth) throws DataAccessException{
        authentication.put(auth.username(),auth);
    }
    public void deleteUser(String Username){
        users.remove(Username);
    }

    public boolean checkUser(String Username,String password){
        return(users.get(Username).getPassword() == password);
    }
    @Override
    public void clear() {
        users.clear();
    }
}
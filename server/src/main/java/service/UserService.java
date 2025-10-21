package service;
import dataaccess.MemoryDataAccess;
import jakarta.servlet.Registration;
import model.AuthData;
import model.User;
import dataaccess.DataAccess;
import dataaccess.DataAccessException;
import java.util.UUID;

import java.util.Collection;
import java.util.UUID;


public class UserService {
    private final DataAccess dataAccess;
    public UserService(DataAccess dataAccess){
        this.dataAccess=dataAccess;

    }
    public static String generateRandomString(){
        return UUID.randomUUID().toString();
    }
    public AuthData register(User user) throws DataAccessException{
        MemoryDataAccess memDataAccess = new MemoryDataAccess();
        if(memDataAccess.getUser(user.getUsername())==null){
            String authToken = generateRandomString();
            AuthData authdata = new AuthData(authToken,user.getUsername());
            memDataAccess.createAuth(authdata);
            return authdata;
        }
        else{
            throw new DataAccessException("this username already exists");
        }
        return null;
    }
    public User addUser(User user) throws DataAccessException{
        //here add error for whether it is already in the database
        return dataAccess.addUser(user);
    }

    public UserList listUsers() throws DataAccessException{
        return dataAccess.listPets();
    }

    public User getUser(String username) throws DataAccessException{
        validateUsername(username);
        return dataAccess.getUser(username);
    }

    public void deleteUser(String username) throws DataAccessException{
        validateUsername(username);
        dataAccess.deleteUser(username);
    }

    public void deleteAllUsers() throws DataAccessException{
        Collection<User> users = dataAccess.listUsers();
        if(!users.isEmpty()){
            dataAccess.deleteAllUsers();
        }
    }

    private void validateUsername(String username) throws DataAccessException{
        if(username <= 0){
            throw new DataAccessException(DataAccessException.Code.ClientError, "Error:invalid username");
        }
    }


//    public RegistrationResult register(User user){
//        dataAccess.saveUser(user);
//        return new RegistrationResult(user.username(),"zyyz")
//    }

    //public void register(User user){


        //how to get auth token
//        import java.util.UUID;
//
// ...
//
//        public static String generateToken() {
//            return UUID.randomUUID().toString();
//        }
//    }
//    public void clear(){
//        dataAccess.clear();
//    }
//    public AuthData register(UserData user) throws Exception{
//        if(dataAccess.getUser(user.username())!=null){
//            throw new Exception("already exists");
//        }
//        dataAccess.createUser(user);
//        var authData = new AuthData(user.username(),generateAuthToken());
//
//        return authData;
//    }


    //handler passes in register(register request)
    /// send getUser(username) to dataaccess

    //if get userData, send alreadytakenexception to server

    //if get error, send error to server

    /// if null is returned, send createUser(userData) to Dataaccess
    /// also send createAuth(authData) to Dataaccess(may be removed in future versions)
    /// send registerresult to handler

}
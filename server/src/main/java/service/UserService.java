package service;

import jakarta.servlet.Registration;
import model.AuthData;
import model.User;
import dataaccess.DataAccess;
import datamodel.RegistrationResult;
import datamodel.User;

public class UserService {
    private DataAccess dataAccess;
    public UserService(DataAccess dataAccess){
        this.dataAccess=dataAccess;

    }
    public RegistrationResult register(User user){
        dataAccess.saveUser(user);
        return new RegistrationResult(user.username(),"zyyz")
    }




    public void register(User user){


        //how to get auth token
//        import java.util.UUID;
//
// ...
//
//        public static String generateToken() {
//            return UUID.randomUUID().toString();
//        }
    }
    public void clear(){
        dataAccess.clear();
    }
    public AuthData register(UserData user) throws Exception{
        if(dataAccess.getUser(user.username())!=null){
            throw new Exception("already exists");
        }
        dataAccess.createUser(user);
        var authData = new AuthData(user.username(),generateAuthToken());

        return authData;
    }


    //handler passes in register(register request)
    /// send getUser(username) to dataaccess

    //if get userData, send alreadytakenexception to server

    //if get error, send error to server

    /// if null is returned, send createUser(userData) to Dataaccess
    /// also send createAuth(authData) to Dataaccess(may be removed in future versions)
    /// send registerresult to handler

}
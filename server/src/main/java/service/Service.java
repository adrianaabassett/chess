package service;
import dataaccess.AuthDAO;
import dataaccess.GameDAO;
import dataaccess.UserDAO;
import model.AuthData;
import model.UserData;
import dataaccess.DataAccessException;
import recordrequests.RegisterRequest;
import recordrequests.RegisterResult;

import java.util.UUID;


public class Service {
    UserDAO userDAO;
    public Service(UserDAO userDAO, GameDAO gameDAO, AuthDAO authDAO){
        this.userDAO=userDAO;
    }

    private String generateRandomString(){
        return UUID.randomUUID().toString();
    }
    public RegisterResult register(RegisterRequest regReq) throws DataAccessException{

        if(regReq.username() == null||regReq.password()==null||regReq.email()==null){
            throw new DataAccessException("no username,password, or email");
        }
        if(userDAO.getUser(regReq.username())==null){
            String authToken = generateRandomString();
            AuthData authdata = new AuthData(authToken,regReq.username());
            AuthDAO.createAuth(authdata);
            userDAO.createUser(new UserData(regReq.username(),regReq.password(),regReq.email()));
            RegisterResult regRes = new RegisterResult(regReq.username(), authToken);
            return regRes;
        }
        else{
            throw new DataAccessException("this username already exists");
        }
    }
//    public User addUser(User user) throws DataAccessException{
//        //here add error for whether it is already in the database
//        return dataAccess.addUser(user);
//    }

    //handler passes in register(register request)
    /// send getUser(username) to dataaccess

    //if get userData, send alreadytakenexception to server

    //if get error, send error to server

    /// if null is returned, send createUser(userData) to Dataaccess
    /// also send createAuth(authData) to Dataaccess(may be removed in future versions)
    /// send registerresult to handler

}
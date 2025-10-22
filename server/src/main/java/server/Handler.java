package server;
import com.google.gson.Gson;
import dataaccess.AuthDAO;
import dataaccess.DataAccessException;
import dataaccess.GameDAO;
import dataaccess.UserDAO;
import io.javalin.http.Context;
import model.AuthData;
import model.UserData;
import recordrequests.RegisterRequest;
import recordrequests.RegisterResult;
import service.Service;

import javax.xml.crypto.Data;

public class Handler {
    //UserService userService = new UserService;
    Service service ;


    public Handler(UserDAO userDAO, GameDAO gameDAO, AuthDAO authDAO){
        Service service = new Service(userDAO, gameDAO, authDAO);
    }
    //it is an Authdata because thats what the register result has
    public void registerHandler(Context ctx) throws DataAccessException{
        //the context object has a body which is the json string
        RegisterRequest regReq = new Gson().fromJson(ctx.body(),RegisterRequest.class);
        RegisterResult registerResult = service.register(regReq);
        //below sets the json, which is the response to the request of the server
        ctx.status(200);
        ctx.json(new Gson().toJson(registerResult));
    }
    public void clearHandler(Context ctx) throws DataAccessException{
            service.clear();
            ctx.status(200);

    }

    public void loginUser(Context ctx) throws DataAccessException{
        UserData userData = new Gson().fromJson(ctx.body(),UserData.class);
        AuthData authData = service.loginUser(userData);
        ctx.status(200);
    }

    public void logoutUser(Context ctx) throws DataAccessException{
           String authToken = ctx.header("Authorization");
           service.logoutUser(authToken);
           ctx.status(200);
    }

    }

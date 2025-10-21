package server;
import com.google.gson.Gson;
import dataaccess.DataAccessException;
import dataaccess.UserDAO;
import io.javalin.http.Context;
import recordrequests.RegisterRequest;
import recordrequests.RegisterResult;
import service.Service;

public class Handler {
    //UserService userService = new UserService;
    Service service ;

    public Handler(UserDAO userDAO){
        service = new Service(userDAO);
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
//    public static String generateRandomString(){
//        return UUID.randomUUID().toString();
//    }
    }
    //string login returns a serialized string
    //logout
    //list games
    //create game
    //join game
    //clear application


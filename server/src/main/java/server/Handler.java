package server;
import com.google.gson.Gson;
import dataaccess.AuthDAO;
import dataaccess.exceptions.*;
import dataaccess.GameDAO;
import dataaccess.UserDAO;
import io.javalin.http.Context;
import model.AuthData;
import model.GameData;
import model.UserData;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import recordrequests.JoinGameRequest;
import recordrequests.RegisterRequest;
import recordrequests.RegisterResult;
import service.Service;

import java.util.Map;

public class Handler {
    private static final Logger LOG = LoggerFactory.getLogger(Handler.class);
    //UserService userService = new UserService;
    Service service ;


    public Handler(UserDAO userDAO, GameDAO gameDAO, AuthDAO authDAO){
        service = new Service(userDAO, gameDAO, authDAO);
    }
    //it is an Authdata because thats what the register result has
    public void registerHandler(Context ctx) throws DataAccessException, BadRequest {
        //the context object has a body which is the json string
        try {
                RegisterRequest regReq = new Gson().fromJson(ctx.body(), RegisterRequest.class);
        RegisterResult registerResult = service.register(regReq);
            ctx.status(200);
            //ctx.result("{}");
            ctx.json(new Gson().toJson(registerResult));
        //below sets the json, which is the response to the request of the server
        }
        catch(BadRequest e){
            ctx.status(400);
            ctx.result("{\"message\":\"Error:bad request\"}");
        }
        catch(AlreadyTakenException e){
            ctx.status(403);
            ctx.result("{\"message\":\"Error:already taken\"}");
        }
        catch(DataAccessException e){
            ctx.status(500);//was 501
        ctx.result("{\"message\":\"Error:"+e.getMessage()+"\"}");
        }
    }
    public void clearHandler(Context ctx) throws DataAccessException{
        ctx.status(200);
        ctx.result("{}");
            try {
                service.clear();
            ctx.status(200);
                ctx.result("{}");
            }
            catch(DataAccessException e){
                //might nned to be changed to unauthorized seperately
                ctx.status(500);
                ctx.result("{\"message\":\"Error:"+e.getMessage()+"\"}");
            }

    }

    public void loginUser(Context ctx) throws DataAccessException, UnauthorizedException, BadRequest, InvalidID {
        try{
            UserData userData = new Gson().fromJson(ctx.body(),UserData.class);
        AuthData authData = service.loginUser(userData);
        ctx.status(200);
            ctx.result(new Gson().toJson(authData));
        }
        catch(DataAccessException e){
            ctx.status(500);//was 401
            ctx.result("{\"message\":\"Error:unknown\"}");
        }
        catch(UnauthorizedException e){
            ctx.status(401);
            ctx.result("{\"message\":\"Error:unauthorized\"}");
        }
        catch(BadRequest e){
            ctx.status(400);
            ctx.result("{\"message\":\"Error:bad request\"}");
        }
        catch(InvalidID e){
            ctx.status(403);
            ctx.result("{\"message\":\"Error:already taken\"}");
        }
    }

    public void logoutUser(Context ctx) throws DataAccessException, UnauthorizedException {
        try{
        String authToken = ctx.header("Authorization");
        service.logoutUser(authToken);
        ctx.status(200);
        ctx.result("{}");
    }
        catch(DataAccessException e){
                ctx.status(500);//was 405
            ctx.result("{\"message\":\"Error:unknown\"}");
        }
        catch(UnauthorizedException e){
            ctx.status(401);
            ctx.result("{\"message\":\"Error:unauthorized\"}");
        }
    }

    public GameData createGame(Context ctx) throws DataAccessException, UnauthorizedException, BadRequest {
        try {
            GameData gameData = new Gson().fromJson(ctx.body(), GameData.class);
            GameData newGame = service.createGame(gameData.gameName(), ctx.header("Authorization"));
            ctx.status(200);
            ctx.result(new Gson().toJson(Map.of("gameID",newGame.gameID())));
            return newGame;
        }
        catch(DataAccessException e){
            ctx.status(500);//was 405
            ctx.result("{\"message\":\"Error:"+e.getMessage()+"\"}");
        }
        catch(UnauthorizedException e){
            ctx.status(401);
            ctx.result("{\"message\":\"Error:unauthorized\"}");

        }
        catch(BadRequest e){
            ctx.status(400);
            ctx.result("{\"message\":\"Error:bad request\"}");
        }
        return null;
    }

    public void listGames(Context ctx) throws DataAccessException{
        try {
            var obj = new Gson().toJson(Map.of("games",service.listGames(ctx.header("Authorization"))));
            ctx.status(200);
            ctx.result(obj);
        }catch(DataAccessException e){
                ctx.status(500);
                ctx.result("{\"message\":\"Error:unknown\"}");
        }
        catch(UnauthorizedException e){
            ctx.status(401);
            ctx.result("{\"message\":\"Error:Unauthorized\"}");
        }
    }
    public void joinGame(Context ctx) throws DataAccessException, BadRequest, UnauthorizedException, InvalidID {
        try{
            JoinGameRequest joinGameRequest = new Gson().fromJson(ctx.body(),JoinGameRequest.class);
        service.joinGame(ctx.header("Authorization"),joinGameRequest.playerColor(),joinGameRequest.gameID());
        ctx.status(200);
            ctx.result("{}");
    }catch(DataAccessException e){
        ctx.status(500);//was 405
            ctx.result("{\"message\":\"Error:"+e.getMessage()+"\"}");
    }
        catch(BadRequest e){
            ctx.status(400);
            ctx.result("{\"message\":\"Error:bad request\"}");
        }
        catch(UnauthorizedException e){
            ctx.status(401);
            ctx.result("{\"message\":\"Error:unauthorized\"}");

        }
        catch(InvalidID e){
            ctx.status(403);
            ctx.result("{\"message\":\"Error:already taken\"}");

        }
    }
    }

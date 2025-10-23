package server;
import com.google.gson.Gson;
import dataaccess.AuthDAO;
import dataaccess.exceptions.BadRequest;
import dataaccess.exceptions.DataAccessException;
import dataaccess.GameDAO;
import dataaccess.UserDAO;
import dataaccess.exceptions.InvalidID;
import dataaccess.exceptions.UnauthorizedException;
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

public class Handler {
    private static final Logger log = LoggerFactory.getLogger(Handler.class);
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
            ctx.json(new Gson().toJson(registerResult));
        //below sets the json, which is the response to the request of the server
        }
        catch(BadRequest e){
            ctx.status(400);
            ctx.result("{\"message\":\"Error:ill add this later\"}");
        }
        catch(DataAccessException e){
            ctx.status(500);}
    }
    public void clearHandler(Context ctx) throws DataAccessException{
            try{
                service.clear();

            ctx.status(200);
        }
            catch(DataAccessException e){
                ctx.status(400);
            }
    }

    public void loginUser(Context ctx) throws DataAccessException, UnauthorizedException, BadRequest, InvalidID {
        try{
            UserData userData = new Gson().fromJson(ctx.body(),UserData.class);
        AuthData authData = service.loginUser(userData);
        ctx.status(200);
        }
        catch(DataAccessException e){
            ctx.status(501);
        }
        catch(UnauthorizedException e){
            ctx.status(401);
        }
        catch(BadRequest e){
            ctx.status(400);
        }
        catch(InvalidID e){
            ctx.status(403);
        }
    }

    public void logoutUser(Context ctx) throws DataAccessException, UnauthorizedException {
        try{
            String authToken = ctx.header("Authorization");
        service.logoutUser(authToken);
        ctx.status(200);
    }
        catch(DataAccessException e){
                ctx.status(405);
        }
        catch(UnauthorizedException e){
            ctx.status(401);
        }
    }

    public GameData createGame(Context ctx) throws DataAccessException, UnauthorizedException, BadRequest {
        try {
            GameData gameData = new Gson().fromJson(ctx.body(), GameData.class);
            GameData newGame = service.createGame(gameData.gameName(), ctx.header("Authorization"));
            ctx.status(200);
            return newGame;
        }
        catch(DataAccessException e){
            ctx.status(405);
        }
        catch(UnauthorizedException e){
            ctx.status(401);
        }
        catch(BadRequest e){
            ctx.status(400);
        }
        return null;
    }

    public void listGames(Context ctx) throws DataAccessException{
        try {
            new Gson().toJson(service.listGames(ctx.header("Authorization")));
            ctx.status(200);
        }catch(DataAccessException e){
                ctx.status(405);
        }
//        public String listGames(Context ctx) throws DataAccessException{
//            ctx.status(200);
//            return new Gson().toJson(service.listGames(ctx.header("Authorization")));
    }
    public void joinGame(Context ctx) throws DataAccessException, BadRequest, UnauthorizedException, InvalidID {
        try{
            JoinGameRequest joinGameRequest = new Gson().fromJson(ctx.body(),JoinGameRequest.class);
        service.joinGame(joinGameRequest.authToken(),joinGameRequest.playerColor(),joinGameRequest.gameID());
        ctx.status(200);
    }catch(DataAccessException e){
        ctx.status(405);
    }
        catch(BadRequest e){
            ctx.status(400);
        }
        catch(UnauthorizedException e){
            ctx.status(401);
        }
        catch(InvalidID e){
            ctx.status(403);
        }
    }
    }

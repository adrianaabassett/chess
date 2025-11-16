package server;
import dataaccess.*;
import dataaccess.exceptions.DataAccessException;
import dataaccess.exceptions.ResponseException;
import io.javalin.*;

import static java.lang.System.out;

public class Server {

    //create the hashmap memories here so that there will only be one of each
    UserDAO memoryUser;
    {
        try {
            memoryUser = new DatabaseSqlUser();
        } catch (ResponseException e) {
            throw new RuntimeException(e);
        } catch (DataAccessException e) {
            throw new RuntimeException(e);
        }
    }

    GameDAO memoryGame;
    {
        try {
            memoryGame = new DatabaseSqlGame();
        } catch (ResponseException e) {
            throw new RuntimeException(e);
        } catch (DataAccessException e) {
            throw new RuntimeException(e);
        }
    }

    AuthDAO memoryAuth;
    {
        try {
            memoryAuth = new DatabaseSqlAuth();
        } catch (ResponseException e) {
            throw new RuntimeException(e);
        } catch (DataAccessException e) {
            throw new RuntimeException(e);
        }
    }

    ///GameDAO memoryGame= new MemoryGame();
    ///   AuthDAO memoryAuth = new MemoryAuth();

//    private final UserService service;
    private final Javalin javalin;

    public Server() {
        out.println("got to server");
        Handler handler = new Handler(memoryUser,memoryGame,memoryAuth);
        javalin = Javalin.create(config -> config.staticFiles.add("web"));

        javalin.post("/user", handler::registerHandler);
        javalin.delete("/db",handler::clearHandler);
        javalin.post("/session",handler::loginUser);
        javalin.delete("/session",handler::logoutUser);
        javalin.post("/game", handler::createGame);
        javalin.get("/game", handler::listGames);
        javalin.put("/game", handler::joinGame);
    }

    public int run(int desiredPort) {
        javalin.start(desiredPort);
        return javalin.port();
    }

    //checks which port we're on
    public int port(){
        return javalin.port();
    }
    public void stop() {
        javalin.stop();
    }



}



/////already taken exception brought here from service
////returns 403, error, username already taken

/////Bad request exception from service
////returns 400 error bad request

/////returns another unknown failure from service
////500 error

///// successfully given username and authtoken from handler
////200 username and auth token to client
package server;
import dataaccess.*;
import io.javalin.*;

public class Server {

    //create the hashmap memories here so that there will only be one of each
    UserDAO memoryUser = new MemoryUser();
    GameDAO memoryGame = new MemoryGame();
    AuthDAO memoryAuth = new MemoryAuth();
    ///GameDAO memoryGame= new MemoryGame();
    ///   AuthDAO memoryAuth = new MemoryAuth();

//    private final UserService service;
    private final Javalin javalin;

    public Server() {
        Handler handler = new Handler(memoryUser,memoryGame,memoryAuth);
        javalin = Javalin.create(config -> config.staticFiles.add("web"));

        javalin.post("/user", handler::registerHandler);
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
package server;

import io.javalin.*;

public class Server {

    private final Javalin javalin;

    public Server() {
        javalin = Javalin.create(config -> config.staticFiles.add("web"));
        // Register your endpoints and exception handlers here.
    }
    public int run(int desiredPort) {
        javalin.start(desiredPort);
        return javalin.port();
    }
    public void stop() {
        javalin.stop();
    }
}


//////////Registration

//post, bring in user, with username, passward, email
///send all three to the handler

/////already taken exception brought here from service
////returns 403, error, username already taken

/////Bad request exception from service
////returns 400 error bad request

/////returns another unknown failure from service
////500 error

///// successfully given username and authtoken from handler
////200 username and auth token to client
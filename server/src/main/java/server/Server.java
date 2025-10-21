package server;
import com.google.gson.Gson;
import exception.ResponseException;
import model.AuthData;
import model.User;
import io.javalin.http.Context;
import server.websocket.WebSocketHandler;
import service.UserService;
import io.javalin.*;

public class Server {
    private final UserService service;
    private final WebSocketHandler webSocketHandler;
    private final Javalin javalin;

    public UserServer(UserService service){
        this.service = service;
        this.webSocketHandler = new WebsocketHandler();

        this.javalin = Javalin.create(config -> config.staticFiles.add("public"));
            .post("/user", this::addUser)
            .get("/user", this::listUsers)
                .delete("/user/{id}",this::deleteUser)
                .delete("/user",this::deleteAllUsers)
                .exception(ResponseException.class,this::exceptionHandler)
                .ws("/ws",ws->{
                   ws.onConnect(webSocketHandler);
                   ws.onMessage(webSocketHandler);
                   ws.onClose(webSocketHandler);
                });
    }

//    public Server() {
//        javalin = Javalin.create(config -> config.staticFiles.add("web"));
//        // Register your endpoints and exception handlers here.
//    }
    public int run(int desiredPort) {
        javalin.start(desiredPort);
        return javalin.port();
        //its return this on petshop
    }

    public int port(){
        return javalin.port();
    }
    public void stop() {
        javalin.stop();
    }
    private void exceptionHandler(ResponseException ex, Context ctx){
        ctx.status(ex.toHttpStatusCode());
        ctx.json(ex.toJson());
    }

    private void addUser(Context ctx) throws ResponseException{
        User user = new Gson().fromJson(ctx.body(),User.class);
        user = service.addUser(user);
        ///theres a makenoise on pet here
        ctx.json(new Gson().toJson(user));
    }

    private void listUsers(Context ctx) throws ResponseException{
        ctx.json(service.listPets().toString());
    }

    private void deleteUser(Context ctx) throws ResponseException {
        var username = String.format(ctx.pathParam("username"));
        User user = service.getUser(username);
        if (pet != null){
            service.deletePet(id);
            //make noise for web socket handler here
            cfx.status(204);
        }
        else{ctx.status(404)}
                /// may throw an error because id isnt a username
    }

    private void deleteAllUsers(Contenxt ctx)throws ResponseException{
        service.deleteAllUsers();
        ctx.status(204);
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
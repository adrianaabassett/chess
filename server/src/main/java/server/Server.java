package server;
import com.google.gson.Gson;
import dataaccess.DataAccessException;
import dataaccess.MemoryUser;
import dataaccess.UserDAO;
import model.UserData;
import io.javalin.http.Context;
import io.javalin.*;

public class Server {

    //create the hashmap memories here so that there will only be one of each
    UserDAO memoryUser = new MemoryUser();
    ///GameDAO memoryGame= new MemoryGame();
    ///   AuthDAO memoryAuth = new MemoryAuth();

//    private final UserService service;
    private final Javalin javalin;

    public Server(){
        Handler handler = new Handler(memoryUser);
    javalin =Javalin.create(config->config.staticFiles.add("web"));
        //////just like petshop aaaa
        javalin.post("/user", handler::registerHandler);
                /// /it says just .post on petshop
//            javalin.get("/user", this::listUsers)
//                javalin.delete("/user/{id}",this::deleteUser)
//                javalin.delete("/user",this::deleteAllUsers)
//                javalin.exception(DataAccessException.class,this::exceptionHandler);
                });

    public int run(int desiredPort) {
        javalin.start(desiredPort);
        return javalin.port();
        //its return this on petshop
    }

    //checks which port we're on
    public int port(){
        return javalin.port();
    }
    public void stop() {
        javalin.stop();
    }


    private void addUser(Context ctx) throws DataAccessException{
        UserData user = new Gson().fromJson(ctx.body(), UserData.class);
        user = service.createUser(user);
        ///theres a makenoise on pet here
        ctx.json(new Gson().toJson(user));
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
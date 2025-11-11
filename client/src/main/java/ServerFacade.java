import model.UserData;
import dataaccess.exceptions.ResponseException;

public class ServerFacade {
    //the point of server facade is to be able to call the javalin things and it works and
    public UserData addUser(UserData user) throws ResponseException{
        var request = buildRequest("POST", "/user",user);
        var response = sendRequest(request);
        return handleResponse(response,UserData.class);
    }

    public void deleteUser(UserData) throws ResponseException {
        var path = String.format("/pet/%s", id);
        var request = buildRequest("DELETE", path, null);
        var response = sendRequest(request);
        handleResponse(response, null);
    }
     javalin.delete("/db",handler::clearHandler);
        javalin.post("/session",handler::loginUser);
        javalin.delete("/session",handler::logoutUser);
        javalin.post("/game", handler::createGame);
        javalin.get("/game", handler::listGames);
        javalin.put("/game", handler::joinGame);

}

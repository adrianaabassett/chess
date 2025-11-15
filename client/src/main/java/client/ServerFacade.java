package client;

import com.google.gson.Gson;
import model.AuthData;
import model.GameData;
import model.UserData;
import dataaccess.exceptions.ResponseException;
import recordrequests.RegisterRequest;

import java.lang.reflect.Array;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

public class ServerFacade {//represents the server, the middleman between th eclient
    private final HttpClient client = HttpClient.newHttpClient();
    private final String serverUrl ;
    private boolean loggedIn = false;
    private String authToken = "";
    public ServerFacade(String serverUrl){
        this.serverUrl = serverUrl;
    }
    public ServerFacade(){
        this.serverUrl = "http://localhost:8080";
    }
    //the point of server facade is to be able to call the javalin things and it works and
    public AuthData addUser(RegisterRequest regReq) throws ResponseException{
        if(loggedIn){
            throw new ResponseException("this is only for logged out profiles");
        }
        var request = buildRequest("POST", "/user",regReq, null);
        var response = sendRequest(request);
        loggedIn = true;
        return handleResponse(response,AuthData.class);
    }

    public void clear() throws ResponseException {
        loggedIn = false;
        var request = buildRequest("DELETE", "/db",null, null);
        var response = sendRequest(request);
        handleResponse(response,null);
    }

    public AuthData loginUser(UserData userData) throws ResponseException{
        var request = buildRequest("POST","/session",userData, null);
        var response = sendRequest(request);
        return handleResponse(response,AuthData.class);
    }

    public void logoutUser(String authToken) throws ResponseException{
        if(!loggedIn){
            throw new ResponseException("not logged in and cannot log out");
        }
        var request = buildRequest("DELETE", "/session", null, authToken);
        var response = sendRequest(request);
        handleResponse(response,null);
    }

    public GameData createGame(GameData gameData, String authToken) throws ResponseException {
        if(!loggedIn){
            throw new ResponseException("not logged in and cannot create a game");
        }
        var request = buildRequest("POST", "/game",gameData, authToken);
        var response = sendRequest(request);
        return handleResponse(response,GameData.class);
    }

    public Array listGames(String authToken) throws ResponseException{
        if(!loggedIn){
        throw new ResponseException("not logged in and cannot complete this function");
        }

        var request = buildRequest("GET", "/game",null, authToken);
        var response = sendRequest(request);
        return handleResponse(response, Array.class);
    }

    public void joinGame(String[] colorAndInt, String authToken) throws ResponseException {
        if(!loggedIn){
        throw new ResponseException("not logged in and cannot complete this function");
        }
        var request = buildRequest("PUT", "/game", colorAndInt, authToken );//2 thing json
        var response = sendRequest(request);
        handleResponse(response, null);
    }

    private HttpRequest buildRequest(String method, String path, Object body, String authorization) { //("POST", "/user",regReq);
        var request = HttpRequest.newBuilder()
                .uri(URI.create(serverUrl + path))
                .method(method, makeRequestBody(body));
        if (body != null) {
            request.setHeader("Content-Type", "application/json");
        }
        if(authorization != null){
            request.header("authorization", authorization );
        }
        return request.build();
    }

    private HttpRequest.BodyPublisher makeRequestBody(Object request) {
        if (request != null) {
            return HttpRequest.BodyPublishers.ofString(new Gson().toJson(request));
        } else {
            return HttpRequest.BodyPublishers.noBody();
        }
    }

    private HttpResponse<String> sendRequest(HttpRequest request) throws ResponseException {
        try {
            return client.send(request, HttpResponse.BodyHandlers.ofString());
        } catch (Exception ex) {
            throw new ResponseException(ex.getMessage());
        }
    }

    private <T> T handleResponse(HttpResponse<String> response, Class<T> responseClass) throws ResponseException {
        var status = response.statusCode();
        if (!isSuccessful(status)) {
            var body = response.body();
            if (body != null) {
                throw new ResponseException(body);
            }
            throw new ResponseException("Other failure from handle response");
        }

        if (responseClass != null) {
            return new Gson().fromJson(response.body(), responseClass);
        }

        return null;
    }

    private boolean isSuccessful(int status) {
        return status / 100 == 2;
    }
}



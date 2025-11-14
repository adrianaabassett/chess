package client;

import model.AuthData;
import model.GameData;
import model.UserData;
import dataaccess.exceptions.ResponseException;
import recordrequests.RegisterRequest;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

public class ServerFacade {
    private final HttpClient client = HttpClient.newHttpClient();
    private final String serverUrl;

    public ServerFacade(String url) {
        serverUrl = url;
    }
    //the point of server facade is to be able to call the javalin things and it works and
    public UserData addUser(RegisterRequest regReq) throws ResponseException{
        var request = buildRequest("POST", "/user",regReq);
        var response = sendRequest(request);
        return handleResponse(response,UserData.class);
    }

    public void clear() throws ResponseException {
        var request = buildRequest("DELETE", "/db",null);
        var response = sendRequest(request);
        handleResponse(response,null);
    }

    public AuthData loginUser(UserData userData) throws ResponseException{
        var request = buildRequest("POST","/session",userData);
        var response = sendRequest(request);
        return handleResponse(response,AuthData.class);
    }

    public void logoutUser(UserData userData) throws ResponseException{
        var request = buildRequest("DELETE", "/session", null);
        var response = sendRequest(request);
        handleResponse(response,null);
    }

    public GameData createGame(GameData gameData) throws ResponseException {
        var request = buildRequest("POST", "/game",gameData);
        var response = sendRequest(request);
        return handleResponse(response,GameData.class);
    }

    public String listGames(String authToken) throws ResponseException{
        var request = buildRequest("GET", "/game",authToken);
        var response = sendRequest(request);
        return handleResponse(response,String.class);
    }

//    public void joinGame(imnotsuer String authToken, String playerColor, Integer gameID) throws ResponseException {
//        var request = buildRequest("PUT", "/game", imenotesure);//2 thing json
//        var response = sendRequest(request);
//        handleResponse(response, null);
//    }

    private HttpRequest buildRequest(String method, String path, Object body) { //("POST", "/user",regReq);
        var request = HttpRequest.newBuilder()
                .uri(URI.create(serverUrl + path))
                .method(method, makeRequestBody(body));
        if (body != null) {
            request.setHeader("Content-Type", "application/json");
        }
        return request.build();
    }
//
//    private HttpRequest.BodyPublisher makeRequestBody(Object request) {
//        if (request != null) {
//            return BodyPublishers.ofString(new Gson().toJson(request));
//        } else {
//            return BodyPublishers.noBody();
//        }
//    }

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
                throw ResponseException.fromJson(body);
            }

            throw new ResponseException(ResponseException.fromHttpStatusCode(status), "other failure: " + status);
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



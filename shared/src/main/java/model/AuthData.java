package model;

import com.google.gson.Gson;

public record AuthData(String authToken, String username){
    public AuthData setAuthToken(String AuthToken){
        return new AuthData(AuthToken, this.username);
    }
    public String getAuthToken(){
        return authToken;
    }
    public AuthData setUsername(String Username){
        return new AuthData(this.authToken, Username);
    }
    public String getUsername(){
        return username;
    }
    public String toString(){
        return new Gson().toJson(this);
    }
}

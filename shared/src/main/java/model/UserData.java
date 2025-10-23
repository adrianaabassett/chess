package model;
import com.google.gson.*;

public record UserData(String username, String password, String email){

    public String getUsername(){
        return this.username;
    }
    public String getPassword(){
        return this.password;
    }
    public String toString(){
        return new Gson().toJson(this);
    }
}



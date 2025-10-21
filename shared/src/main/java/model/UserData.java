package model;
import com.google.gson.*;

public record UserData(String username, String password, String email){
    public UserData setUsername(String username){
        return new UserData(username, this.password,this.email);
    }
    public String getUsername(){
        return this.username;
    }
    public String getPassword(){
        return this.password;
    }
    public UserData setPassword(String Password){
        return new UserData(this.username, Password,this.email);
    }
    public UserData setEmail(String Email){
        return new UserData(this.username, this.password,Email);
    }
    public String toString(){
        return new Gson().toJson(this);
    }
}



package model;
import com.google.gson.*;

public record User(String Username, String password, String email){
    public User setUsername(String Username){
        return new User(Username, this.password,this.email);
    }
    public String getUsername(){
        return this.Username;
    }
    public String getPassword(){
        return this.password;
    }
    public User setPassword(String Password){
        return new User(this.Username, Password,this.email);
    }
    public User setEmail(String Email){
        return new User(this.Username, this.password,Email);
    }
    public String toString(){
        return new Gson().toJson(this);
    }
}



package dataaccess;

import model.UserData;

import java.util.HashMap;

public class MemoryUser implements UserDAO{
    //implements UserDAO
    private final HashMap<String, UserData> users = new HashMap<>();
    public void clear(){
        users.clear();
    }
    public void createUser(UserData userData){
        users.put(userData.username(),userData);
    }
    public UserData getUser(String username){
        if(users.containsKey(username)){
            return users.get(username);
        }
        return null;
    }
}

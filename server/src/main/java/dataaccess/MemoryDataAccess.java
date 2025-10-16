package dataaccess;
import java.util.HashMap;

public class MemoryDataAccess implements DataAccess{
    private final HashMap<String> users = new HashMap<>();
    @Override
    public void clear(){
        users.clear();}
    @Override
    public void createUser(Username, Password, users.put(user.username(),user));
    @Override
    public UserData getUser(){
        return users.get(username);
    }
}

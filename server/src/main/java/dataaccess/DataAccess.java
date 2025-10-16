package dataaccess;
import datamodel.*;

public interface DataAccess {
    void clear();
    void createUser(UserData user);
    Userdata getUser(String username);
}

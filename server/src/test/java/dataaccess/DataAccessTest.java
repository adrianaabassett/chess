package dataaccess;
import datamodel.*;
import model.AuthData;
import model.User;
import org.junit.jupiter.api.*;

import static org.junit.jupiter.api.Assertions.*;

class DataAccessTest {

    @Test
    void createUserPositive() throws DataAccessException{
        var user = new User("joe","j@j","j");
        DataAccess da = new MemoryDataAccess();
        assertNull(da.getUser(user.getUsername()));
        da.createUser(user);
        assertNotNull(da.getUser(user.getUsername()));
        da.clear();
        assertNull(da.getUser(user.getUsername()));
            }

            @Test
     void createUserNegative(){

            }



    @Test
    void clear(){

    }

}

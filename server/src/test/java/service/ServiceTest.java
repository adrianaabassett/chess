package service;

import org.junit.jupiter.api.Test;
import model.*;
import dataaccess.DataAccessException;

public class ServiceTest {

    @Test
    void register() throws Exception{
        var user = new UserData("joe","j@j","j");
        var at = "xyz";
//
//        var da = new DataAccess();
//        var service = new UserService();

        var da = new MemoryDataAccess();
        var service = new Service(da);
        AuthToken res = service.register(user);
        assertNotNull(res);
        assertEquals(res.username(), user.username());
        assertNotNull(res.authToken());
        assertEquals(String.class, res.authToken().getClass());
    }

    @Test
    void testRegister() throws DataAccessException {
        void
    }
    @Test
    void clear(){

    }
}

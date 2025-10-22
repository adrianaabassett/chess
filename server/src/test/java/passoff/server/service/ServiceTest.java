package passoff.server.service;
import dataaccess.*;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import recordrequests.RegisterRequest;
import service.Service;
import model.UserData;

import javax.xml.crypto.Data;

import static org.junit.jupiter.api.Assertions.*;

// for simplicity's sake, I'm putting all of my testis in this file
public class ServiceTest {
    MemoryUser userMemory = new MemoryUser();
    MemoryGame gameMemory = new MemoryGame();
    MemoryAuth authMemory = new MemoryAuth();
    Service userService = new Service(userMemory,gameMemory,authMemory);
    @Test
    @DisplayName("registering a user and it returns a register result to handler")
    public void registerPositive() throws DataAccessException {
        var userTest = new UserData("joe","jjj","chicken");
        var authTest = userService.register(new RegisterRequest("joe","jjj","chicken"));
        assertNotNull(authTest.authToken());
        assertEquals("joe", authTest.username());

    }
    //try to register a user without a password

    @Test
    @DisplayName("registering a user but it returns with an error")
    public void registerNegative() throws DataAccessException {
        var userTest = new UserData("joe","jjj","chicken");
        var authTest = userService.register(new RegisterRequest("joe","jjj","chicken"));
        assertNotNull(authTest.authToken());
        assertEquals("joe", authTest.username());

    }
//
    @Test
    @DisplayName("Logged in")
    public void loginPositive() throws DataAccessException{
        var userTest = new UserData("usern","pass","email");
        userService.register(new RegisterRequest(userTest.username(),userTest.password(),userTest.email()));
        var authTest = userService.login(userTest);
        assertNotNull(authTest.authToken());
        assertEquals("usern",authtest.username());
    }
//
//    @Test
//    @DisplayName("Not Logged in because of Error")
//    public void loginNegative throws DataAccessException{
//        var userTest = new UserData("user", "pass", "email");
//        userService.register(userTest);
//        var userTestTwo = new UserData("user","notpass", "email");
//        assertThrows(userService.login(userTestTwo));
//    }
//    @Test
//    @DisplayName("logged out")
//
//    @Test
//    @DisplayName("not logged out")
//
//    @Test
//    @DisplayName("list games")
//
//    @Test
//    @DisplayName("cant list games")
//
//    @Test
//    @DisplayName("create new game")
//
//    @Test
//    @DisplayName("cant create new game")
//
//    @Test
//    @DisplayName("join game")
//
//    @Test
//    @DisplayName("cant join game")
//
//    @Test
//    @DisplayName("clear")
//    var user = new UserData("joe","j@j","j");
//    MemoryUser da = new MemoryUser();
//    assertNull(d()a.getUser(user.username()));
//    da.createUser(user);
//    assertNotNull(da.getUser(user.username));
//        @Override
//        public void createUser(UserData userData) {
//
//        }
//
//        @Override
//        public UserData getUser(String username) {
//            return null;
//        }
//    }
//
//    @Test
//    @DisplayName("cant clear")
//
}

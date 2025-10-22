//package passoff.server;
//import dataaccess.DataAccessException;
//import dataaccess.MemoryAuth;
//import dataaccess.MemoryGame;
//import dataaccess.MemoryUser;
//import org.junit.jupiter.api.Assertions;
//import org.junit.jupiter.api.DisplayName;
//import org.junit.jupiter.api.Test;
//import recordrequests.RegisterRequest;
//import service.Service;
//import model.UserData;
//
//public class ServiceTest {
//    var userMemory = new MemoryUser();
//    var gameMemory = new MemoryGame();
//    var authMemory = new MemoryAuth();
//    var userService = new Service(userMemory,gameMemory,authMemory);
//    @Test
//    @DisplayName("registering a user and it returns a register result to handler")
//    public void registerPositive() throws DataAccessException {
//        var userTest = new UserData("cow","rat","chicken");
//        var authTest = userService.register(new RegisterRequest())
//        var res = userService.register(new UserData("cow","rat","john"));
//        Assertions.assertNotNull(res);
//    }
//    //try to register a user without a password
//
//    @Test
//    @DisplayName("registering a user but it returns with an error alreay taken to server")
//    public void registerNormal(){
//        var dataAccess = new MemoryDataAccess();
//        //try to register user with a password
//        var userService = new Service(dataAccess);
//        var res = userService.register(new UserData("cow","rat","john"));
//        Assertions.assertNotNull(res);
//    }
//
//
//    @Test
//    @DisplayName("registering a user but it returns with an error bad request")
//}

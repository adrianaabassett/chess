package passoff.server;
import dataaccess.MemoryDataAccess;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import service.UserService;
import service.UserServiceTest;
import model.User;

public class ServiceTest {
    @Test
    @DisplayName("registering a user and it returns a register result to handler")
    public void registerNormal(){
        var dataAccess = new MemoryDataAccess();

                var userService = new UserService(dataAccess);
        var res = userService.register(new User("cow","rat","john"));
        Assertions.assertNotNull(res);
    }
    //try to register a user without a password

    @Test
    @DisplayName("registering a user but it returns with an error alreay taken to server")
    public void registerNormal(){
        var dataAccess = new MemoryDataAccess();
        //try to register user with a password
        var userService = new UserService(dataAccess);
        var res = userService.register(new User("cow","rat","john"));
        Assertions.assertNotNull(res);
    }


    @Test
    @DisplayName("registering a user but it returns with an error bad request")
}

package passoff.server;
import dataaccess.MemoryDataAccess;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import service.UserService;
import service.UserServiceTest;

public class ServiceTest {
    @Testpublic void registerNormal(){
        var dataAccess = new MemoryDataAccess();
        //try to register a user without a password
                var userService = new UserService(dataAccess);
        var res = userService.register(new User("cow","rat","john"));
        Assertions.assertNotNull(res);
    }
}

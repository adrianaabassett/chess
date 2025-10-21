package server;
import com.google.gson.Gson;
import dataaccess.DataAccessException;
import dataaccess.MemoryDataAccess;
import model.*;
import service.UserService;
import java.util.UUID;

public class Handler {
    //UserService userService = new UserService;
    MemoryDataAccess memDataAccess = new MemoryDataAccess();
    //it is an Authdata because thats what the register result has

//    public static String generateRandomString(){
//        return UUID.randomUUID().toString();
//    }
//    public AuthData register(User user) throws DataAccessException{
//       if(memDataAccess.getUser(user.getUsername())==null){
//           String authToken = generateRandomString();
//           AuthData authdata = new AuthData(authToken,user.getUsername());
//           memDataAccess.createAuth(authdata);
//           return authdata;
//       }
//        else{
//            throw new DataAccessException("this username already exists");
//       }
//        return null;
//    }
//    public class register(registerRequest regRequest, registerResponse regResponse) throws BadRequestException{
//        User user = new Gson().fromJson(regRequest.)
//    }

    //from server get username, password and email and
    // send register(register request) to service

    //get registerresult from service
    //send username and authtoken to server
}

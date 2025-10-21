//package dataaccess;
//
//import model.AuthData;
//import model.GameData;
//
//import java.util.HashMap;
//
//package dataaccess;
//import model.UserData;
//import model.*;
//
//        import java.util.HashMap;
//
//public class MemoryDataAccess implements DataAccess {
//    private final HashMap<String, AuthData> authentication = new HashMap<>();
//    private final HashMap<String, GameData> games = new HashMap<>();
//
//    public model.UserData getUserFromAuth(AuthData authData)throws DataAccessException{
//        if (authentication.get(authData.username()).authToken() == authData.authToken()){
//            return authentication.get(authData.getUsername());
//        }
//        return null;
//    }
//    @Override
//    public model.UserData getUser(String username) throws DataAccessException {
//        //username is the key
//        return users.get(username);
//        //returns null if theres nothing there
//        //throw new DataAccessException("Theres no user with this name");
//    }
//
//    @Override
//    public void createUser(model.UserData newUser) throws DataAccessException {
//        //try {
//        if(getUser(newUser.getUsername())!=null){
//            users.put(newUser.getUsername(), newUser);
//        }
//        //} catch (DataAccessException exception) {
//        else{
//            throw new DataAccessException("The user you are trying to delete does not exist");
//        }
//        //}
//        return;
//    }
//    public AuthData getAuth(String username){
//        return authentication.get(username);
//    }
//    public void createAuth(AuthData auth) throws DataAccessException{
//        authentication.put(auth.username(),auth);
//    }
//    public void deleteUser(String Username){
//        users.remove(Username);
//    }
//
//    public boolean checkUser(String Username,String password){
//        return(users.get(Username).getPassword() == password);
//    }
//
//    @Override
//    public void clear() {
//        users.clear();
//    }
//}

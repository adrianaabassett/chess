package service;
import chess.ChessGame;
import dataaccess.AuthDAO;
import dataaccess.GameDAO;
import dataaccess.UserDAO;
import dataaccess.exceptions.*;
import model.AuthData;
import model.GameData;
import model.UserData;
import org.mindrot.jbcrypt.BCrypt;
import recordrequests.RegisterRequest;
import recordrequests.RegisterResult;

import java.util.HashMap;
import java.util.List;
import java.util.UUID;

import static org.mindrot.jbcrypt.BCrypt.gensalt;


public class Service {
    UserDAO userDAO;
    AuthDAO authDAO;
    GameDAO gameDAO;
    public Service(UserDAO userDAO, GameDAO gameDAO, AuthDAO authDAO){
        this.userDAO=userDAO;
        this.gameDAO=gameDAO;
        this.authDAO = authDAO;
    }

    private String generateRandomString(){
        return UUID.randomUUID().toString();
    }

    public RegisterResult register(RegisterRequest regReq) throws DataAccessException, BadRequest, AlreadyTakenException {
        if(regReq.username() == null||regReq.password()==null||regReq.email()==null||regReq.password().isEmpty()){
            throw new BadRequest("no username,password, or email");
        }
        else if(userDAO.getUser(regReq.username())==null){
            String authToken = generateRandomString();
            AuthData authdata = new AuthData(authToken,regReq.username());
            authDAO.createAuth(authdata);
//            AuthData auth = authDAO.getAuth(authdata.authToken());
            userDAO.createUser(new UserData(regReq.username(),regReq.password(),regReq.email()));
            RegisterResult regRes = new RegisterResult(regReq.username(), authToken);
            return regRes;
        }
        else{
            throw new AlreadyTakenException("this username already exists");
        }
//      c
//
//

        //server communicaticesto json
        //service has logic and  takes that and puts it into database. This is where the game of chess happens
        // dataaccess is where the data is stored
        //encription is in service since server is just serialization

        //theres a password example up in the phase 4 instrcutction

    }

    public AuthData loginUser(UserData userData) throws DataAccessException, UnauthorizedException, BadRequest, InvalidID {
        if(userData.username() == null|| userData.password()==null){
            throw new BadRequest("error: one of your information spots are blank");
        }
        else if (userDAO.getUser(userData.username())==null){
            throw new DataAccessException("error: this username doesnt work");
        }
        else if (!checkPass(userDAO.getUser(userData.username()).password(), (userData.password()))){//checspass
            throw new UnauthorizedException("error: username and password are incorrect");
        }
//        else if (userDAO.getUser(userData.username())!=null){
//        }
        else{
            String authToken = generateRandomString();
            var authData = new AuthData(authToken, userData.username());
            //this might create a second authdata if its already registered aaaaaaaa
            authDAO.createAuth(authData);
            return authData;
        }
    }
    public boolean checkPass(String encrypted, String clearString){
        return BCrypt.checkpw(clearString, encrypted);
    }
    public void logoutUser(String authToken) throws DataAccessException, UnauthorizedException{
        if(authDAO.getAuth(authToken)==null||authToken.isBlank()){
            throw new UnauthorizedException("Error:unable to log out due to invalid authtoken");
        }
        else if(authDAO.getAuth(authToken)==null){
            throw new DataAccessException("Error:there is no user to log out");
        }
        else{
            authDAO.deleteAuth(authToken);
        }
    }

    public void clear() throws DataAccessException {
        userDAO.clear();
        gameDAO.clear();
        authDAO.clear();
    }

    public GameData  createGame(String gameName, String authToken) throws DataAccessException, UnauthorizedException, BadRequest {
        if(gameName == null || authToken ==null || gameName.isBlank() || authToken.isBlank()){
            throw new BadRequest("no game name or auth token");
        }
        else if (authDAO.getAuth(authToken)==null){
            throw new UnauthorizedException("you cannot create a game if you are not logged in");
        }
        else{
            int gameID = gameDAO.createGame(gameName);
            GameData newGame = new GameData(gameID, null, null,gameName,new ChessGame());
            return newGame;
        }
    }

    public List<GameData> listGames(String authToken) throws DataAccessException, UnauthorizedException{
        if(authDAO.getAuth(authToken)==null){
            throw new UnauthorizedException("this user is not logged in");
        }
        else{
            return gameDAO.listGames();
        }
    }

    public void joinGame(String authToken, String playerColor, Integer gameID)
            throws DataAccessException, InvalidID, BadRequest, UnauthorizedException {
        if(playerColor == null|| gameID==null|| gameDAO.getGame(gameID)==null ){
            throw new BadRequest("bad game idt");
        }
        else if (authToken == null || authDAO.getAuth(authToken) == null ){
            throw new UnauthorizedException("Error: null authToken");
        }
        boolean isNotTaken = gameDAO.getUsername(playerColor,gameID)==null;
        if(!isNotTaken){
            throw new InvalidID("someone already claimed this color");
        }
        else if(!playerColor.equals("WHITE") && !playerColor.equals("BLACK")){
            throw new BadRequest("please only play as black or white");
        }
        else{
            GameData gameData = gameDAO.getGame(gameID);
            String whiteUsername;
            String blackUsername;
            if(playerColor.equals("WHITE")) {
                whiteUsername = authDAO.getAuth(authToken).getUsername();
                blackUsername = gameData.blackUsername();
            }
            else if(playerColor.equals("BLACK")) {
                whiteUsername = gameData.whiteUsername();
                blackUsername = authDAO.getAuth(authToken).getUsername();
            }
            else{
                throw new DataAccessException("wrong color");
            }
            gameDAO.updateGame(new GameData(gameID, whiteUsername,blackUsername,gameData.gameName(),gameData.game()));


        }

    }


}
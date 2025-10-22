package service;
import chess.ChessGame;
import dataaccess.AuthDAO;
import dataaccess.GameDAO;
import dataaccess.UserDAO;
import model.AuthData;
import model.GameData;
import model.UserData;
import dataaccess.exceptions.DataAccessException;
import recordrequests.RegisterRequest;
import recordrequests.RegisterResult;

import java.util.HashMap;
import java.util.UUID;


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
    public RegisterResult register(RegisterRequest regReq) throws DataAccessException{

        if(regReq.username() == null||regReq.password()==null||regReq.email()==null){
            throw new DataAccessException("no username,password, or email");
        }
        if(userDAO.getUser(regReq.username())==null){
            String authToken = generateRandomString();
            AuthData authdata = new AuthData(authToken,regReq.username());
            authDAO.createAuth(authdata);
            userDAO.createUser(new UserData(regReq.username(),regReq.password(),regReq.email()));
            RegisterResult regRes = new RegisterResult(regReq.username(), authToken);
            return regRes;
        }
        else{
            throw new DataAccessException("this username already exists");
        }
    }

    public AuthData loginUser(UserData userData) throws DataAccessException{
        if(userData.username() == null|| userData.password()==null ||userData.email() == null || userDAO.getUser(userData.username())==null){
            throw new DataAccessException("error: one of your information spots are blank");
        }
        else if (userDAO.getUser(userData.username()).password()!=userData.password()){
            throw new DataAccessException("error: username and password are incorrect");
        }
        else{
            String authToken = generateRandomString();
            var authData = new AuthData(authToken, userData.username());
            //this might create a second authdata if its already registered aaaaaaaa
            authDAO.createAuth(authData);
            return authData;
        }
    }

    public void logoutUser(String authToken) throws DataAccessException{
        if(authDAO.getAuth(authToken)==null||authToken.isBlank()||authDAO.getAuth(authToken)==null){
            throw new DataAccessException("Error:unable to log out due to invalid authtoken");
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

    public GameData  createGame(String gameName, String authToken) throws DataAccessException{
        if(gameName == null || authToken ==null || gameName.isBlank() || authToken.isBlank()){
            throw new DataAccessException("no game name or auth token");
        }
        else if (authDAO.getAuth(authToken)==null){
            throw new DataAccessException("incorrect login");
        }
        else{
            int gameID = gameDAO.createGame(gameName);
            GameData newGame = new GameData(gameID, null, null,gameName,new ChessGame());
            return newGame;
        }
    }

    public HashMap<Integer, GameData> listGames(String authToken) throws DataAccessException{
        if(authDAO.getAuth(authToken)==null){
            throw new DataAccessException("this user does not exist");
        }
        else{
            return gameDAO.listGames();
        }
    }

    public void joinGame(String authToken, String playerColor, Integer gameID)throws DataAccessException{
        if(authToken == null|| playerColor == null|| gameID==null|| gameDAO.getGame(gameID)==null || gameDAO.getUsername(playerColor,gameID)!=null){
            throw new DataAccessException("invalid input");
        }
        else{
            GameData gameData = gameDAO.getGame(gameID);
            String whiteUsername;
            String blackUsername;
            if(playerColor=="WHITE") {
                whiteUsername = authDAO.getAuth(authToken).getUsername();
                blackUsername = gameData.blackUsername();
            }
            else if(playerColor=="BLACK") {
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
package service;
import chess.ChessGame;
import dataaccess.AuthDAO;
import dataaccess.GameDAO;
import dataaccess.UserDAO;
import exceptions.*;
import model.AuthData;
import model.GameData;
import model.UserData;
import recordrequests.RegisterRequest;
import recordrequests.RegisterResult;

import java.util.List;
import java.util.UUID;


public class OldService {
    UserDAO userDAO;
    AuthDAO authDAO;
    GameDAO gameDAO;
    public OldService(UserDAO userDAO, GameDAO gameDAO, AuthDAO authDAO){
        this.userDAO=userDAO;
        this.gameDAO=gameDAO;
        this.authDAO = authDAO;
    }

    private String generateRandomString(){
        return UUID.randomUUID().toString();
    }

    public RegisterResult register(RegisterRequest oldRegReq) throws DataAccessException, BadRequest, AlreadyTakenException {
        if(oldRegReq.username() == null||oldRegReq.password()==null||oldRegReq.email()==null||oldRegReq.password().isEmpty()){
            throw new BadRequest("no username,password, or email");
        }
        else if(userDAO.getUser(oldRegReq.username())==null){
            String oldAuthToken = generateRandomString();
            AuthData authdata = new AuthData(oldAuthToken,oldRegReq.username());
            authDAO.createAuth(authdata);
            userDAO.createUser(new UserData(oldRegReq.username(),oldRegReq.password(),oldRegReq.email()));
            RegisterResult regRes = new RegisterResult(oldRegReq.username(), oldAuthToken);
            return regRes;
        }
        else{
            throw new AlreadyTakenException("this username already exists");
        }
    }

    public AuthData loginUser(UserData userData) throws DataAccessException, UnauthorizedException, BadRequest, InvalidID {
        if(userData.username() == null|| userData.password()==null){
            throw new BadRequest("error: old  one of your information spots are blank");
        }
        else if (userDAO.getUser(userData.username())==null){
            throw new DataAccessException("error: old  this username doesnt work");
        }
        else if (!userDAO.getUser(userData.username()).password().equals(userData.password())){
            throw new UnauthorizedException("error: old  username and password are incorrect");
        }
//        else if (userDAO.getUser(userData.username())!=null){
//
//        }
        else{
            String oldAuthToken = generateRandomString();
            var authData = new AuthData(oldAuthToken, userData.username());
            //this might create a second authdata if its already registered aaaaaaaa
            authDAO.createAuth(authData);
            return authData;
        }
    }

    public void logoutUser(String oldAuthToken) throws DataAccessException, UnauthorizedException{
        if(authDAO.getAuth(oldAuthToken)==null||oldAuthToken.isBlank()){
            throw new UnauthorizedException("error: old unable to log out due to invalid oldAuthToken");
        }
        else if(authDAO.getAuth(oldAuthToken)==null){
            throw new DataAccessException("error: old there is no user to log out");
        }
        else{
            authDAO.deleteAuth(oldAuthToken);
        }
    }

    public void clear() throws DataAccessException {
        gameDAO.clear();
        userDAO.clear();
        authDAO.clear();
    }

    public GameData  createGame(String gameName, String oldAuthToken) throws DataAccessException, UnauthorizedException, BadRequest {
        if(gameName == null || oldAuthToken ==null || gameName.isBlank() || oldAuthToken.isBlank()){
            throw new BadRequest("no game name or auth token");
        }
        else if (authDAO.getAuth(oldAuthToken)==null){
            throw new UnauthorizedException("you cannot create a game if you are not logged in");
        }
        else{
            int gameID = gameDAO.createGame(gameName);
            GameData oldNewishGame = new GameData(gameID, null, null,gameName,new ChessGame());
            return oldNewishGame;
        }
    }

    public List<GameData> listGames(String oldAuthToken) throws DataAccessException, UnauthorizedException{
        if(authDAO.getAuth(oldAuthToken)==null){
            throw new UnauthorizedException("this user is not logged in");
        }
        else{
            return gameDAO.listGames();
        }
    }

    public void joinGame(String oldAuthToken, String oldPlayerColor, Integer oldGameID)
            throws DataAccessException, InvalidID, BadRequest, UnauthorizedException {
        if(oldPlayerColor == null|| oldGameID==null|| gameDAO.getGame(oldGameID)==null ){
            throw new BadRequest("old bad game idt");
        }
        else if (oldAuthToken == null || authDAO.getAuth(oldAuthToken) == null ){
            throw new UnauthorizedException("error: old  null oldAuthToken");
        }
        else if(gameDAO.getUsername(oldPlayerColor,oldGameID)!=null){
            throw new InvalidID("old someone already claimed this color");
        }
        else if(!oldPlayerColor.equals("WHITE") && !oldPlayerColor.equals("BLACK")){
            throw new BadRequest("old please only play as black or white");
        }
        else{
            GameData gameData = gameDAO.getGame(oldGameID);
            String whiteUsername;
            String blackUsername;
            if(oldPlayerColor.equals("WHITE")) {
                whiteUsername = authDAO.getAuth(oldAuthToken).getUsername();
                blackUsername = gameData.blackUsername();
            }
            else if(oldPlayerColor.equals("BLACK")) {
                whiteUsername = gameData.whiteUsername();
                blackUsername = authDAO.getAuth(oldAuthToken).getUsername();
            }
            else{
                throw new DataAccessException("wrong color");
            }
            gameDAO.updateGame(new GameData(oldGameID, whiteUsername,blackUsername,gameData.gameName(),gameData.game()));


        }

    }


}
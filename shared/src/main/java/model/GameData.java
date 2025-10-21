package model;
import chess.ChessGame;
import com.google.gson.Gson;

public record GameData(int gameID, String whiteUsername, String blackUsername, String gameName, ChessGame game){
    public GameData setGmeID(int gameID){
        return new GameData(gameID, this.whiteUsername, this.blackUsername, this.gameName, this.game);
    }
    public GameData setGameID(String WhiteUsername){
        return new GameData(this.gameID, WhiteUsername, this.blackUsername, this.gameName, this.game);
    }
    public GameData setWhiteUsername(String WhiteUsername){
        return new GameData(this.gameID, WhiteUsername, this.blackUsername, this.gameName, this.game);
    }
    public GameData setBlackUsername(String BlackUsername){
        return new GameData(this.gameID, this.whiteUsername, BlackUsername, this.gameName, this.game);
    }
    public GameData setGameName(String GameName){
        return new GameData(this.gameID, this.whiteUsername,this.blackUsername, GameName, this.game);
    }
    public GameData getGame(ChessGame Game){
        return new GameData(this.gameID, this.whiteUsername, this.blackUsername, this.gameName, Game);
    }
    public String toString(){
        return new Gson().toJson(this);
    }
}

package model;
import chess.ChessGame;
import com.google.gson.Gson;

public record GameData(int gameID, String whiteUsername, String blackUsername, String gameName, ChessGame game){
//
    public GameData getGame(ChessGame game){
        return new GameData(this.gameID, this.whiteUsername, this.blackUsername, this.gameName, game);
    }
    public String toString(){
        return new Gson().toJson(this);
    }
}

import chess.*;
import exceptions.AlreadyTakenException;
import exceptions.ResponseException;
import ui.Client;

public class Main {
    public static void main(String[] args) throws ResponseException, AlreadyTakenException {
        var piece = new ChessPiece(ChessGame.TeamColor.WHITE, ChessPiece.PieceType.PAWN);
        System.out.println("♕ 240 Chess Client: " + piece);
        Client client = new Client(    "http://localhost:8080");
        client.repl();
    }
}
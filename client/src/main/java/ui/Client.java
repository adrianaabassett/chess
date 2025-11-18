package ui;
import chess.ChessBoard;
import chess.ChessGame;
import chess.ChessPiece;
import chess.ChessPosition;
import client.ServerFacade;
import com.google.gson.Gson;
import dataaccess.exceptions.AlreadyTakenException;
import dataaccess.exceptions.ResponseException;
import model.AuthData;
import model.GameData;
import model.UserData;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import recordrequests.JoinGameRequest;
import recordrequests.RegisterRequest;
import server.Server;

import java.util.ArrayList;
import java.util.Objects;
import java.util.Random;
import java.util.Scanner;

import static java.lang.System.out;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class Client {
    private ServerFacade server;
    private String serverUrl;
    //all the help, create game,
    //test in terminal how
    //repl
    //repl is read eval print loop
//    private static Server server;
//    String serverUrl = "http://localhost:8080";
//
    ServerFacade serverFacade = new ServerFacade();

    public Client(String serverUrl){
      serverFacade = new ServerFacade(serverUrl);
      this.serverUrl = serverUrl;
    }

    boolean hasntQuit = true;
    boolean signedIn = false;
    private String authToken;
    ArrayList<String> games = new ArrayList<>();
    ArrayList<String> gameNames = new ArrayList<>();



    public void repl() throws ResponseException, AlreadyTakenException {
        String input = "";
        while(hasntQuit) {
            if(signedIn){
                out.print("\n[LOGGED_IN] >>> ");
            }
            else{
                out.print("\n[LOGGED_OUT] >>> ");
            }
            Scanner scanner = new Scanner(System.in);
            input = scanner.nextLine();
            //this is where I parse the input to make it readible for my code
//            ServerFacade serverFacade = new ServerFacade(serverUrl);

            String[] inputPieces = input.toLowerCase().split(" ");
            switch (inputPieces[0]) {
                case "help":
                    out.print(toStringHelp());
                    break;
                case "register":
                    if(inputPieces.length>=4){
                    out.print(toStringRegister(inputPieces[1], inputPieces[2],inputPieces[3]));}
                    else{
                        out.print("Error: please include your username, password, and email");
                    }
                    break;


                case "pretend":
                    fakeBoard();
                    break;

            case "login":
                if(inputPieces.length < 3) {
                    out.println("Not enough variables. Please enter your username, password, and email");
                }
                else if(inputPieces.length == 3){
                    out.print(toStringLogin(inputPieces[1], inputPieces[2], ""));

                }
                else{ //assumes the number of variables given is at least 4, including the login
                    out.print(toStringLogin(inputPieces[1], inputPieces[2], inputPieces[3]));
                }
                break;
//                logged in

            case "create":
                out.print(toStringCreate(inputPieces[1]));
                break;

            case "list":
                out.print(toStringList());
                break;

            case "join":
                if(inputPieces.length < 3){
                    out.println("don't forget your gameID and color");

                }
                else {
                    out.println("," + inputPieces[1] + ",");
                    out.println("," +inputPieces[2] + ",");
                    out.print(toStringJoin(inputPieces[1], inputPieces[2]));
                }
                break;

            case "observe":
                out.print(fakeBoard());
                break;

            case "logout":
                out.print(toStringLogout());
                break;

            case "quit":
                hasntQuit = false;
                signedIn = false;
                break;

                case "clear":
                    signedIn = false;
                    serverFacade.clear();
                    break;
            }

        }
    serverFacade.clear();
        //this reads from the input
//this is the ui part
    }//json to md

    private String toStringHelp() {
        String result = "";
        if (!signedIn){
            result = result+"register <USERNAME> <PASSWORD> <EMAIL> - to create an account\n";
            result = result+"login <USERNAME> <PASSWORD> - to play chess\n";
            result = result+"quit - playing chess\n";
            result = result+"help - with possible commands\n";
        }
        else{
            result = result+"create <NAME> - a game\n";
            result = result+"list - games\n";
            result = result+"join <ID> [WHITE|BLACK] - a game\n";
            result = result+"observe <ID> - a game\n";
            result = result+"logout - when you are done\n";
            result = result+"quit - playing chess\n";
            result = result+"help - with possible commands\n";
        }
        return result;
    }

    private String toStringRegister(String name, String pass, String email) throws AlreadyTakenException {
        String result = "";
        try{
            AuthData authData = serverFacade.addUser(new RegisterRequest(name,pass,email));
            authToken = authData.authToken();
            signedIn = true;
        }
        catch(ResponseException e){
            result = result +"unable to register";
        } catch (AlreadyTakenException e) {
            result = result +"Username has already been taken, try again";
        }
        return result;

    }

    private String toStringLogin (String username, String password, String email){
        String result = "";
        try{
            AuthData authData = serverFacade.loginUser(new UserData(username,password,email));
            authToken = authData.authToken();
            result = "\nWelcome, " + username +"\n";
            signedIn = true;
        }catch(ResponseException e){
            result = "Login invalid. Please check your username and password.";
        }catch(AlreadyTakenException e){
            result = "already taken exception from login";
        }
        return result;
    }

    private String toStringCreate (String gameName){
        try{
        String result = "";
        int randomNum = generateRandomNum();
        GameData gameData = new GameData(randomNum, null, null,gameName, new ChessGame());
        serverFacade.createGame(gameData, authToken);
        result = result + gameName + " created, type list to list games";
        games.add(randomNum + "");
        gameNames.add(gameName);
        return result;
        } catch (ResponseException e) {
            return "Can't create a game right now. Please make sure you're logged in";
        } catch (AlreadyTakenException e) {
            return "error creating game";
        }
    }
    private int generateRandomNum(){
        Random random = new Random();
        return random.nextInt(1,1000000);
    }
    private String toStringList (){
        String result = "";
        try{
            result = result + "games:\n";
            int i = 1;
            for(String gameName : gameNames){
                result = result + gameName + " "+ i+ "\n";
                i++;
            }
            serverFacade.listGames(this.authToken);
        }catch(ResponseException e){
            result = "List unavailable";
        }catch(AlreadyTakenException e){
            result = "list already taken exception";
        }
        return result;
    }
    private String toStringJoin (String num, String color){
        String result = "";
        if(color.equals("white") || color.equals("black")) {
            try {
                String[] params;

                if(color.equals("white")){params = new String[]{num,"WHITE"};}
                else{params = new String[]{num,"BLACK"};}
                JoinGameRequest joinGameRequest = new JoinGameRequest(authToken, color.toUpperCase(), Integer.parseInt(num));
                serverFacade.joinGame(joinGameRequest, authToken);
                out.println("Game joined!");
                if (color.equals("white")){out.print(fakeBoard());}
                if (color.equals("black")){out.print(fakeBoardBlack());}
            } catch (ResponseException | AlreadyTakenException e) {
                e.getMessage();
                result = "cannot join game now, please check your input ";
            }
        }
        else{
            result = "the only colors available are white and black";
        }
        return result;

    }
//
//    private String toStringObserve (){
//        String result = "";
//        try{
//
//        }catch(ResponseException e){
//
//        }
//        return result;
//    }
    private String toStringLogout (){
        String result = "";
        try{
            serverFacade.logoutUser(authToken);
            signedIn = false;
        }catch(ResponseException e){
            result = "You cannot log out right now.";
        }catch(AlreadyTakenException e){
            result = "This username has already been taken.";
        }
        return result;
    }

    private String fakeBoard(){
        out.print(boardToString(new ChessGame(), "WHITE"));
        return "";
    }

    private String fakeBoardBlack(){
        out.print(boardToString(new ChessGame(), "BLACK"));
        return "";
    }

    private String boardToString(ChessGame chessGame, String color){
        String greenT = "\u001B[32m";
        String pinkT = "\u001B[38;2;255;192;203m";
        String blackBG = "\u001B[40m";////
        String whiteBG = "\u001B[41m";//
        String pinkBG = "\u001b[105m";
        String blackT = "\u001b[30m";
        String resetC = "\u001b[0m";

        String[] leftRight;
        String[] topDown;
        if(color.equals("WHITE")){
            leftRight = new String[]{"a","b","c","d","e","f","g","h"};
            topDown = new String[]{"8","7","6","5","4","3","2","1"};
        }
        else{//black color
            leftRight = new String[]{"h","g","f","e","d","c","b","a"};
            topDown = new String[]{"1","2","3","4","5","6","7","8"};
        }
        String board = pinkBG + " ";
        ChessBoard chessBoard = chessGame.getBoard();
        ChessPiece currentPiece;
        for(int numm = 0; numm<8; numm++){
            board = board + pinkBG + blackT + leftRight[numm];
        }
        board = board + resetC + " \n" ;
        boolean whiteSquare = false;
        for(int rowNum = 0; rowNum<8; rowNum ++){
            board = board + blackT;
            board = board + pinkBG + topDown[rowNum];
            if(whiteSquare){whiteSquare = false;}
            else{whiteSquare = true;}
            for(int colNum = 0; colNum <8; colNum++){
                if(whiteSquare){
                    board = board + whiteBG;
                    whiteSquare = false;
                }
                else{
                    board = board + blackBG;
                    whiteSquare = true;
                }
                    currentPiece = chessBoard.getPiece(new ChessPosition(Integer.parseInt(topDown[rowNum]), Integer.parseInt(topDown[colNum])));
                if(currentPiece != null){
                    if(currentPiece.getTeamColor() == ChessGame.TeamColor.WHITE){
                        board = board + pinkT;
                    }
                    else{board = board+greenT;}
                }
                if(currentPiece == null){
                    board = board + " ";
                }
                else if (currentPiece.getPieceType() == ChessPiece.PieceType.PAWN){
                    board = board + "P";
                }
                else if (currentPiece.getPieceType() == ChessPiece.PieceType.QUEEN){
                    board = board + "Q";
                }
                else if (currentPiece.getPieceType() == ChessPiece.PieceType.KING){
                    board = board + "K";
                }
                else if (currentPiece.getPieceType() == ChessPiece.PieceType.ROOK){
                    board = board + "R";
                }
                else if (currentPiece.getPieceType() == ChessPiece.PieceType.KNIGHT){
                    board = board + "N";
                }
                else if (currentPiece.getPieceType() == ChessPiece.PieceType.BISHOP){
                    board = board + "B";
                }
            }
            board = board + blackT+ pinkBG + topDown[rowNum] + " \n";
        }
        board = board + blackT+ pinkBG + " ";
        for(int numm = 0; numm<8; numm++){
            board = board + pinkBG + blackT + leftRight[numm] + resetC;
        }
        board = board + " \n";

        return board;
    }

}


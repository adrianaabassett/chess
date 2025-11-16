package ui;
import chess.ChessGame;
import client.ServerFacade;
import dataaccess.exceptions.ResponseException;
import model.AuthData;
import model.GameData;
import model.UserData;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import recordrequests.RegisterRequest;
import server.Server;
import java.util.Random;
import java.util.Scanner;

import static java.lang.System.out;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class Client {
    //all the help, create game,
    //test in terminal how
    //repl
    //repl is read eval print loop

//    private static Server server;
//    String serverUrl = "http://localhost:8080";
//
private static Server server;
    ServerFacade serverFacade = new ServerFacade();

    public Client(String serverUrl){
      serverFacade = new ServerFacade(serverUrl);
    }

    @BeforeAll
    public static void init() {
        server = new Server();
        var port = server.run(8080);
        System.out.println("Started test HTTP server on " + port);
        /////  UserData userData = new Gson().fromJson(ctx.body(),UserData.class);
    }

    @AfterAll
    static void stopServer() {
        server.stop();
    }

    ChessGame chessGame;
    boolean hasntQuit = true;
    boolean signedIn = false;
    String authToken;

//    ServerFacade serverFacade = new ServerFacade(serverUrl);

//    public Client(String serverUrl){
//      this.serverUrl = serverUrl;
//      serverFacade = new ServerFacade(serverUrl);
//    }



    public void repl() throws ResponseException {
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
                    if(inputPieces.length>2){
                    out.print(toStringRegister(inputPieces[1], inputPieces[2],inputPieces[3]));}
                    else{
                        out.print("Error: please include your username, password, and email");
                    }
                    break;
            case "login":
                if(inputPieces.length < 4) {
                    out.println("Not enough variables. Please enter your username, password, and email");
                }
                else {
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
                out.print(toStringJoin(inputPieces[1], inputPieces[2]));
                break;

//            case "observe":
//                out.print(toStringObserve());
//                break;

            case "logout":
                out.print(toStringLogout());
                break;

            case "quit":
                hasntQuit = false;
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

    private String toStringRegister(String name, String pass, String email){
        String result = "";
        try{
            AuthData authData = serverFacade.addUser(new RegisterRequest(name,pass,email));
            authToken = authData.authToken();
            signedIn = true;
        }
        catch(ResponseException e){
            result = result + e.toString() + " "+ e.getCause();
        }
        return result;

    }

    private String toStringLogin (String username, String password, String email){
        String result = "";
        try{
            AuthData authData = serverFacade.loginUser(new UserData(username,password,email));
        }catch(ResponseException e){
            result = "\nWelcome, " + username +"\n";
        }
        return result;
    }

    private String toStringCreate (String gameName){
        String result = "";
        GameData gameData = new GameData(generateRandomNum(), null, null,gameName, new ChessGame());
        return result;
    }
    private int generateRandomNum(){
        Random random = new Random();
        return random.nextInt(1,1000000);
    }
    private String toStringList (){
        String result = "";
        try{
            result = result + serverFacade.listGames(this.authToken);
        }catch(ResponseException e){
            result = "List unavailable";
        }
        return result;
    }
    private String toStringJoin (String color, String num){
        String result = "";
        try{
            String[] colorAndInt = new String[2];
            colorAndInt[0] = color;
            colorAndInt[1]= num;
            serverFacade.joinGame(colorAndInt,authToken);
        }catch(ResponseException e){
            result  = "cannot join game now";
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
        }catch(ResponseException e){
            result = "error";
        }
        return result;
    }

}


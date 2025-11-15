package ui;

import chess.ChessGame;
import client.ServerFacade;
import dataaccess.exceptions.ResponseException;

import static java.lang.System.out;

public class Client {
    //all the help, create game,
    //test in terminal how
    //repl
    //repl is read eval print loop
    final String serverUrl;
    boolean signedIn = false;
    public Client(String serverUrl){
        ServerFacade serverFacade = new ServerFacade(serverUrl);
      this.serverUrl = serverUrl;
    }

    public String repl(String input){
        //this is where I parse the input to make it readible for my code
        ServerFacade serverFacade = new ServerFacade(serverUrl);
        ChessGame chessGame;
        String[] inputPieces = input.toLowerCase().split("");
        switch (inputPieces[0]){
            case "help":
                out.print(toStringHelp());
                break;
            case "register":
                toStringRegister();
                break;
            case "login":
                toStringLogin();
                break;

                //logged in

            case "create":
                toStringCreate();
                break;

            case "list":
                toStringList();
                break;

            case "join":
                toStringJoin();
                break;

            case "observe":
                toStringObserve();

            case "logout":
                toStringLogout();

            case "quit":
                break;
        }


        //this reads from the input
//this is the ui part
return null;
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

    private String toStringRegister(){
        String result =
    }

}


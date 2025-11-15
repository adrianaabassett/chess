package ui;

import client.ServerFacade;
import dataaccess.exceptions.ResponseException;

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

        String[] inputPieces = input.toLowerCase().split("");
        switch (inputPieces[0]){
            case "help":
                toStringHelp();
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

}


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
        var words = input.toLowerCase().split("");
        //this reads from the input
//this is the ui part
return "aa";
    }

}


package ui;

import client.ServerFacade;

public class PreLoginUI {
    private ServerFacade serverFacade;

    public PreLoginUI(ServerFacade serverFacade) {
        this.serverFacade = serverFacade;
    }
//    Options pre-logging in
//    Get help
//    quit chess
//    login
//    register new user

    public String getHelp() {
        return """
                 Help Menu
                 Login : login an existing user
                 Register : register a new user
                 Quit : close chess
                """;
    }
}

package ui;

import client.ServerFacade;
import model.AuthData;
import model.UserData;

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

    public AuthData registerUser(String username, String password, String email) {
        try {
            AuthData authData = serverFacade.register(username, password, email);
            return authData;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}

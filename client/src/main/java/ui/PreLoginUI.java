package ui;

import serverfacade.ServerFacade;
import model.AuthData;

public class PreLoginUI {
    private final ServerFacade serverFacade;

    public PreLoginUI(ServerFacade serverFacade) {
        this.serverFacade = serverFacade;
    }
//    Options pre-logging in
//    Get help
//    quit chess -- not part of this though
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
            return serverFacade.register(username, password, email);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public AuthData login(String username, String password) {
        try {
            return serverFacade.login(username, password);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}

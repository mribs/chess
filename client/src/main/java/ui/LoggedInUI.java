package ui;

import model.AuthData;
import serverfacade.ServerFacade;

public class LoggedInUI {
    private final ServerFacade serverFacade;

    public LoggedInUI(ServerFacade serverFacade) {
        this.serverFacade = serverFacade;
    }

    //    options after logging in
//    help
//    logout
//    create game
//    list games
//    join game
//    observe game
//
    public String getHelp() {
        return """
                 Help Menu
                 Logout : logout of chess
                 Create game : create a new game
                 Join game : join an existing game
                 Observe game : watch an existing game
                 List games : print a list of existing games
                """;
    }

    public String logout(AuthData authData) {
        try {
            serverFacade.logout(authData.authToken());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return "Hurry Back";
    }
}

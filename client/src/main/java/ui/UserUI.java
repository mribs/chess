package ui;

import client.ServerFacade;
import model.AuthData;

public class UserUI {
    private boolean loggedIn;
    ServerFacade serverFacade;
    private AuthData authData;
    private PreLoginUI preLogin;

    public UserUI(String serverUrl) {
        this.loggedIn = false;
        this.serverFacade = new ServerFacade(serverUrl);
        this.authData = null;
    }

    public String getHelp() {
        if (loggedIn) {
//            help menu with authorized things
        }
//        help menu with login options
        return preLogin.getHelp();
    }

    public String register() {
        return null;
    }

    public String login() {
        return null;
    }

    public String logout() {
        return null;
    }

    public String createGame() {
        return null;
    }

    public String listGames() {
        return null;
    }

    public String joinGame() {
        return null;
    }

    public String observeGame() {
        return null;
    }
}

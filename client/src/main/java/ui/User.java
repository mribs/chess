package ui;

import client.ServerFacade;
import model.AuthData;
import model.GameData;

public class User {
    private boolean loggedIn;
    ServerFacade serverFacade;
    private AuthData authData;

    public User(String serverUrl) {
        this.loggedIn = false;
        this.serverFacade = new ServerFacade(serverUrl);
        this.authData = null;
    }

    public String getHelp() {
        if (loggedIn) {
//            help menu with authorized things
        }
//        help menu with login options
        return null;
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

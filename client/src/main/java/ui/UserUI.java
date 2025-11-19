package ui;

import client.ServerFacade;
import model.AuthData;

import java.util.Arrays;
import java.util.Scanner;

public class UserUI {
    private boolean loggedIn;
    ServerFacade serverFacade;
    private AuthData authData;
    private PreLoginUI preLogin;
    private Scanner scanner;

    public UserUI(String serverUrl) {
        this.loggedIn = false;
        this.serverFacade = new ServerFacade(serverUrl);
        this.authData = null;
        this.scanner = new Scanner(System.in);
        this.preLogin = new PreLoginUI(this.serverFacade);
    }

    public String getHelp() {
        if (loggedIn) {
//            help menu with authorized things
        }
//        help menu with login options
        return preLogin.getHelp();
    }

    public String register() {
        String returnString = null;
        try {
            System.out.println("Enter username:");
            String username = scanner.nextLine();
            System.out.println("Enter password:");
            String password = scanner.nextLine();
            System.out.println("Enter email:");
            String email = scanner.nextLine();

            authData = preLogin.registerUser(username, password, email);
            if (authData.authToken() != null) {
                returnString = "Hiya " + authData.username() + "!";
                loggedIn = true;
            }
        } catch (Exception e) {
            returnString = "Couldn't register user";
            loggedIn = false;
        }
        return returnString;
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

    public String invalidResponse() {
        return "Invalid input\n" + getHelp();
    }

    public String evalLine(String line) {
        try {
            var tokens = line.toLowerCase().split(" ");
            var cmd = (tokens.length > 0) ? tokens[0] : "help";
            var params = Arrays.copyOfRange(tokens, 1, tokens.length);
            if (!loggedIn) {
                return switch (cmd) {
                    case "quit" -> "quit";
                    case "register" -> register();
                    case "login" -> login();
                    case "help" -> getHelp();
                    default -> invalidResponse();
                };
            }
//            if logged in
            else {
                return switch (cmd) {
                    case "logout" -> logout();
                    case "create" -> createGame();
                    case "list" -> listGames();
                    case "join" -> joinGame();
                    case "observe" -> observeGame();
                    case "help" -> getHelp();
                    default -> invalidResponse();
                };
            }
        } catch (Throwable e) {
            return e.getMessage();
        }
    }
}

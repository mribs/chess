package ui;

import model.GameData;
import serverfacade.ServerFacade;
import model.AuthData;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Scanner;

public class UserUI {
    private boolean loggedIn;
    ServerFacade serverFacade;
    private AuthData authData;
    private PreLoginUI preLogin;
    private LoggedInUI postLogin;
    private Scanner scanner;

    public UserUI(String serverUrl) {
        this.loggedIn = false;
        this.serverFacade = new ServerFacade(serverUrl);
        this.authData = null;
        this.scanner = new Scanner(System.in);
        this.preLogin = new PreLoginUI(this.serverFacade);
        this.postLogin = new LoggedInUI(this.serverFacade);
    }

    public String getHelp() {
        if (loggedIn) {
            return postLogin.getHelp();
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
        String returnString = null;
        try {
            System.out.println("Enter username:");
            String username = scanner.nextLine();
            System.out.println("Enter password:");
            String password = scanner.nextLine();

            authData = preLogin.login(username, password);
            if (authData.authToken() != null) {
                returnString = "Hiya " + authData.username() + "!";
                loggedIn = true;
            }
        } catch (Exception e) {
            returnString = "Failed to Login";
            loggedIn = false;
        }
        return returnString;
    }

    public String logout() {
        String returnString = null;
        try {
            returnString = postLogin.logout(authData);
        } catch (Exception e) {
            returnString = "You're stuck here (couldn't log out)";
        }
        loggedIn = false;
        return returnString;
    }

    public String createGame() {
        System.out.println("Enter game name:");
        String gameName = scanner.nextLine();

        String returnString = null;
        try {
            int gameID = postLogin.createGame(authData.authToken(), gameName);
            returnString = gameName + " has been created. The game ID is: " + gameID;
        } catch (Exception e) {
            returnString = "There was a problem making your game";
        }
        return returnString;
    }

    public String listGames() {
        StringBuilder returnString = new StringBuilder();
        try {
            Collection<GameData> gameList = postLogin.listGames(authData.authToken());
            int index = 1;
            if (gameList == null || gameList.isEmpty()) {
                return "Be the first to create a game";
            }
            for (GameData game : gameList) {
                String gameString = index + ": " + game.gameName() + "\n    White username: " + game.whiteUsername()
                        + " Black username: " + game.blackUsername() + "\n";
                returnString.append(gameString);
                index += 1;
            }
        } catch (Exception e) {
            returnString.append("There was a problem making your game");
        }
        return returnString.toString();
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

package ui;

import model.GameData;
import serverfacade.ServerFacade;
import model.AuthData;

import java.util.List;
import java.util.Scanner;

public class UserUI {
    private boolean loggedIn;
    ServerFacade serverFacade;
    private AuthData authData;
    private final PreLoginUI preLogin;
    private final LoggedInUI postLogin;
    private final Scanner scanner;
    private List<GameData> gameList;

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
        String returnString;
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

        String returnString;
        try {
            int gameID = postLogin.createGame(authData.authToken(), gameName);
            returnString = gameName + " has been created.";
        } catch (Exception e) {
            returnString = "There was a problem making your game";
        }
        return returnString;
    }

    public String listGames() {
        StringBuilder returnString = new StringBuilder();
        try {
            gameList = postLogin.listGames(authData.authToken());
            int index = 1;
            if (gameList == null || gameList.isEmpty()) {
                return "Be the first to create a game";
            }
            for (GameData game : gameList) {
                String whiteUsername = game.whiteUsername();
                String blackUsername = game.blackUsername();
                if (whiteUsername == null) {
                    whiteUsername = "available";
                }
                if (blackUsername == null) {
                    blackUsername = "available";
                }
                String gameString = index + ": " + game.gameName() + "\n    White username: " + whiteUsername
                        + " Black username: " + blackUsername + "\n";
                if (game.game().isOver()) {
                    gameString = index + ": " + game.gameName() + "\n    GAME OVER \n";
                }
                returnString.append(gameString);
                index += 1;
            }
        } catch (Exception e) {
            returnString.append("There was a problem making your game");
        }
        return returnString.toString();
    }

    public String joinGame() {
        System.out.println("Enter game number (from list):");
        String gameNumberString = scanner.nextLine();
        System.out.println("Which (available) team? w/b:");
        String playerColor = scanner.nextLine();
        playerColor = playerColor.toUpperCase();
        switch (playerColor) {
            case "W", "WHITE" -> playerColor = "WHITE";
            case "B", "BLACK" -> playerColor = "BLACK";
            default -> {
                return "invalid team color";
            }
        }

        int gameID;
        try {
            gameID = findGameID(gameNumberString);
        } catch (Exception e) {
            return "Invalid game number";
        }
        GameData gameData;
        try {
            gameData = postLogin.joinGame(gameID, playerColor, this.authData);
        } catch (Exception e) {
            return "failed to join game";
        }
        if (gameData != null) {
//            temporay(phase 5) just print out the board from color perspective
            GameUI gameUI = new GameUI(gameData, authData, playerColor);
            gameUI.run();
        } else {
            return "failed to join game";
        }
        return "Exited gameplay";
    }

    private int findGameID(String gameNumber) throws Exception {
        int gameNumberInt = Integer.parseInt(gameNumber);
        if (gameList != null && gameNumberInt <= gameList.size() && gameNumberInt > 0) {
            return gameList.get(gameNumberInt - 1).gameID();
        } else {
            throw new Exception("Invalid game number");
        }
    }

    public String observeGame() {
        System.out.println("Enter game number (from list):");
        String gameNumberString = scanner.nextLine();
        int gameID;
        try {
            gameID = findGameID(gameNumberString);
        } catch (Exception e) {
            return "Invalid game number";
        }
        GameData gameData = postLogin.joinGame(gameID, "OBSERVE", authData);
        if (gameData != null) {
            GameUI gameUI = new GameUI(gameData, authData, "OBSERVE");
            gameUI.run();
        } else {
            return "failed to join game";
        }
        return "Exited gameplay";
    }

    public String invalidResponse() {
        return "Invalid input\n" + getHelp();
    }

    public String evalLine(String line) {
        try {
            var tokens = line.toLowerCase().split(" ");
            var cmd = (tokens.length > 0) ? tokens[0] : "help";
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

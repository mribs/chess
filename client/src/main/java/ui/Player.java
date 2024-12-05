package ui;

import chess.ChessGame;
import model.*;
import server.ServerFacade;

import java.util.Arrays;
import java.util.Scanner;

public class Player {
  private Boolean loggedIn;
  private PreLogin preLogin;
  private PostLogin postLogin;
  private Scanner scanner;
  private ServerFacade serverFacade;
  private AuthToken authToken;
  private Game[] gameList;
  private GameBoard gameboard;

  public Player(String serverUrl) {
    this.serverFacade = new ServerFacade(serverUrl);
    this.loggedIn = false;
    this.preLogin = new PreLogin(serverFacade);
    this.postLogin = new PostLogin(serverFacade);
    this.scanner = new Scanner(System.in);
    this.authToken = null;
    this.gameboard = new GameBoard();

  }

  public String help() {
    if (loggedIn) {
      return postLogin.helpMenu();
    }
    return preLogin.helpMenu();
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

      authToken = preLogin.register(username, password, email);
      if (authToken.getAuthToken() != null) {
        returnString = ("Welcome to Chess, " + authToken.getUsername() + "!");
        loggedIn = true;
      }
    } catch (Exception e) {
      returnString = ("Couldn't register user");
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

      authToken = preLogin.login(username, password);
      if (authToken.getAuthToken() != null) {
        returnString = ("Welcome to Chess, " + authToken.getUsername() + "!");
        loggedIn = true;
      }
    } catch (Exception e) {
      returnString = ("Login failed");
      loggedIn = false;
    }
    return returnString;
  }

  private String logout() {
    String returnString = null;
    try {
      postLogin.logout(authToken);
      returnString = "Successfully logged out!";
      loggedIn = false;
    } catch (Exception e) {
      returnString = "Failed to log out :(";
    }
    return returnString;
  }

  private String createGame() {
    try {
      System.out.println("Enter game name:");
      String gameName = scanner.nextLine();
      return postLogin.createGame(authToken, gameName);
    } catch (Exception e) {
      return "error creating game :( ";
    }
  }

  private String listGames() {
    try {
      Game[] games =  postLogin.listGames(authToken);
      StringBuilder returnString = new StringBuilder();
      int indexPlusOne = 1;
      if (games == null || games.length == 0) {
        return "No games to list";
      }
      for (Game game : games) {
        System.out.println(indexPlusOne + ":\n Game Name: " + game.getGameName() +
                ", White Player: " + game.getWhiteUsername() + ", Black Player: " + game.getBlackUsername());
        indexPlusOne++;
      }
      this.gameList = games;
      return "";
    }catch (Exception e) {
      return "Could not list games :( ";
    }
  }

  private String joinGame() {
    int gameNum;
    int gameID;
    System.out.println("Enter game number:");
    String stringGameID = scanner.nextLine();
    System.out.println("Which team? (white/black):");
    String color = scanner.nextLine();
    try {
      gameNum=Integer.parseInt(stringGameID);
    } catch (NumberFormatException e) {
      return "Invalid game number";
    }

    if (gameList != null && gameNum <= gameList.length && gameNum > 0) {
      gameID=gameList[gameNum - 1].getGameID();
    } else {
      return "Invalid game number";
    }
    if (gameList != null && gameID <= gameList.length) {
      gameID = gameList[gameID-1].getGameID();
    }
    ChessGame joined = postLogin.joinGame(gameID, color, authToken);
    if (joined != null) {
      return gameboard.startGame(joined, color);
    }
    else {
      return "failed to join game";
    }
  }
  private String observeGame() {
    int gameID=0;
    System.out.println("Enter game number:");
    String stringGameID=scanner.nextLine();
    int gameNum;
    try {
      gameNum=Integer.parseInt(stringGameID);
    } catch (NumberFormatException e) {
      return "Invalid game number";
    }

    if (gameList != null && gameNum <= gameList.length && gameNum > 0) {
      gameID=gameList[gameNum - 1].getGameID();
    } else {
      return "Invalid game number";
    }
    ChessGame joined=postLogin.joinGame(gameID, "OBSERVE", authToken);
    if (joined != null) {
      return gameboard.startGame(joined, null);
    } else {
      return "failed to observe game";
    }
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
          case "help" -> help();
          default -> invalid();
        };
      }
      // if we are logged in
      else {
        return switch (cmd) {
          case "logout" -> logout();
          case "create" -> createGame();
          case "list" -> listGames();
          case "join" -> joinGame();
          case "observe" -> observeGame();
          case "help" -> help();
          default -> invalid();
        };
      }
    } catch (Throwable e) {
      return e.getMessage();
    }
  }

  private String invalid() {
    return "Invalid option\n" + help();
  }

}

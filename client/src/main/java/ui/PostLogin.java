package ui;

import chess.ChessGame;
import exceptions.DataAccessException;
import model.*;
import server.ServerFacade;

public class PostLogin {
  private ServerFacade serverFacade;

  public PostLogin(ServerFacade serverFacade) {
    this.serverFacade=serverFacade;
  }
  public String helpMenu() {
    String help ="""
            Help Menu
            Create : Create a new game
            List : List the existing games
            Join : Join an existing game as a player
            Observe : Join an existing game as an observer
            Logout : Logout
            Help : Show this menu
            """;
    return help;
  }

  public void logout(AuthToken authToken) {
    try {
      serverFacade.logout(authToken.getAuthToken());
    } catch (Exception e) {
      throw new RuntimeException(e);
    }
  }

  public String createGame(AuthToken authToken, String gameName) {
    int gameID = 0;
    String returnString;
    try {
      gameID=serverFacade.createGame(authToken.getAuthToken(), gameName);
      returnString =gameName + " successfully created!";
    } catch (DataAccessException e) {
      returnString = "Could not create the game :(";
    }

    return returnString;
  }
  public Game[] listGames(AuthToken authToken) {
    Game[] games = null;
    try {
      games = serverFacade.listGames(authToken.getAuthToken());
    } catch (Exception e) {
      System.out.println( "Could not list games :(");
    }
    return games;
  }

  public ChessGame joinGame(int gameID, String color, AuthToken authToken) {
    try {
      ChessGame joined = serverFacade.joinGame(gameID, color, authToken.getUsername(), authToken.getAuthToken());
      return joined;
    } catch (Exception e) {
      System.out.println("Couldn't join game :(");
    }
    return null;
  }

}

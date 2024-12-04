package model;

import chess.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class Game {
  int gameID;
  String whiteUsername;
  String blackUsername;
  List<String> observers;
  String gameName;
  ChessGame game;

  public Game(String gameName) {
    this.gameName=gameName;

    this.game = new chess.ChessGame();

    this.gameID = this.game.hashCode();

  }

  public Game(int gameID, String whiteUsername, String blackUsername, String gameName, ChessGame game) {
    this.gameID=gameID;
    this.whiteUsername=whiteUsername;
    this.blackUsername=blackUsername;
    this.gameName=gameName;
    this.game=game;
  }
  //getters and setters

  public int getGameID() {
    return gameID;
  }

  public String getWhiteUsername() {
    return whiteUsername;
  }

  public void setWhiteUsername(String whiteUsername) {
    this.whiteUsername=whiteUsername;
  }

  public String getBlackUsername() {
    return blackUsername;
  }

  public void setBlackUsername(String blackUsername) {
    this.blackUsername=blackUsername;
  }

  public ChessGame getGame() {
    return game;
  }

  public void setGame(ChessGame game) {
    this.game=game;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (!(o instanceof Game game1)) {
      return false;
    }
    return gameID == game1.gameID && Objects.equals(whiteUsername, game1.whiteUsername) && Objects.equals(blackUsername,
            game1.blackUsername) && Objects.equals(gameName, game1.gameName);
  }

  public Object getGameName() {
    return gameName;
  }
}
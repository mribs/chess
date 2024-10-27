package dataAccess.models;

import chess.*;

import java.util.ArrayList;
import java.util.List;

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

    this.observers = new ArrayList<>();
  }

  //getters and setters

  public List<String> getObservers() {
    return observers;
  }

  public void addObserver(String observer) {
    this.observers.add(observer);
  }

  public int getGameID() {
    return gameID;
  }

  public void setGameID(int gameID) {
    this.gameID=gameID;
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

  public String getGameName() {
    return gameName;
  }

  public void setGameName(String gameName) {
    this.gameName=gameName;
  }

  public ChessGame getGame() {
    return game;
  }

  public void setGame(ChessGame game) {
    this.game=game;
  }
}

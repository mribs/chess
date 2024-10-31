package model;

import chess.ChessGame;

public record Game(int gameID, String whiteUsername, String blackUsername, String gameName, ChessGame game) {
  public Game(String gameName, ChessGame game) {
    this(game.hashCode(), null, null, gameName, game);

  }

  public Integer getGameID() {
    return gameID;
  }
}



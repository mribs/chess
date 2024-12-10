package websocket.commands;

import chess.ChessGame;

public class JoinCommand extends UserGameCommand{
  int gameID;
  ChessGame.TeamColor playerColor;

  public JoinCommand(String authToken, int gameID, ChessGame.TeamColor playerColor) {
    super(CommandType.CONNECT, authToken, gameID);
    this.playerColor = playerColor;
  }

  public ChessGame.TeamColor getColor() {
    return playerColor;
  }
}

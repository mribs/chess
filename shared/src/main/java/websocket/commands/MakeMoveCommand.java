package websocket.commands;

import chess.ChessGame;
import chess.ChessMove;

public class MakeMoveCommand extends UserGameCommand{
   public ChessMove move;
   public ChessGame.TeamColor teamColor;
  public MakeMoveCommand(CommandType commandType, String authToken, Integer gameID, ChessMove move) {
    super(commandType, authToken, gameID);
    this.move = move;
    this.teamColor = teamColor;
  }
}

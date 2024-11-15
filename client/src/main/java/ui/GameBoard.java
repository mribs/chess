package ui;

import chess.*;

public class GameBoard {
  private ChessGame game;
  private ChessBoard board;

  public GameBoard() {
    this.game = new ChessGame();
    this.board = game.getBoard();
  }

  public String startGame(ChessGame game, String playerColor) {
    this.game = game;
    this.board = game.getBoard();
    board.resetBoard();

    //TODO UPDATE FOR GAMEPLAY
    fancyPrint("white");
    System.out.println();
    fancyPrint("black");

    return ("gameplay is not enabled now");
  }

  public void fancyPrint(String color) {
    Boolean reverse = false;
    if (color != null) color = color.toLowerCase();
    if ("black".equals(color)) reverse = true;
    for (int i = 1; i <= 8; i++) {
      int row = reverse ? i : 8 - i + 1;
      for (int j = 1; j <= 8; j++) {
        int col = reverse ? 8 - j + 1 : j;
        System.out.print(EscapeSequences.moveCursorToLocation(col * 5, (reverse ? 8 - row + 2 : row + 1)));

        boolean isWhiteSquare = (row + col) % 2 == 0;
        String backgroundColor = isWhiteSquare ? EscapeSequences.SET_BG_COLOR_WHITE : EscapeSequences.SET_BG_COLOR_BLACK;
        String textColor = EscapeSequences.SET_TEXT_COLOR_MAGENTA;

        System.out.print(backgroundColor + textColor);

        ChessPiece piece = board.getPiece(new ChessPosition(row, col));
        if (piece == null)
          System.out.print(EscapeSequences.EMPTY);
        else if (piece.getTeamColor().equals(ChessGame.TeamColor.WHITE))
          System.out.print(printWhitePiece(piece));
        else
          System.out.print(printBlackPiece(piece));

        System.out.print(EscapeSequences.RESET_BG_COLOR + EscapeSequences.RESET_TEXT_COLOR);
      }
      System.out.println(); // Move to the next line for the next row
    }
    System.out.print(EscapeSequences.RESET_TEXT_COLOR);
  }

  private String printWhitePiece(ChessPiece piece) {
    switch (piece.getPieceType()) {
      case PAWN: return EscapeSequences.WHITE_PAWN;
      case ROOK: return EscapeSequences.WHITE_ROOK;
      case KNIGHT: return EscapeSequences.WHITE_KNIGHT;
      case BISHOP: return EscapeSequences.WHITE_BISHOP;
      case QUEEN: return EscapeSequences.WHITE_QUEEN;
      case KING: return EscapeSequences.WHITE_KING;
    }
    return null;
  }

  private String printBlackPiece(ChessPiece piece) {
    switch (piece.getPieceType()) {
      case PAWN: return EscapeSequences.BLACK_PAWN;
      case ROOK: return EscapeSequences.BLACK_ROOK;
      case KNIGHT: return EscapeSequences.BLACK_KNIGHT;
      case BISHOP: return EscapeSequences.BLACK_BISHOP;
      case QUEEN: return EscapeSequences.BLACK_QUEEN;
      case KING: return EscapeSequences.BLACK_KING;
    }
    return null;
  }
}

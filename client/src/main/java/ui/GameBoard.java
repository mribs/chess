package ui;

import chess.*;

import java.util.ArrayList;

public class GameBoard {
  ChessGame game;
  Player player;
  int gameID;

  public GameBoard() {
    this.game = new ChessGame();
  }

  public void setGame(ChessGame game) {
    this.game = game;
  }


  public String startGame(ChessGame game, String playerColor, Player player, int gameID) {
    this.game = game;
    this.player = player;
    this.gameID = gameID;
    game.gameBoard.resetBoard();

    fancyPrint(playerColor, null, null);
    try {
      new GamePlayUI(this,playerColor, player).run();
    } catch (Exception e) {
      System.out.println(e.getStackTrace());
    }

    return ("exited game play");
  }
  public void fancyPrint(String color, ArrayList<ChessPosition> highlightSquares, ChessPosition highlightPiece) {
    boolean reverse = false;
    if (color != null) {
      color = color.toLowerCase();
    }
    if ("black".equals(color)) {
      reverse = true;
    }

    System.out.println();
    System.out.print(EscapeSequences.RESET_TEXT_COLOR);

    // Print column labels (letters)
    System.out.print("    "); // Indent for row numbers
    for (int j = 1; j <= 8; j++) {
      char colLabel = (char) ('A' + (reverse ? 8 - j : j - 1));
      System.out.print(colLabel + "  "); // Adjust spacing as needed
    }
    System.out.println();

    for (int i = 1; i <= 8; i++) {
      int row = reverse ? i : 8 - i + 1;

      // Print row label (numbers)
      System.out.print(" " + row + " "); // Adjust spacing as needed

      for (int j = 1; j <= 8; j++) {
        int col = reverse ? 8 - j + 1 : j;
        System.out.print(EscapeSequences.moveCursorToLocation(col * 5, (reverse ? 8 - row + 2 : row + 1)));

        boolean isWhiteSquare = (row + col) % 2 == 1;
        String backgroundColor = isWhiteSquare ? EscapeSequences.SET_BG_COLOR_WHITE : EscapeSequences.SET_BG_COLOR_BLACK;
        String textColor = EscapeSequences.SET_TEXT_COLOR_MAGENTA;

        if (highlightSquares != null && highlightSquares.contains(new ChessPosition(row, col))) {
          backgroundColor = EscapeSequences.SET_BG_COLOR_GREEN;
        }
        if (highlightPiece != null && highlightPiece.equals(new ChessPosition(row, col))) {
          backgroundColor = EscapeSequences.SET_BG_COLOR_YELLOW;
        }

        System.out.print(backgroundColor + textColor);

        ChessPiece piece = game.gameBoard.getPiece(new ChessPosition(row, col));
        if (piece == null) {
          System.out.print(EscapeSequences.EMPTY);
        }
        else if (piece.getTeamColor().equals(ChessGame.TeamColor.WHITE)) {
          System.out.print(printWhitePiece(piece));
        }
        else {
          System.out.print(printBlackPiece(piece));
        }

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

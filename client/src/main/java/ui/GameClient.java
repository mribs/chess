package ui;

import chess.ChessMove;
import chess.ChessPosition;
import chess.InvalidMoveException;
import com.sun.nio.sctp.NotificationHandler;
import model.DataAccessException;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Scanner;

public class GameClient {
  private Scanner scanner;
//  private WebsocketFacade facade;
  private GameBoard gameBoard;
  private String playerColor;
  private NotificationHandler notificationHandler;
  private Player playerInfo;

  public GameClient(GameBoard gameBoard, String playerColor, Player player) throws DataAccessException {
    this.gameBoard=gameBoard;
    this.scanner = new Scanner(System.in);
//    this.facade = new WebsocketFacade(serverURL);
    this.playerColor = playerColor;
    this.playerInfo = player;
  }



  public String help() {
    String help ="""
            Help Menu
            Redraw : Redraws current game board
            Highlight : Highlight legal moves
            Move : Make a move (if it's your turn)
            Leave : Leave game without ending it
            Resign : AKA rage quit
            Help : Show this menu
            
            """;
    if (playerColor == null) {
      help ="""
            Help Menu
            Redraw : Redraws current game board
            Highlight : Highlight legal moves for a piece of your choice
            Leave : Exit game
            Help : Show this menu
    
            """;
    }
    return help;
  }

  private String redraw() {
    gameBoard.fancyPrint(playerColor, null, null);
    return "board redrawn";
  }
  private String highlightMoves() {
    System.out.println("Enter piece X position (1-8):");
    String pieceX = scanner.nextLine();
    System.out.println("Enter piece Y position (1-8):");
    String pieceY = scanner.nextLine();

    int col = Integer.parseInt(pieceX);
    int row = Integer.parseInt(pieceY);

    if (gameBoard.board.getPiece(new ChessPosition(row, col)) == null) {
      return "No piece at given position";
    }
    Collection<ChessMove> validMoves = gameBoard.game.validMoves(new ChessPosition(row, col));
    if (validMoves == null || validMoves.isEmpty()) {
      return "No valid moves for that piece";
    }

    ArrayList<ChessPosition> highlightSquares = new ArrayList<>();
    for (ChessMove move : validMoves) {
      highlightSquares.add(move.getEndPosition());
    }
    gameBoard.fancyPrint(playerColor, highlightSquares, new ChessPosition(row, col));
    return "Valid moves highlighted";
  }
  private String makeMove() throws InvalidMoveException {
    System.out.println("Enter piece X position (1-8):");
    String pieceX = scanner.nextLine();
    System.out.println("Enter piece Y position (1-8):");
    String pieceY = scanner.nextLine();

    int col = Integer.parseInt(pieceX);
    int row = Integer.parseInt(pieceY);
    ChessPosition start = new ChessPosition(row, col);

    System.out.println("Enter goal X position (1-8):");
    String pieceXF = scanner.nextLine();
    System.out.println("Enter goal Y position (1-8):");
    String pieceYF = scanner.nextLine();

    int colF = Integer.parseInt(pieceXF);
    int rowF = Integer.parseInt(pieceYF);
    ChessPosition end = new ChessPosition(rowF, colF);

    gameBoard.game.makeMove(new ChessMove(start, end, null));

    redraw();
    return "move made";
  }

  private String leave() {

    return "quit";
  }

  public String evalLine(String line) {
    try {
      var tokens = line.toLowerCase().split(" ");
      var cmd = (tokens.length > 0) ? tokens[0] : "help";
      var params = Arrays.copyOfRange(tokens, 1, tokens.length);
      return switch (cmd) {
        case "quit" -> "quit";
        case "help" -> help();
        case "redraw" -> redraw();
        case "highlight" -> highlightMoves();
        case "leave" -> leave();
        case "move" -> makeMove();
        default -> invalid();
      };
    } catch (Throwable e) {
      return e.getMessage();
    }
  }

  private String invalid() {
    return "Invalid option\n" + help();
  }
}

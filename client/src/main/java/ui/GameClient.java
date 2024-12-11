package ui;

import chess.*;
import exceptions.DataAccessException;
import server.ServerFacade;
import websocket.NotificationHandler;
import websocket.WebSocketClient;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Scanner;

public class GameClient {
  private Scanner scanner;
  private GameBoard gameBoard;
  private String playerColor;
  private Player playerInfo;
  private WebSocketClient wsFacade;

  public GameClient(GameBoard gameBoard, String playerColor, Player player, NotificationHandler notificationHandler) throws Exception {
    this.gameBoard=gameBoard;
    this.scanner = new Scanner(System.in);
    this.wsFacade = new WebSocketClient(notificationHandler);
    this.playerColor = playerColor;
    this.playerInfo = player;

    wsFacade.joinGame(playerInfo.getAuthToken(), gameBoard.gameID, playerColor);
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
    System.out.println("Enter piece letter (a-h):");
    String pieceX = scanner.nextLine().toLowerCase();
    System.out.println("Enter piece number (1-8):");
    String pieceY = scanner.nextLine();

    int col = pieceX.charAt(0) - 'a' + 1;
    int row = Integer.parseInt(pieceY);
    ChessPosition start = new ChessPosition(row, col);

    if (gameBoard.game.gameBoard.getPiece(new ChessPosition(row, col)) == null) {
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
    System.out.println("Enter start letter (a-h):");
    String pieceX = scanner.nextLine().toLowerCase();
    System.out.println("Enter start number (1-8):");
    String pieceY = scanner.nextLine();

    int col = pieceX.charAt(0) - 'a' + 1; // Convert letter to column number
    int row = Integer.parseInt(pieceY);
    ChessPosition start = new ChessPosition(row, col);

    System.out.println("Enter goal letter (a-h):");
    String pieceXF = scanner.nextLine();
    System.out.println("Enter goal number (1-8):");
    String pieceYF = scanner.nextLine();

    int colF = pieceXF.charAt(0) - 'a' + 1; // Convert letter to column number
    int rowF = Integer.parseInt(pieceYF);
    ChessPosition end = new ChessPosition(rowF, colF);

    ChessMove move = new ChessMove(start, end, null);
    //websockets
    try {
      wsFacade.makeMove(playerInfo.getAuthToken(), gameBoard.gameID, move);
      return "";
    } catch (DataAccessException e) {
      throw new InvalidMoveException("Invalid move");
    }
  }

  private String leave(){
    try {
      wsFacade.leave(playerInfo.getAuthToken(), gameBoard.gameID);
      return "quit";
    } catch (DataAccessException e) {
      return "could not leave game";
    }
  }

  private String resign(){
    try {
      wsFacade.resign(playerInfo.getAuthToken(), gameBoard.gameID);
      if (playerColor == null || playerColor == "observer") {
        return"";
      }
      return "quit";
    } catch (DataAccessException e) {
      return "Cant resign";
    }
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
        case "resign" -> resign();
        default -> invalid();
      };
    } catch (Throwable e) {
      return e.getMessage();
    }
  }

  public void updateGame(ChessGame game) {
    this.gameBoard.setGame(game);
    gameBoard.fancyPrint(playerColor, null, null);
  }

  private String invalid() {
    return "Invalid option\n" + help();
  }
}

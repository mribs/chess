package ui;

import model.AuthData;
import model.GameData;
import chess.*;
import websocket.NotificationHandler;
import websocket.WebSocketClient;
import websocket.messages.ServerMessage;

import java.util.*;

public class GameUI implements NotificationHandler {
    GameData gameData;
    AuthData authData;
    String playerColor;
    ChessGame chessGame;
    ChessBoard board;
    WebSocketClient webSocketClient;
    private Scanner scanner;

    public GameUI(GameData gameData, AuthData authData, String playerColor) {
        this.gameData = gameData;
        this.authData = authData;
        this.playerColor = playerColor;
        this.chessGame = gameData.game();
        this.board = gameData.game().getBoard();
        try {
            this.webSocketClient = new WebSocketClient(this);
        } catch (Exception e) {
            System.out.println("Error Connecting");
        }


    }

    public void run() {
        try {
            webSocketClient.joinGame(authData.authToken(), gameData.gameID(), playerColor);
        } catch (Exception e) {
            System.out.println("Error joining game");
            return;
        }
        System.out.println("Welcome to " + gameData.gameName() + "!\n" +
                "And may the odds be ever in your favor");

        fancyPrint(playerColor, null, null);
        System.out.println(getHelp());

        scanner = new Scanner(System.in);
        var result = "";
        while (!result.equals("quit")) {
            System.out.println(EscapeSequences.SET_TEXT_COLOR_WHITE + "Enter Option >>");
            String line = scanner.nextLine();

            try {
                result = evalLine(line);
                String printResult = result;
                if (result.equals("quit")) {
                    printResult = "Leaving game...";
                }
                System.out.println((EscapeSequences.SET_TEXT_COLOR_BLUE + printResult));
            } catch (Throwable e) {
                var msg = e.toString();
                System.out.println(msg);
            }
        }
        System.out.println();
    }

    private String redrawBoard() {
        fancyPrint(playerColor, null, null);
        return "Board redrawn";
    }

    private String highlightMoves() {
        if (chessGame.isOver()) {
            return (gameData.gameName() + " is over. No legal moves.");
        }
        ChessPosition start = getInputPosition("piece");

        if (board.getPiece(start) == null) {
            return "No piece at given position";
        }
        Collection<ChessMove> validMoves = chessGame.validMoves(start);
        if (validMoves == null || validMoves.isEmpty()) {
            return "No valid moves for that piece";
        }

        ArrayList<ChessPosition> highlightSquares = new ArrayList<>();
        for (ChessMove move : validMoves) {
            highlightSquares.add(move.getEndPosition());
        }
        fancyPrint(playerColor, highlightSquares, start);
        return "Valid moves highlighted";
    }

    private String leave() {
        try {
            webSocketClient.leaveGame(authData.authToken(), gameData.gameID());
            return "quit";
        } catch (Exception e) {
            return "Could not leave game. Guess you're stuck here buddy";
        }
    }

    private String makeMove() {
        if (chessGame.isOver()) {
            return (gameData.gameName() + " is over.");
        }
        ChessPosition start = getInputPosition("start");
        ChessPosition end = getInputPosition("end");
        ChessPiece.PieceType promotionPiece = null;
        Collection<ChessMove> validMoves = chessGame.validMoves(start);
        if (!validMoves.isEmpty()) {
            for (ChessMove move : validMoves) {
                if (move.getPromotionPiece() != null) {
                    promotionPiece = getPromotionInput();
                    break;
                }
            }
        }
        ChessMove move;
        if (promotionPiece != null) {
            move = new ChessMove(start, end, promotionPiece);
        } else {
            move = new ChessMove(start, end);
        }
        try {
            webSocketClient.makeMove(authData.authToken(), gameData.gameID(), move);
            return "";
        } catch (Exception e) {
            throw new RuntimeException("Invalid move");
        }
    }

    private String resign() {
        if (chessGame.isOver()) {
            return (gameData.gameName() + " is over.");
        }
        try {
            webSocketClient.resign(authData.authToken(), gameData.gameID());
            chessGame.gameOver();
            if (playerColor == null || playerColor.equals("observer")) {
                return "";
            }
        } catch (Exception e) {
            return "failed to resign";
        }
        return "You have resigned. The game is over. Please use leave command to exit";
    }

    private String evalLine(String line) {
        try {
            var tokens = line.toLowerCase().split(" ");
            var cmd = (tokens.length > 0) ? tokens[0] : "help";
            var params = Arrays.copyOfRange(tokens, 1, tokens.length);
            return switch (cmd) {
                case "quit" -> "quit";
                case "help" -> getHelp();
                case "redraw" -> redrawBoard();
                case "highlight" -> highlightMoves();
                case "leave" -> leave();
                case "move" -> makeMove();
                case "resign" -> resign();
                default -> invalidResponse();
            };
        } catch (Throwable e) {
            return e.getMessage();
        }
    }

    public String invalidResponse() {
        return "Invalid input\n" + getHelp();
    }

    private String getHelp() {
        if (Objects.equals(playerColor, "observe")) {
            return """
                    Help Menu
                    Redraw : Redraw the board
                    Highlight : Highlight a piece's possible moves
                    Leave : Leave the game
                    """;
        }
        return """
                Help Menu
                Redraw : Redraw the board
                Highlight : Highlight a piece's possible moves
                Move : Make a move
                Leave : Leave the game (gameplay can continue)
                Resign : Rage quit
                """;
    }

    //    Board print code copied from my earlier version of project, which I lowkey think was starter code at the time, I don't want to redo it
    public void fancyPrint(String color, ArrayList<ChessPosition> highlightSquares, ChessPosition highlightPiece) {
        boolean reverse = false;
        if (color != null) {
            color = color.toLowerCase();
        }
        if ("black".equals(color)) {
            reverse = true;
        }

        System.out.println();
        if (this.board == null) {
            System.out.println("There's no board to be found");
            return;
        }
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

                ChessPiece piece = chessGame.gameBoard.getPiece(new ChessPosition(row, col));
                if (piece == null) {
                    System.out.print(EscapeSequences.EMPTY);
                } else if (piece.getTeamColor().equals(ChessGame.TeamColor.WHITE)) {
                    System.out.print(printWhitePiece(piece));
                } else {
                    System.out.print(printBlackPiece(piece));
                }

                System.out.print(EscapeSequences.RESET_BG_COLOR + EscapeSequences.RESET_TEXT_COLOR);
            }
            System.out.println(); // Move to the next line for the next row
        }
        System.out.print(EscapeSequences.RESET_TEXT_COLOR);
        System.out.print(EscapeSequences.RESET_BG_COLOR);
    }

    private String printWhitePiece(ChessPiece piece) {
        return switch (piece.getPieceType()) {
            case PAWN -> EscapeSequences.WHITE_PAWN;
            case ROOK -> EscapeSequences.WHITE_ROOK;
            case KNIGHT -> EscapeSequences.WHITE_KNIGHT;
            case BISHOP -> EscapeSequences.WHITE_BISHOP;
            case QUEEN -> EscapeSequences.WHITE_QUEEN;
            case KING -> EscapeSequences.WHITE_KING;
        };
    }

    private String printBlackPiece(ChessPiece piece) {
        return switch (piece.getPieceType()) {
            case PAWN -> EscapeSequences.BLACK_PAWN;
            case ROOK -> EscapeSequences.BLACK_ROOK;
            case KNIGHT -> EscapeSequences.BLACK_KNIGHT;
            case BISHOP -> EscapeSequences.BLACK_BISHOP;
            case QUEEN -> EscapeSequences.BLACK_QUEEN;
            case KING -> EscapeSequences.BLACK_KING;
        };
    }

    @Override
    public void notify(ServerMessage notification) {
        switch (notification.getServerMessageType()) {
            case NOTIFICATION -> System.out.println(notification.getMessage());
            case LOAD_GAME -> updateGame(notification.getGame());
            case ERROR -> printError(notification.getErrorMessage());
        }
    }

    private void printError(String error) {
        if (error == null) {
            System.out.println("There was a problem with the server");
            return;
        }
        System.out.println(error);
    }

    private void updateGame(ChessGame gameUpdate) {
        this.chessGame = gameUpdate;
        this.board = gameUpdate.getBoard();
        fancyPrint(playerColor, null, null);
    }

    private ChessPosition getInputPosition(String location) {
        System.out.println("Enter " + location + " letter (a-h):");
        String pieceX = scanner.nextLine().toLowerCase();
        System.out.println("Enter " + location + " number (1-8):");
        String pieceY = scanner.nextLine();

        int col = pieceX.charAt(0) - 'a' + 1;
        int row = Integer.parseInt(pieceY);
        return new ChessPosition(row, col);
    }

    private ChessPiece.PieceType getPromotionInput() {
        while (true) {
            System.out.println("Enter promotion piece:");
            String promotionString = scanner.nextLine().toLowerCase();
            switch (promotionString) {
                case "rook" -> {
                    return ChessPiece.PieceType.ROOK;
                }
                case "knight" -> {
                    return ChessPiece.PieceType.KNIGHT;
                }
                case "bishop" -> {
                    return ChessPiece.PieceType.BISHOP;
                }
                case "queen" -> {
                    return ChessPiece.PieceType.QUEEN;
                }
                default -> {
                    System.out.println("Invalid piece name. Please choose one of the following:");
                    System.out.println("rook / knight / bishop / queen");
                }
            }
        }
    }
}

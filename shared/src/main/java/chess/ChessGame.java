package chess;

import java.util.Collection;
import java.util.HashSet;

/**
 * For a class that can manage a chess game, making moves on a board
 * <p>
 * Note: You can add to this class, but you may not alter
 * signature of the existing methods.
 */
public class ChessGame {
    TeamColor whosTurn;
    ChessBoard gameBoard;
    Boolean checkmate;

    public ChessGame() {
        whosTurn = TeamColor.WHITE;
        gameBoard = new ChessBoard();
        gameBoard.resetBoard();
        checkmate = false;
    }

    /**
     * @return Which team's turn it is
     */
    public TeamColor getTeamTurn() {
        return whosTurn;
    }

    /**
     * Set's which teams turn it is
     *
     * @param team the team whose turn it is
     */
    public void setTeamTurn(TeamColor team) {
        whosTurn = team;
    }

    /**
     * Enum identifying the 2 possible teams in a chess game
     */
    public enum TeamColor {
        WHITE,
        BLACK
    }

    /**
     * Gets a valid moves for a piece at the given location
     *
     * @param startPosition the piece to get valid moves for
     * @return Set of valid moves for requested piece, or null if no piece at
     * startPosition
     */
    public Collection<ChessMove> validMoves(ChessPosition startPosition) {
        Collection<ChessMove> trueValidMoves = new HashSet<>();
        //get piece at location
        ChessPiece piece =getBoard().getPiece(startPosition);
        //check for null piece
        if (piece == null) {
            return null;
        }
        //get that piece's moves
        Collection<ChessMove> validMoves = piece.pieceMoves(gameBoard, startPosition);
        if (validMoves == null) return null;
        //check if any are legal (don't leave king in danger)
        for (ChessMove move : validMoves) {
            boolean inCheck = checkForCheck(move, piece);
            if (inCheck == false) {
                trueValidMoves.add(move);
            }
        }
        if (trueValidMoves == null) {
            return null;
        }
        return trueValidMoves;
    }

    /**
     * Makes a move in a chess game
     *
     * @param move chess move to preform
     * @throws InvalidMoveException if move is invalid
     */
    public void makeMove(ChessMove move) throws InvalidMoveException {
        Collection<ChessMove> validMoves = validMoves(move.getStartPosition());
        if (validMoves == null) throw new InvalidMoveException("No valid moves found");
        for (ChessMove move1 : validMoves) {
            if (move.equals(move1)) {
                ChessPiece piece = gameBoard.getPiece(move.getStartPosition());
                //check for turn
                if (getTeamTurn() != piece.getTeamColor()) throw new InvalidMoveException("Not your turn");
                boolean inCheck = checkForCheck(move, piece);
                if (inCheck) throw new InvalidMoveException("Moving " + piece.getTeamColor() + " " +
                        piece.getPieceType() + "to " + move.getEndPosition().toString() + " leaves in check");

                if (move.getPromotionPiece() != null) {
                    ChessPiece.PieceType type = move.getPromotionPiece();
                    TeamColor pieceColor = piece.getTeamColor();
                    switch(type) {
                        case ROOK -> piece = new ChessPiece(pieceColor, ChessPiece.PieceType.ROOK);
                        case KNIGHT -> piece = new ChessPiece(pieceColor, ChessPiece.PieceType.KNIGHT);
                        case BISHOP -> piece = new ChessPiece(pieceColor, ChessPiece.PieceType.BISHOP);
                        case QUEEN -> piece = new ChessPiece(pieceColor, ChessPiece.PieceType.QUEEN);
                    }
                }
                gameBoard.removePiece(move.getStartPosition());
                gameBoard.addPiece(move.getEndPosition(), piece);
                if (piece.getPieceType() == ChessPiece.PieceType.KING) {
                    if (piece.getTeamColor() == TeamColor.WHITE) {
                        gameBoard.setWhiteKingPos(move.getEndPosition());
                    }
                    if (piece.getTeamColor() == TeamColor.BLACK) {
                        gameBoard.setBlackKingPos(move.getEndPosition());
                    }
                }
                switch (piece.getTeamColor()) {
                    case BLACK -> setTeamTurn(TeamColor.WHITE);
                    case WHITE -> setTeamTurn(TeamColor.BLACK);
                }
                return;
            }
        }
        throw new InvalidMoveException("Move: " + move.getEndPosition() + "for " +
                gameBoard.getPiece(move.getStartPosition()).getPieceType() + " is invalid");
    }
    private boolean checkForCheck(ChessMove move, ChessPiece piece) {
        ChessGame testGame = new ChessGame();
        ChessBoard testBoard = new ChessBoard();
        for (int r = 1; r <= 8; r++ ) {
            for (int c = 1; c <= 8; c++) {
                if (gameBoard.getPiece(new ChessPosition(r,c)) != null) {
                    testBoard.addPiece(new ChessPosition(r, c), gameBoard.getPiece(new ChessPosition(r, c)));
                }
            }
        }
        testGame.setBoard(testBoard);
        testBoard.removePiece(move.getStartPosition());
        testBoard.addPiece(move.getEndPosition(), piece);
        if (piece.getPieceType() == ChessPiece.PieceType.KING) {
            if (piece.getTeamColor() == TeamColor.WHITE) {
                testBoard.setWhiteKingPos(move.getEndPosition());
            }
            if (piece.getTeamColor() == TeamColor.BLACK) {
                testBoard.setBlackKingPos(move.getEndPosition());
            }
        }
        return testGame.isInCheck(piece.getTeamColor());
    }

    /**
     * Determines if the given team is in check
     *
     * @param teamColor which team to check for check
     * @return True if the specified team is in check
     */
    public boolean isInCheck(TeamColor teamColor) {
        ChessPosition kingPos = null;

        switch (teamColor) {
            case BLACK:
                kingPos = gameBoard.getBlackKingPos();
                break;
            case WHITE:
                kingPos = gameBoard.getWhiteKingPos();
                break;
        }
        for (int r = 1; r <= 8; r++) {
            for (int c = 1; c <= 8; c++) {
                ChessPiece piece = gameBoard.getPiece(new ChessPosition(r, c));
                if (piece == null) {continue;}
                if (piece.getTeamColor() != teamColor) {
                    Collection<ChessMove> moves = piece.pieceMoves(gameBoard, new ChessPosition(r, c));
                    if (moves != null) {
                        for (ChessMove move : moves) {
                            if (move.getEndPosition().equals(kingPos)) {
                                return true;
                            }
                        }
                    }
                }
            }
        }
        return false;
    }

    /**
     * Determines if the given team is in checkmate
     *
     * @param teamColor which team to check for checkmate
     * @return True if the specified team is in checkmate
     */
    public boolean isInCheckmate(TeamColor teamColor) {
        this.checkmate = false;
        if (isInCheck(teamColor) && checkForStalemate(teamColor)) {this.checkmate = true;}
        return checkmate;
    }

    /**
     * Determines if the given team is in stalemate, which here is defined as having
     * no valid moves
     *
     * @param teamColor which team to check for stalemate
     * @return True if the specified team is in stalemate, otherwise false
     */
    public boolean isInStalemate(TeamColor teamColor) {
        boolean stalemate = checkForStalemate(teamColor);
        if (isInCheck(teamColor)) {return false;}
        return stalemate;
    }
    private boolean checkForStalemate(TeamColor teamColor) {
        // assume is teamColor's turn
        for (int r = 1; r <= 8; r++) {
            for (int c = 1; c <= 8; c++) {
                ChessPosition position = new ChessPosition(r, c);
                if (gameBoard.getPiece(position) != null && gameBoard.getPiece(position).getTeamColor() == teamColor) {
                    if (!(validMoves(position).isEmpty())) {
                        return false;
                    }
                }
            }
        }
        return true;
    }

    /**
     * Sets this game's chessboard with a given board
     *
     * @param board the new board to use
     */
    public void setBoard(ChessBoard board) {
        gameBoard = board;
    }

    /**
     * Gets the current chessboard
     *
     * @return the chessboard
     */
    public ChessBoard getBoard() {
        return gameBoard;
    }
}

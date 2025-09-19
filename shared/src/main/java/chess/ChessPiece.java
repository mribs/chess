package chess;

import java.util.Collection;
import java.util.HashSet;
import java.util.Objects;

/**
 * Represents a single chess piece
 * <p>
 * Note: You can add to this class, but you may not alter
 * signature of the existing methods.
 */
public class ChessPiece {
    private ChessGame.TeamColor color;
    private PieceType type;

    public ChessPiece(ChessGame.TeamColor pieceColor, ChessPiece.PieceType type) {
        color = pieceColor;
        this.type = type;
    }

    /**
     * The various different chess piece options
     */
    public enum PieceType {
        KING,
        QUEEN,
        BISHOP,
        KNIGHT,
        ROOK,
        PAWN
    }

    /**
     * @return Which team this chess piece belongs to
     */
    public ChessGame.TeamColor getTeamColor() {
        return color;
    }

    /**
     * @return which type of chess piece this piece is
     */
    public PieceType getPieceType() {
        return type;
    }

    /**
     * Calculates all the positions a chess piece can move to
     * Does not take into account moves that are illegal due to leaving the king in
     * danger
     *
     * @return Collection of valid moves
     */
    public Collection<ChessMove> pieceMoves(ChessBoard board, ChessPosition myPosition) {
        ChessPiece piece = board.getPiece(myPosition);
        if (piece == null) {
            return null;
        }
        switch (piece.type) {
            case PAWN -> {
                return pawnMoves(board, myPosition);
            }
            case ROOK -> {
                return rookMoves(board, myPosition);
            }
            case KNIGHT -> {
                return knightMoves(board, myPosition);
            }
            case BISHOP -> {
                return bishopMoves(board, myPosition);
            }
            case QUEEN -> {
                return queenMoves(board, myPosition);
            }
            case KING -> {
                return kingMoves(board, myPosition);
            }

        }
        return null;
    }

    private Collection<ChessMove> pawnMoves(ChessBoard board, ChessPosition myPosition) {
        return null;
    }

    private Collection<ChessMove> rookMoves(ChessBoard board, ChessPosition myPosition) {
        return null;
    }

    private Collection<ChessMove> knightMoves(ChessBoard board, ChessPosition myPosition) {
        return null;
    }

    private Collection<ChessMove> bishopMoves(ChessBoard board, ChessPosition myPosition) {
        return null;
    }

    private Collection<ChessMove> queenMoves(ChessBoard board, ChessPosition myPosition) {
        return null;
    }

    private Collection<ChessMove> kingMoves(ChessBoard board, ChessPosition myPosition) {
        return null;
    }

    //    Helper functions for declaring moves, diagonal movement is combinations of these
    private ChessPosition moveForward(ChessPosition startPosition) {
        return null;
    }

    private ChessPosition moveBack(ChessPosition startPosition) {
        return null;
    }

    private ChessPosition moveRight(ChessPosition startPosition) {
        return null;
    }

    private ChessPosition moveLeft(ChessPosition startPosition) {
        return null;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof ChessPiece piece)) {
            return false;
        }
        return color == piece.color && type == piece.type;
    }

    @Override
    public int hashCode() {
        return Objects.hash(color, type);
    }

    @Override
    public String toString() {
        return color + " " + type + " ";
    }
}

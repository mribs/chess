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

    public enum BlockedType {
        OPEN,
        BLOCKED,
        ENEMY
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
                return pawnMoves(board, myPosition, piece);
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

    private Collection<ChessMove> pawnMoves(ChessBoard board, ChessPosition myPosition, ChessPiece myPiece) {
        Collection<ChessMove> validMoves = new HashSet<>();
        ChessPosition forwardOne = null;

        if (myPiece.color == ChessGame.TeamColor.WHITE) {
            forwardOne = moveForward(myPosition);
            validMoves.add(new ChessMove(myPosition, forwardOne));
            //  if first move can move two
            if (myPosition.getRow() == 1) {
                validMoves.add(new ChessMove(myPosition, moveForward(forwardOne)));
            }
            // if capturable piece is diagonal can go diagonal
//            ChessPosition diagonalLeft = moveLeft(forwardOne);
//            ChessPosition diagonalRight = moveRight(forwardOne);
//            if (diagonalLeft != null && checkBlocked(myPiece, board, diagonalLeft) == BlockedType.ENEMY) {
//                validMoves.add(new ChessMove(myPosition, diagonalLeft));
//            }
//            if (diagonalRight != null && checkBlocked(myPiece, board, diagonalRight) == BlockedType.ENEMY) {
//                validMoves.add(new ChessMove(myPosition, diagonalRight));
//            }
//            TODO: if end of board, pawn promotion
        }
        if (myPiece.color == ChessGame.TeamColor.BLACK) {
//            Black technically is moving backward, but for my own sanity it shall be marked as forward
            forwardOne = moveBack(myPosition);
            validMoves.add(new ChessMove(myPosition, forwardOne));
            //  if first move can move two
            if (myPosition.getRow() == 6) {
                validMoves.add(new ChessMove(myPosition, moveBack(forwardOne)));
            }
            // if capturable piece is diagonal can go diagonal

//            TODO: if end of board, pawn promotion
        }
//        remove invalid moves (blocked moves)
        for (ChessMove move : validMoves) {
            BlockedType blockedType = checkBlocked(myPiece, board, move.getEndPosition());
            if (blockedType == BlockedType.BLOCKED || blockedType == BlockedType.ENEMY) {
                validMoves.remove(move);
            }
        }
//        add diagonal movements if enemy
        ChessPosition diagonalLeft = moveLeft(forwardOne);
        ChessPosition diagonalRight = moveRight(forwardOne);
        if (diagonalLeft != null && checkBlocked(myPiece, board, diagonalLeft) == BlockedType.ENEMY) {
            validMoves.add(new ChessMove(myPosition, diagonalLeft));
        }
        if (diagonalRight != null && checkBlocked(myPiece, board, diagonalRight) == BlockedType.ENEMY) {
            validMoves.add(new ChessMove(myPosition, diagonalRight));
        }

        return validMoves;
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

    //    Helper functions for moves, diagonal movement is combinations of these
    private ChessPosition moveForward(ChessPosition startPosition) {
        int startRow = startPosition.getRow();
        int startCol = startPosition.getColumn();
        if (startRow < 7) {
            return new ChessPosition(startRow + 1, startCol, Boolean.TRUE);
        }
        return null;
    }

    private ChessPosition moveBack(ChessPosition startPosition) {
        int startRow = startPosition.getRow();
        int startCol = startPosition.getColumn();
        if (startRow > 0) {
            return new ChessPosition(startRow - 1, startCol, Boolean.TRUE);
        }
        return null;
    }

    private ChessPosition moveRight(ChessPosition startPosition) {
        int startRow = startPosition.getRow();
        int startCol = startPosition.getColumn();
        if (startCol < 7) {
            return new ChessPosition(startRow, startCol + 1, Boolean.TRUE);
        }
        return null;
    }

    private ChessPosition moveLeft(ChessPosition startPosition) {
        int startRow = startPosition.getRow();
        int startCol = startPosition.getColumn();
        if (startCol > 0) {
            return new ChessPosition(startRow, startCol - 1, Boolean.TRUE);
        }
        return null;
    }

    private BlockedType checkBlocked(ChessPiece myPiece, ChessBoard board, ChessPosition goalPosition) {
        ChessPiece enemy = board.getPiece(goalPosition);
        if (enemy == null) {
            return BlockedType.OPEN;
        } else if (enemy.color != myPiece.color) {
            return BlockedType.ENEMY;
        }
        return BlockedType.BLOCKED;
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

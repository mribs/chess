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
                return rookMoves(board, myPosition, piece);
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
        Collection<ChessMove> potentialMoves = new HashSet<>();
        ChessPosition forwardOne = null;

        if (myPiece.color == ChessGame.TeamColor.WHITE) {
            forwardOne = moveForward(myPosition);
            if (checkBlocked(myPiece, board, forwardOne) == BlockedType.OPEN) {
                potentialMoves.add(new ChessMove(myPosition, forwardOne));
                //  if first move and not blocked can move two
                if (myPosition.getRow() == 1 && checkBlocked(myPiece, board, moveForward(forwardOne)) == BlockedType.OPEN) {
                    potentialMoves.add(new ChessMove(myPosition, moveForward(forwardOne)));
                }
            }
        }
        if (myPiece.color == ChessGame.TeamColor.BLACK) {
//            Black technically is moving backward, but for my own sanity it shall be marked as forward
            forwardOne = moveBack(myPosition);
            if (checkBlocked(myPiece, board, forwardOne) == BlockedType.OPEN) {
                potentialMoves.add(new ChessMove(myPosition, forwardOne));
                //  if first move and not blocked can move two
                if (myPosition.getRow() == 6 && checkBlocked(myPiece, board, moveBack(forwardOne)) == BlockedType.OPEN) {
                    potentialMoves.add(new ChessMove(myPosition, moveBack(forwardOne)));
                }
            }
        }

//        add diagonal movements if enemy
        ChessPosition diagonalLeft = moveLeft(forwardOne);
        ChessPosition diagonalRight = moveRight(forwardOne);
        if (diagonalLeft != null && checkBlocked(myPiece, board, diagonalLeft) == BlockedType.ENEMY) {
            potentialMoves.add(new ChessMove(myPosition, diagonalLeft));
        }
        if (diagonalRight != null && checkBlocked(myPiece, board, diagonalRight) == BlockedType.ENEMY) {
            potentialMoves.add(new ChessMove(myPosition, diagonalRight));
        }
//        promotion moves
        Collection<ChessMove> validMoves = new HashSet<>();
        for (ChessMove move : potentialMoves) {
            if ((myPiece.color == ChessGame.TeamColor.WHITE && move.getEndPosition().getRow() == 7)
                    || (myPiece.color == ChessGame.TeamColor.BLACK && move.getEndPosition().getRow() == 0)) {
                validMoves.addAll(pawnPromote(move.getStartPosition(), move.getEndPosition()));
            } else {
                validMoves.add(move);
            }
        }

        return validMoves;
    }

    private Collection<ChessMove> rookMoves(ChessBoard board, ChessPosition myPosition, ChessPiece myPiece) {
        Collection<ChessMove> validMoves = new HashSet<>();
        ChessPosition nextSquare = null;
        BlockedType blocked = null;
        int startRow = myPosition.getRow();
        int startCol = myPosition.getColumn();
        ChessPosition currPosition = myPosition;
//        move up board
        for (int i = startRow; i < 7; i++) {
            nextSquare = moveForward(currPosition);
            blocked = checkBlocked(myPiece, board, nextSquare);
            if (blocked == BlockedType.OPEN) {
                validMoves.add(new ChessMove(myPosition, nextSquare));
                currPosition = nextSquare;
            } else if (blocked == BlockedType.ENEMY) {
                validMoves.add(new ChessMove(myPosition, nextSquare));
                break;
            }
        }
//        move down board
        currPosition = myPosition;
        for (int i = startRow; i > 0; i--) {
            nextSquare = moveBack(currPosition);
            blocked = checkBlocked(myPiece, board, nextSquare);
            if (blocked == BlockedType.OPEN) {
                validMoves.add(new ChessMove(myPosition, nextSquare));
                currPosition = nextSquare;
            } else if (blocked == BlockedType.ENEMY) {
                validMoves.add(new ChessMove(myPosition, nextSquare));
                break;
            }
        }
//        move left across board
        currPosition = myPosition;
        for (int i = startCol; i > 0; i--) {
            nextSquare = moveLeft(currPosition);
            blocked = checkBlocked(myPiece, board, nextSquare);
            if (blocked == BlockedType.OPEN) {
                validMoves.add(new ChessMove(myPosition, nextSquare));
                currPosition = nextSquare;
            } else if (blocked == BlockedType.ENEMY) {
                validMoves.add(new ChessMove(myPosition, nextSquare));
                break;
            }
        }
//        move Right across board
        currPosition = myPosition;
        for (int i = startCol; i < 7; i++) {
            nextSquare = moveRight(currPosition);
            blocked = checkBlocked(myPiece, board, nextSquare);
            if (blocked == BlockedType.OPEN) {
                validMoves.add(new ChessMove(myPosition, nextSquare));
                currPosition = nextSquare;
            } else if (blocked == BlockedType.ENEMY) {
                validMoves.add(new ChessMove(myPosition, nextSquare));
                break;
            }
        }
        return validMoves;
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

    private Collection<ChessMove> pawnPromote(ChessPosition startPosition, ChessPosition endPosition) {
        Collection<ChessMove> promotionMoves = new HashSet<>();
        promotionMoves.add(new ChessMove(startPosition, endPosition, PieceType.ROOK));
        promotionMoves.add(new ChessMove(startPosition, endPosition, PieceType.KNIGHT));
        promotionMoves.add(new ChessMove(startPosition, endPosition, PieceType.BISHOP));
        promotionMoves.add(new ChessMove(startPosition, endPosition, PieceType.QUEEN));
        return promotionMoves;
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

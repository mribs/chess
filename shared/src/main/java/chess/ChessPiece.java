package chess;

import java.util.Collection;
import java.util.HashSet;

/**
 * Represents a single chess piece
 * <p>
 * Note: You can add to this class, but you may not alter
 * signature of the existing methods.
 */
public class ChessPiece {
    ChessGame.TeamColor color;
    PieceType pieceType;


    public ChessPiece(ChessGame.TeamColor pieceColor, ChessPiece.PieceType type) {
        this.color = pieceColor;
        this.pieceType = type;
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
        return pieceType;
    }

    /**
     * Calculates all the positions a chess piece can move to
     * Does not take into account moves that are illegal due to leaving the king in
     * danger
     *
     * @return Collection of valid moves
     */
    public Collection<ChessMove> pieceMoves(ChessBoard board, ChessPosition myPosition) {
        Collection<ChessMove> validMoves = new HashSet<>();
        switch (pieceType) {
            case BISHOP:
                validMoves = bishopMoves(board, myPosition);
                break;
        }

        return validMoves;
    }

    private Collection<ChessMove> kingMoves(ChessBoard board, ChessPosition myPosition) {
        Collection<ChessPosition> validPositions = new HashSet<>();
        Collection<ChessMove> validMoves = new HashSet<>();
        int row = myPosition.getRow();
        int col = myPosition.getColumn();
        int tempRow = row;
        int tempCol = col;

        //up
        tempRow = row + 1;
        if (tempRow <= 8 && tempRow > 0) {
            ChessPiece piece = board.getPiece(new ChessPosition(tempRow, col));
            if (piece == null  || (piece.getTeamColor() != this.color)) {
                validPositions.add(new ChessPosition(tempRow, col));
            }
        }
        //down
        tempRow = row - 1;
        if (tempRow <= 8 && tempRow > 0) {
            ChessPiece piece = board.getPiece(new ChessPosition(tempRow, col));
            if (piece == null  || (piece.getTeamColor() != this.color)) {
                validPositions.add(new ChessPosition(tempRow, col));
            }
        }
        //left
        tempCol = col - 1;
        if (tempCol <= 8 && tempCol > 0) {
            ChessPiece piece = board.getPiece(new ChessPosition(row, tempCol));
            if (piece == null  || (piece.getTeamColor() != this.color)) {
                validPositions.add(new ChessPosition(row, tempCol));
            }
        }
        //right
        tempCol = col + 1;
        if (tempCol <= 8 && tempCol > 0) {
            ChessPiece piece = board.getPiece(new ChessPosition(row, tempCol));
            if (piece == null  || (piece.getTeamColor() != this.color)) {
                validPositions.add(new ChessPosition(row, tempCol));
            }
        }
        //diagonal UL
        tempRow = row + 1;
        tempCol = col - 1;
        if ((tempCol <= 8 && tempCol > 0) && (tempRow <= 8 && tempRow > 0)) {
            ChessPiece piece = board.getPiece(new ChessPosition(tempRow, tempCol));
            if (piece == null  || (piece.getTeamColor() != this.color)) {
                validPositions.add(new ChessPosition(tempRow, tempCol));
            }
        }
        //diagonal UR
        tempRow = row + 1;
        tempCol = col + 1;
        if ((tempCol <= 8 && tempCol > 0) && (tempRow <= 8 && tempRow > 0)) {
            ChessPiece piece = board.getPiece(new ChessPosition(tempRow, tempCol));
            if (piece == null  || (piece.getTeamColor() != this.color)) {
                validPositions.add(new ChessPosition(tempRow, tempCol));
            }
        }
        //diagonal DL
        tempRow = row - 1;
        tempCol = col - 1;
        if ((tempCol <= 8 && tempCol > 0) && (tempRow <= 8 && tempRow > 0)) {
            ChessPiece piece = board.getPiece(new ChessPosition(tempRow, tempCol));
            if (piece == null  || (piece.getTeamColor() != this.color)) {
                validPositions.add(new ChessPosition(tempRow, tempCol));
            }
        }
        //diagonal DR
        tempRow = row - 1;
        tempCol = col + 1;
        if ((tempCol <= 8 && tempCol > 0) && (tempRow <= 8 && tempRow > 0)) {
            ChessPiece piece = board.getPiece(new ChessPosition(tempRow, tempCol));
            if (piece == null  || (piece.getTeamColor() != this.color)) {
                validPositions.add(new ChessPosition(tempRow, tempCol));
            }
        }

        if (validPositions.isEmpty()) return validMoves;

        for (ChessPosition position: validPositions) {
            validMoves.add(new ChessMove(myPosition, position, null));
        }

        return validMoves;
    };
    private Collection<ChessMove> queenMoves(ChessBoard board, ChessPosition myPosition) {
        return null;
    };


    private Collection<ChessMove> bishopMoves(ChessBoard board, ChessPosition myPosition) {
        Collection<ChessMove> validMoves = new HashSet<>();
        Collection<ChessPosition> validPositions = new HashSet<>();
        int row = myPosition.getRow();
        int col = myPosition.getColumn();
        int tempColLeft = col;
        int tempColRight = col;
        ChessGame.TeamColor color = board.getPiece(myPosition).getTeamColor();

        //White forward/ Black backwards
        for (int r=row + 1; r <= 8; r++) {
            //right
            tempColRight=tempColRight + 1;
            if (tempColRight <= 8 && tempColRight > 0) {
                ChessPiece inFront=board.getPiece(new ChessPosition(r, tempColRight));
                if (inFront == null) {
                    validPositions.add(new ChessPosition(r, tempColRight));
                } else if (inFront.getTeamColor() == color) {
                    break;
                } else {
                    validPositions.add(new ChessPosition(r, tempColRight));
                    break;
                }
            }
        }
        for (int r = row + 1; r <= 8; r++) {
            //left
            tempColLeft = tempColLeft - 1;
            if (tempColLeft <= 8 && tempColLeft > 0) {
                ChessPiece inFront=board.getPiece(new ChessPosition(r, tempColLeft));
                if (inFront == null) {
                    validPositions.add(new ChessPosition(r, tempColLeft));
                }
                else if (inFront.getTeamColor() == color) {
                    break;
                }
                else {
                    validPositions.add(new ChessPosition(r, tempColLeft));
                    break;
                }
            }

        }
        //White Backward/ Black forwards
        tempColRight = col;
        tempColLeft = col;
        for (int r=row - 1; r > 0; r--) {
            //right
            tempColRight=tempColRight + 1;
            if (tempColRight <= 8 && tempColRight > 0) {
                ChessPiece inFront=board.getPiece(new ChessPosition(r, tempColRight));
                if (inFront == null) {
                    validPositions.add(new ChessPosition(r, tempColRight));
                } else if (inFront.getTeamColor() == color) {
                    break;
                } else {
                    validPositions.add(new ChessPosition(r, tempColRight));
                    break;
                }
            }
        }
        for (int r = row -1; r > 0; r--) {
            //left
            tempColLeft=tempColLeft - 1;
            if (tempColLeft <= 8 && tempColLeft > 0) {
                ChessPiece inFront = board.getPiece(new ChessPosition(r, tempColLeft));
                if (inFront == null) {
                    validPositions.add(new ChessPosition(r, tempColLeft));
                } else if (inFront.getTeamColor() == color) {
                    break;
                } else {
                    validPositions.add(new ChessPosition(r, tempColLeft));
                    break;
                }
            }
        }



        if (validPositions.isEmpty()) return validMoves;
        //make moves
        for (ChessPosition position: validPositions) {
            validMoves.add(new ChessMove(myPosition, position, null));
        }

        return validMoves;
    }
    private Collection<ChessMove> knightMoves(ChessBoard board, ChessPosition myPosition) {
        return null;
    };
    private Collection<ChessMove> rookMoves(ChessBoard board, ChessPosition myPosition) {
        return null;
    };
    private Collection<ChessMove> pawnMoves(ChessBoard board, ChessPosition myPosition) {
        return null;
    };



}

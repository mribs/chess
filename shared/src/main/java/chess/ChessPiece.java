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
        this.color = pieceColor;
        this.type = type;
    }
    public enum PieceType {
        KING,
        QUEEN,
        BISHOP,
        KNIGHT,
        ROOK,
        PAWN
    }
    public ChessGame.TeamColor getTeamColor() {
        return color;
    }
    public PieceType getPieceType() {
        return type;
    }
    public Collection<ChessMove> pieceMoves(ChessBoard board, ChessPosition myPosition) {
        ChessPiece piece = board.getPiece(myPosition);
        switch (piece.type) {
            case PAWN -> {
                return pawnMoves(board, myPosition, piece);
            }
            case KING -> {
                return kingMoves(board, myPosition,piece);
            }
            case QUEEN -> {
                return queenMoves(board, myPosition, piece);
            }
            case ROOK -> {
                return rookMoves(board, myPosition, piece);
            }
            case BISHOP -> {
                return bishopMoves(board, myPosition, piece);
            }
            case KNIGHT -> {
                return knightMoves(board, myPosition, piece);
            }
        }
        return null;
    }

    private Collection<ChessMove> knightMoves(ChessBoard board, ChessPosition myPosition, ChessPiece piece) {
        Collection<ChessMove> validMoves = new HashSet<>();
        Collection<ChessMove> potentialMoves = new HashSet<>();
        int row =myPosition.getRow();
        int col = myPosition.getColumn();
        //up 2 right 1
        potentialMoves.add(new ChessMove(myPosition, new ChessPosition(row + 2, col + 1), null));
        //down 2 right 1
        potentialMoves.add(new ChessMove(myPosition, new ChessPosition(row - 2, col + 1), null));
        //up 1 right 2
        potentialMoves.add(new ChessMove(myPosition, new ChessPosition(row + 1, col + 2), null));
        //down 1 right 2
        potentialMoves.add(new ChessMove(myPosition, new ChessPosition(row - 1, col + 2), null));
        //up 2 left 1
        potentialMoves.add(new ChessMove(myPosition, new ChessPosition(row + 2, col - 1), null));
        //down 2 left 1
        potentialMoves.add(new ChessMove(myPosition, new ChessPosition(row - 2, col - 1), null));
        //up 1 left 2
        potentialMoves.add(new ChessMove(myPosition, new ChessPosition(row + 1, col - 2), null));
        //down 1 left 2
        potentialMoves.add(new ChessMove(myPosition, new ChessPosition(row - 1, col - 2), null));
        for (ChessMove move : potentialMoves) {
            row = move.getEndPosition().getRow();
            col = move.getEndPosition().getColumn();
            if (row > 8 || row < 1 || col > 8 || col < 1) {
                continue;
            }
            ChessPiece blocker = board.getPiece(move.getEndPosition());
            if (blocker != null && blocker.color == piece.color) {
                continue;
            }
            validMoves.add(move);
        }
        return validMoves;
    }

    private Collection<ChessMove> rookMoves(ChessBoard board, ChessPosition myPosition, ChessPiece piece) {
        Collection<ChessMove> validMoves = new HashSet<>();
        ChessMove potentialMove = null;
        ChessPiece blocker = null;
        Boolean canMove = Boolean.TRUE;
        //up
        potentialMove = up(myPosition);
        while (canMove) {
            if (potentialMove.getEndPosition().getRow() > 8) {
                break;
            }
            blocker = board.getPiece(potentialMove.getEndPosition());
            if (blocker != null) {
                if (blocker.color != piece.color) {
                    validMoves.add(new ChessMove(myPosition, potentialMove.getEndPosition(), null));
                }
                break;
            }
            validMoves.add(new ChessMove(myPosition, potentialMove.getEndPosition(), null));
            potentialMove = up(potentialMove.getEndPosition());
        }
        //down
        canMove = Boolean.TRUE;
        potentialMove = down(myPosition);
        while (canMove) {
            if (potentialMove.getEndPosition().getRow() < 1) {
                break;
            }
            blocker = board.getPiece(potentialMove.getEndPosition());
            if (blocker != null) {
                if (blocker.color != piece.color) {
                    validMoves.add(new ChessMove(myPosition, potentialMove.getEndPosition(), null));
                }
                break;
            }
            validMoves.add(new ChessMove(myPosition, potentialMove.getEndPosition(), null));
            potentialMove = down(potentialMove.getEndPosition());
        }
        //left
        canMove = Boolean.TRUE;
        potentialMove = left(myPosition);
        while (canMove) {
            if (potentialMove.getEndPosition().getColumn() < 1) {
                break;
            }
            blocker = board.getPiece(potentialMove.getEndPosition());
            if (blocker != null) {
                if (blocker.color != piece.color) {
                    validMoves.add(new ChessMove(myPosition, potentialMove.getEndPosition(), null));
                }
                break;
            }
            validMoves.add(new ChessMove(myPosition, potentialMove.getEndPosition(), null));
            potentialMove = left(potentialMove.getEndPosition());
        }
        //right
        canMove = Boolean.TRUE;
        potentialMove = right(myPosition);
        while (canMove) {
            if (potentialMove.getEndPosition().getColumn() > 8) {
                break;
            }
            blocker = board.getPiece(potentialMove.getEndPosition());
            if (blocker != null) {
                if (blocker.color != piece.color) {
                    validMoves.add(new ChessMove(myPosition, potentialMove.getEndPosition(), null));
                }
                break;
            }
            validMoves.add(new ChessMove(myPosition, potentialMove.getEndPosition(), null));
            potentialMove = right(potentialMove.getEndPosition());
        }
        return validMoves;
    }

    private Collection<ChessMove> queenMoves(ChessBoard board, ChessPosition myPosition, ChessPiece piece) {
        Collection<ChessMove> validMoves = rookMoves(board, myPosition, piece);
        Collection<ChessMove> bishopMoves = bishopMoves(board, myPosition, piece);
        for (ChessMove move : bishopMoves) {
            validMoves.add(move);
        }
        return validMoves;
    }

    private Collection<ChessMove> bishopMoves(ChessBoard board, ChessPosition myPosition, ChessPiece piece) {
        Collection<ChessMove> validMoves = new HashSet<>();
        ChessMove potentialMove = null;
        ChessPiece blocker = null;
        Boolean canMove = Boolean.TRUE;
        //upRight
        potentialMove = upRight(myPosition);
        while (canMove) {
            if (potentialMove.getEndPosition().getRow() > 8 || potentialMove.getEndPosition().getColumn() > 8) {
                break;
            }
            blocker = board.getPiece(potentialMove.getEndPosition());
            if (blocker != null) {
                if (blocker.color != piece.color) {
                    validMoves.add(new ChessMove(myPosition, potentialMove.getEndPosition(), null));
                }
                break;
            }
            validMoves.add(new ChessMove(myPosition, potentialMove.getEndPosition(), null));
            potentialMove = upRight(potentialMove.getEndPosition());
        }
        //downRight
        canMove = Boolean.TRUE;
        potentialMove = downRight(myPosition);
        while (canMove) {
            if (potentialMove.getEndPosition().getRow() < 1  || potentialMove.getEndPosition().getColumn() > 8) {
                break;
            }
            blocker = board.getPiece(potentialMove.getEndPosition());
            if (blocker != null) {
                if (blocker.color != piece.color) {
                    validMoves.add(new ChessMove(myPosition, potentialMove.getEndPosition(), null));
                }
                break;
            }
            validMoves.add(new ChessMove(myPosition, potentialMove.getEndPosition(), null));
            potentialMove = downRight(potentialMove.getEndPosition());
        }
        //downLeft
        canMove = Boolean.TRUE;
        potentialMove = downLeft(myPosition);
        while (canMove) {
            if (potentialMove.getEndPosition().getColumn() < 1  || potentialMove.getEndPosition().getRow() < 1) {
                break;
            }
            blocker = board.getPiece(potentialMove.getEndPosition());
            if (blocker != null) {
                if (blocker.color != piece.color) {
                    validMoves.add(new ChessMove(myPosition, potentialMove.getEndPosition(), null));
                }
                break;
            }
            validMoves.add(new ChessMove(myPosition, potentialMove.getEndPosition(), null));
            potentialMove = downLeft(potentialMove.getEndPosition());
        }
        //upLeft
        canMove = Boolean.TRUE;
        potentialMove = upLeft(myPosition);
        while (canMove) {
            if (potentialMove.getEndPosition().getRow() > 8  || potentialMove.getEndPosition().getColumn() < 1) {
                break;
            }
            blocker = board.getPiece(potentialMove.getEndPosition());
            if (blocker != null) {
                if (blocker.color != piece.color) {
                    validMoves.add(new ChessMove(myPosition, potentialMove.getEndPosition(), null));
                }
                break;
            }
            validMoves.add(new ChessMove(myPosition, potentialMove.getEndPosition(), null));
            potentialMove = upLeft(potentialMove.getEndPosition());
        }
        return validMoves;
    }

    private Collection<ChessMove> kingMoves(ChessBoard board, ChessPosition myPosition, ChessPiece piece) {
        Collection<ChessMove> validMoves = new HashSet<>();
        Collection<ChessMove> potentialMoves = new HashSet<>();
        ChessPiece blocker = null;
        potentialMoves.add(upLeft(myPosition));
        potentialMoves.add(up(myPosition));
        potentialMoves.add(upRight(myPosition));
        potentialMoves.add(right(myPosition));
        potentialMoves.add(downRight(myPosition));
        potentialMoves.add(down(myPosition));
        potentialMoves.add(downLeft(myPosition));
        potentialMoves.add(left(myPosition));

        for (ChessMove move : potentialMoves) {
            int row =move.getEndPosition().getRow();
            int col =move.getEndPosition().getColumn();
            if (row > 8 || row < 1 || col > 8 || col < 1) continue;
            blocker = board.getPiece(move.getEndPosition());
            if (blocker == null) {
                validMoves.add(move);
            } else if (blocker.color != piece.color) {
                validMoves.add(move);
            }
        }
        return validMoves;
    }
    private Collection<ChessMove> whitePawnMoves(ChessBoard board, ChessPosition myPosition, ChessPiece piece) {
        Collection<ChessMove> validMoves = new HashSet<>();
        ChessMove potentialMove = null;
        ChessPiece blocker = null;
        int row =myPosition.getRow();
        int col =myPosition.getColumn();
        int checkRow = 0;
        int checkCol = 0;
        if (row == 8) return validMoves;
        //move up two if in OG row
        if (row == 2) {
            potentialMove = new ChessMove(myPosition, new ChessPosition(row + 2, col), null);
            checkRow = potentialMove.getEndPosition().getRow();
            checkCol = potentialMove.getEndPosition().getColumn();
            blocker = board.getPiece(potentialMove.getEndPosition());
            if (blocker == null ) {
                blocker = board.getPiece(new ChessPosition(row + 1, col));
                if (blocker == null) {
                    validMoves.add(potentialMove);
                }
            }
        } else if (row == 7) {
            potentialMove = new ChessMove(myPosition, new ChessPosition(row + 1, col), null);
            blocker = board.getPiece(potentialMove.getEndPosition());
            if (blocker == null ) {
                validMoves.addAll(addAllPromotion(myPosition, potentialMove.getEndPosition()));
            }
            //move diagonally for capture
            if (col > 1 && row < 8) {
                potentialMove=upLeft(myPosition);
                blocker=board.getPiece(potentialMove.getEndPosition());
                if (blocker != null) {
                    if (blocker.color == ChessGame.TeamColor.BLACK) {
                        validMoves.addAll(addAllPromotion(myPosition, potentialMove.getEndPosition()));
                    }
                }
            }
            if (col < 8 && row > 1) {
                potentialMove=upRight(myPosition);
                blocker=board.getPiece(potentialMove.getEndPosition());
                if (blocker != null) {
                    if (blocker.color == ChessGame.TeamColor.BLACK) {
                        validMoves.addAll(addAllPromotion(myPosition, potentialMove.getEndPosition()));
                    }
                }
            }
            return validMoves;
        }
        //move up one if no one blocking
        potentialMove = up(myPosition);
        blocker = board.getPiece(potentialMove.getEndPosition());
        if (blocker == null ) {
            validMoves.add(potentialMove);
        }
        //move diagonally for capture
        if (col > 1 && row < 8) {
            potentialMove=upLeft(myPosition);
            blocker=board.getPiece(potentialMove.getEndPosition());
            if (blocker != null) {
                if (blocker.color == ChessGame.TeamColor.BLACK) {
                    validMoves.add(potentialMove);
                }
            }
        }
        if (col < 8 && row < 8) {
            potentialMove=upRight(myPosition);
            blocker=board.getPiece(potentialMove.getEndPosition());
            if (blocker != null) {
                if (blocker.color == ChessGame.TeamColor.BLACK) {
                    validMoves.add(potentialMove);
                }
            }
        }
        return validMoves;
    }

    private Collection<ChessMove> pawnMoves(ChessBoard board, ChessPosition myPosition, ChessPiece piece) {
        Collection<ChessMove> validMoves = new HashSet<>();
        ChessMove potentialMove = null;
        ChessPiece blocker = null;
        int row =myPosition.getRow();
        int col =myPosition.getColumn();
        int checkRow = 0;
        int checkCol = 0;
        if (piece.color == ChessGame.TeamColor.WHITE) {
            validMoves = whitePawnMoves(board, myPosition, piece);
        }
        else if (piece.color == ChessGame.TeamColor.BLACK) {
            if (row == 1) return validMoves;
            //move down two if in OG row
            if (row == 7) {
                potentialMove = new ChessMove(myPosition, new ChessPosition(row - 2, col), null);
                blocker = board.getPiece(potentialMove.getEndPosition());
                if (blocker == null ) {
                    blocker = board.getPiece(new ChessPosition(row - 1, col));
                    if (blocker == null) {
                        validMoves.add(potentialMove);
                    }
                }
            }else if (row == 2) {
                potentialMove = new ChessMove(myPosition, new ChessPosition(row - 1, col), null);
                blocker = board.getPiece(potentialMove.getEndPosition());
                if (blocker == null ) {
                    validMoves.addAll(addAllPromotion(myPosition, potentialMove.getEndPosition()));
                }
                //move diagonally for capture
                if (col > 1 && row > 1) {
                    potentialMove=downLeft(myPosition);
                    blocker=board.getPiece(potentialMove.getEndPosition());
                    if (blocker != null) {
                        if (blocker.color == ChessGame.TeamColor.WHITE) {
                            validMoves.addAll(addAllPromotion(myPosition, potentialMove.getEndPosition()));
                        }
                    }
                }
                if (col < 8 && row > 1) {
                    potentialMove=downRight(myPosition);
                    blocker=board.getPiece(potentialMove.getEndPosition());
                    if (blocker != null) {
                        if (blocker.color == ChessGame.TeamColor.WHITE) {
                            validMoves.addAll(addAllPromotion(myPosition, potentialMove.getEndPosition()));
                        }
                    }
                }
                return validMoves;
            }
            //move down one if no one blocking
            potentialMove = down(myPosition);
            blocker = board.getPiece(potentialMove.getEndPosition());
            if (blocker == null ) {
                validMoves.add(potentialMove);
            }
            //move diagonally for capture
            if (col > 1 && row > 1) {
                potentialMove=downLeft(myPosition);
                blocker=board.getPiece(potentialMove.getEndPosition());
                if (blocker != null) {
                    if (blocker.color == ChessGame.TeamColor.WHITE) {
                        validMoves.add(potentialMove);
                    }
                }
            }
            if ( col < 8 && row > 1) {
                potentialMove=downRight(myPosition);
                blocker=board.getPiece(potentialMove.getEndPosition());
                if (blocker != null) {
                    if (blocker.color == ChessGame.TeamColor.WHITE) {
                        validMoves.add(potentialMove);
                    }
                }
            }
        }
        return validMoves;
    }

    private Collection<ChessMove> addAllPromotion(ChessPosition myPosition, ChessPosition endPosition) {
        Collection<ChessMove> promotionMoves = new HashSet<>();
        promotionMoves.add(new ChessMove(myPosition, endPosition, PieceType.ROOK));
        promotionMoves.add(new ChessMove(myPosition, endPosition, PieceType.KNIGHT));
        promotionMoves.add(new ChessMove(myPosition, endPosition, PieceType.BISHOP));
        promotionMoves.add(new ChessMove(myPosition, endPosition, PieceType.QUEEN));
        return promotionMoves;
    }

    private ChessMove right(ChessPosition myPosition) {
        return new ChessMove(myPosition, new ChessPosition(myPosition.getRow(), myPosition.getColumn() +1), null);
    }

    private ChessMove left(ChessPosition myPosition) {
        return new ChessMove(myPosition, new ChessPosition(myPosition.getRow(), myPosition.getColumn() -1), null);
    }

    private ChessMove downRight(ChessPosition myPosition) {
        return new ChessMove(myPosition, new ChessPosition(myPosition.getRow() - 1, myPosition.getColumn() +1), null);
    }

    private ChessMove downLeft(ChessPosition myPosition) {
        return new ChessMove(myPosition, new ChessPosition(myPosition.getRow() - 1, myPosition.getColumn() -1), null);
    }

    private ChessMove upRight(ChessPosition myPosition) {
        return new ChessMove(myPosition, new ChessPosition(myPosition.getRow() + 1, myPosition.getColumn() +1), null);
    }

    private ChessMove upLeft(ChessPosition myPosition) {
        return new ChessMove(myPosition, new ChessPosition(myPosition.getRow() + 1, myPosition.getColumn() -1), null);
    }
    private ChessMove down(ChessPosition myPosition) {
        return new ChessMove(myPosition, new ChessPosition(myPosition.getRow()- 1, myPosition.getColumn()), null);
    }
    private ChessMove up(ChessPosition myPosition) {
        return new ChessMove(myPosition, new ChessPosition(myPosition.getRow() + 1, myPosition.getColumn()), null);
    }
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof ChessPiece piece)) return false;
        return color == piece.color && type == piece.type;
    }
    @Override
    public int hashCode() {
        return Objects.hash(color, type);
    }
}
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
            case KING:
                validMoves = kingMoves(board, myPosition);
                break;
            case QUEEN:
                validMoves = queenMoves(board, myPosition);
                break;
            case BISHOP:
                validMoves = bishopMoves(board, myPosition);
                break;
            case KNIGHT:
                validMoves = knightMoves(board, myPosition);
                break;
            case ROOK:
                validMoves = rookMoves(board, myPosition);
                break;
            case PAWN:
                validMoves = pawnMoves(board, myPosition);
                break;
        }

        return validMoves;
    }

    private Collection<ChessMove> kingMoves(ChessBoard board, ChessPosition position) {
        Collection<ChessMove> validMoves = new HashSet<>();
        ChessPosition newPosition = position;
        //up
        newPosition = up(position);
        if (newPosition != null) {
            if (board.getPiece(newPosition) != null) {
                ChessPiece blockage=board.getPiece(newPosition);
                if (blockage.getTeamColor() != this.color) {
                    validMoves.add(new ChessMove(position, newPosition, null));
                }
            }
            else {
                validMoves.add(new ChessMove(position, newPosition, null));
            }
        }
        //down
        newPosition = down(position);
        if (newPosition != null) {
            if (board.getPiece(newPosition) != null) {
                ChessPiece blockage=board.getPiece(newPosition);
                if (blockage.getTeamColor() != this.color) {
                    validMoves.add(new ChessMove(position, newPosition, null));
                }
            }
            else {
                validMoves.add(new ChessMove(position, newPosition, null));
            }
        }
        //left
        newPosition = left(position);
        if (newPosition != null) {
            if (board.getPiece(newPosition) != null) {
                ChessPiece blockage=board.getPiece(newPosition);
                if (blockage.getTeamColor() != this.color) {
                    validMoves.add(new ChessMove(position, newPosition, null));
                }
            }
            else {
                validMoves.add(new ChessMove(position, newPosition, null));
            }
        }
        //right
        newPosition = right(position);
        if (newPosition != null) {
            if (board.getPiece(newPosition) != null) {
                ChessPiece blockage=board.getPiece(newPosition);
                if (blockage.getTeamColor() != this.color) {
                    validMoves.add(new ChessMove(position, newPosition, null));
                }
            }
            else {
                validMoves.add(new ChessMove(position, newPosition, null));
            }
        }
        //upright
        newPosition = upRight(position);
        if (newPosition != null) {
            if (board.getPiece(newPosition) != null) {
                ChessPiece blockage=board.getPiece(newPosition);
                if (blockage.getTeamColor() != this.color) {
                    validMoves.add(new ChessMove(position, newPosition, null));
                }
            }
            else {
                validMoves.add(new ChessMove(position, newPosition, null));
            }
        }
        //upLeft
        newPosition = upLeft(position);
        if (newPosition != null) {
            if (board.getPiece(newPosition) != null) {
                ChessPiece blockage=board.getPiece(newPosition);
                if (blockage.getTeamColor() != this.color) {
                    validMoves.add(new ChessMove(position, newPosition, null));
                }
            }
            else {
                validMoves.add(new ChessMove(position, newPosition, null));
            }
        }
        //downRight
        newPosition = downRight(position);
        if (newPosition != null) {
            if (board.getPiece(newPosition) != null) {
                ChessPiece blockage=board.getPiece(newPosition);
                if (blockage.getTeamColor() != this.color) {
                    validMoves.add(new ChessMove(position, newPosition, null));
                }
            }
            else {
                validMoves.add(new ChessMove(position, newPosition, null));
            }
        }
        //downLeft
        newPosition = downLeft(position);
        if (newPosition != null) {
            if (board.getPiece(newPosition) != null) {
                ChessPiece blockage=board.getPiece(newPosition);
                if (blockage.getTeamColor() != this.color) {
                    validMoves.add(new ChessMove(position, newPosition, null));
                }
            }
            else {
                validMoves.add(new ChessMove(position, newPosition, null));
            }
        }

        return validMoves;
    };
    private Collection<ChessMove> queenMoves(ChessBoard board, ChessPosition position) {
        Collection<ChessMove> validMoves = new HashSet<>();
        ChessPosition newPosition = position;
        boolean canMove = true;

        //up
        while (canMove) {
            newPosition=up(newPosition);
            if (newPosition != null) {
                if (board.getPiece(newPosition) != null) {
                    ChessPiece blockage=board.getPiece(newPosition);
                    if (blockage.getTeamColor() == this.color) {
                        break;
                    } else {
                        canMove=false;
                    }
                }
                validMoves.add(new ChessMove(position, newPosition, null));
            } else canMove=false;
        }

        canMove = true;
        newPosition = position;
        //down
        while (canMove) {
            newPosition = down(newPosition);
            if (newPosition != null) {
                if (board.getPiece(newPosition) != null) {
                    ChessPiece blockage =board.getPiece(newPosition);
                    if (blockage.getTeamColor() == this.color) {
                        break;
                    }
                    else {
                        canMove = false;
                    }
                }
                validMoves.add(new ChessMove(position, newPosition, null));
            }
            else canMove = false;
        }

        canMove = true;
        newPosition = position;
        //left
        while (canMove) {
            newPosition = left(newPosition);
            if (newPosition != null) {
                if (board.getPiece(newPosition) != null) {
                    ChessPiece blockage =board.getPiece(newPosition);
                    if (blockage.getTeamColor() == this.color) {
                        break;
                    }
                    else {
                        canMove = false;
                    }
                }
                validMoves.add(new ChessMove(position, newPosition, null));
            }
            else canMove = false;
        }

        canMove = true;
        newPosition = position;
        //right
        while (canMove) {
            newPosition = right(newPosition);
            if (newPosition != null) {
                if (board.getPiece(newPosition) != null) {
                    ChessPiece blockage =board.getPiece(newPosition);
                    if (blockage.getTeamColor() == this.color) {
                        break;
                    }
                    else {
                        canMove = false;
                    }
                }
                validMoves.add(new ChessMove(position, newPosition, null));
            }
            else canMove = false;
        }

        canMove = true;
        newPosition = position;
        //downLeft
        while (canMove) {
            newPosition = downLeft(newPosition);
            if (newPosition != null) {
                if (board.getPiece(newPosition) != null) {
                    ChessPiece blockage =board.getPiece(newPosition);
                    if (blockage.getTeamColor() == this.color) {
                        break;
                    }
                    else {
                        canMove = false;
                    }
                }
                validMoves.add(new ChessMove(position, newPosition, null));
            }
            else canMove = false;
        }
        canMove = true;
        //downRight
        newPosition = position;
        while (canMove) {
            newPosition = downRight(newPosition);
            if (newPosition != null) {
                if (board.getPiece(newPosition) != null) {
                    ChessPiece blockage =board.getPiece(newPosition);
                    if (blockage.getTeamColor() == this.color) {
                        break;
                    }
                    else {
                        canMove = false;
                    }
                }
                validMoves.add(new ChessMove(position, newPosition, null));
            }
            else canMove = false;
        }
        canMove = true;
        //upLeft
        newPosition = position;
        while (canMove) {
            newPosition = upLeft(newPosition);
            if (newPosition != null) {
                if (board.getPiece(newPosition) != null) {
                    ChessPiece blockage = board.getPiece(newPosition);
                    if (blockage.getTeamColor() == this.color) {
                        break;
                    }
                    else {
                        validMoves.add(new ChessMove(position, newPosition, null));
                        canMove = false;
                    }
                }
                validMoves.add(new ChessMove(position, newPosition, null));
            }
            else canMove = false;
        }
        canMove = true;
        //upRight
        newPosition = position;
        while (canMove) {
            newPosition = upRight(newPosition);

            if (newPosition != null) {
                if (board.getPiece(newPosition) != null) {
                    ChessPiece blockage =board.getPiece(newPosition);
                    if (blockage.getTeamColor() == this.color) {
                        break;
                    }
                    else {
                        validMoves.add(new ChessMove(position, newPosition, null));
                        canMove = false;
                    }
                }
                validMoves.add(new ChessMove(position, newPosition, null));
            }
            else canMove = false;
        }
        return validMoves;
    };
    private Collection<ChessMove> bishopMoves(ChessBoard board, ChessPosition position) {
        Collection<ChessMove> validMoves = new HashSet<>();
        ChessPosition newPosition = position;
        boolean canMove = true;

        //downLeft
        while (canMove) {
            newPosition = downLeft(newPosition);
            if (newPosition != null) {
                if (board.getPiece(newPosition) != null) {
                    ChessPiece blockage =board.getPiece(newPosition);
                    if (blockage.getTeamColor() == this.color) {
                        break;
                    }
                    else {
                        canMove = false;
                    }
                }
                validMoves.add(new ChessMove(position, newPosition, null));
            }
            else canMove = false;
        }
        canMove = true;
        //downRight
        newPosition = position;
        while (canMove) {
            newPosition = downRight(newPosition);
            if (newPosition != null) {
                if (board.getPiece(newPosition) != null) {
                    ChessPiece blockage =board.getPiece(newPosition);
                    if (blockage.getTeamColor() == this.color) {
                        break;
                    }
                    else {
                        canMove = false;
                    }
                }
                validMoves.add(new ChessMove(position, newPosition, null));
            }
            else canMove = false;
        }
        canMove = true;
        //upLeft
        newPosition = position;
        while (canMove) {
            newPosition = upLeft(newPosition);
            if (newPosition != null) {
                if (board.getPiece(newPosition) != null) {
                    ChessPiece blockage = board.getPiece(newPosition);
                    if (blockage.getTeamColor() == this.color) {
                        break;
                    }
                    else {
                        validMoves.add(new ChessMove(position, newPosition, null));
                        canMove = false;
                    }
                }
                validMoves.add(new ChessMove(position, newPosition, null));
            }
            else canMove = false;
        }
        canMove = true;
        //upRight
        newPosition = position;
        while (canMove) {
            newPosition = upRight(newPosition);

            if (newPosition != null) {
                if (board.getPiece(newPosition) != null) {
                    ChessPiece blockage =board.getPiece(newPosition);
                    if (blockage.getTeamColor() == this.color) {
                        break;
                    }
                    else {
                        validMoves.add(new ChessMove(position, newPosition, null));
                        canMove = false;
                    }
                }
                validMoves.add(new ChessMove(position, newPosition, null));
            }
            else canMove = false;
        }
        return validMoves;
    }
    private Collection<ChessMove> knightMoves(ChessBoard board, ChessPosition position) {
        Collection<ChessMove> validMoves = new HashSet<>();
        ChessPosition newPosition = position;
        //up up right
        newPosition = up(position);
        if (newPosition != null) newPosition = up(newPosition);
        if (newPosition != null) newPosition = right(newPosition);
        if (newPosition != null) {
            if (board.getPiece(newPosition) != null) {
                if (board.getPiece(newPosition).getTeamColor() != color) {
                    validMoves.add(new ChessMove(position, newPosition, null));
                }
            }
            else {
                validMoves.add(new ChessMove(position,newPosition,null));
            }
        }
        //up up left
        newPosition = up(position);
        if (newPosition != null) newPosition = up(newPosition);
        if (newPosition != null) newPosition = left(newPosition);
        if (newPosition != null) {
            if (board.getPiece(newPosition) != null) {
                if (board.getPiece(newPosition).getTeamColor() != color) {
                    validMoves.add(new ChessMove(position, newPosition, null));
                }
            }
            else {
                validMoves.add(new ChessMove(position,newPosition,null));
            }
        }
        //down down right
        newPosition = down(position);
        if (newPosition != null) newPosition = down(newPosition);
        if (newPosition != null) newPosition = right(newPosition);
        if (newPosition != null) {
            if (board.getPiece(newPosition) != null) {
                if (board.getPiece(newPosition).getTeamColor() != color) {
                    validMoves.add(new ChessMove(position, newPosition, null));
                }
            }
            else {
                validMoves.add(new ChessMove(position,newPosition,null));
            }
        }
        //down down left
        newPosition = down(position);
        if (newPosition != null) newPosition = down(newPosition);
        if (newPosition != null) newPosition = left(newPosition);
        if (newPosition != null) {
            if (board.getPiece(newPosition) != null) {
                if (board.getPiece(newPosition).getTeamColor() != color) {
                    validMoves.add(new ChessMove(position, newPosition, null));
                }
            }
            else {
                validMoves.add(new ChessMove(position,newPosition,null));
            }
        }
        //left left up
        newPosition = left(position);
        if (newPosition != null) newPosition = left(newPosition);
        if (newPosition != null) newPosition = up(newPosition);
        if (newPosition != null) {
            if (board.getPiece(newPosition) != null) {
                if (board.getPiece(newPosition).getTeamColor() != color) {
                    validMoves.add(new ChessMove(position, newPosition, null));
                }
            }
            else {
                validMoves.add(new ChessMove(position,newPosition,null));
            }
        }
        //left left down
        newPosition = left(position);
        if (newPosition != null)  newPosition = left(newPosition);
        if (newPosition != null) newPosition = down(newPosition);
        if (newPosition != null) {
            if (board.getPiece(newPosition) != null) {
                if (board.getPiece(newPosition).getTeamColor() != color) {
                    validMoves.add(new ChessMove(position, newPosition, null));
                }
            }
            else {
                validMoves.add(new ChessMove(position,newPosition,null));
            }
        }
        //right right up
        newPosition = right(position);
        if (newPosition != null) newPosition = right(newPosition);
        if (newPosition != null) newPosition = up(newPosition);
        if (newPosition != null) {
            if (board.getPiece(newPosition) != null) {
                if (board.getPiece(newPosition).getTeamColor() != color) {
                    validMoves.add(new ChessMove(position, newPosition, null));
                }
            }
            else {
                validMoves.add(new ChessMove(position,newPosition,null));
            }
        }
        //right right down
        newPosition = right(position);
        if (newPosition != null) newPosition = right(newPosition);
        if (newPosition != null) newPosition = down(newPosition);
        if (newPosition != null) {
            if (board.getPiece(newPosition) != null) {
                if (board.getPiece(newPosition).getTeamColor() != color) {
                    validMoves.add(new ChessMove(position, newPosition, null));
                }
            }
            else {
                validMoves.add(new ChessMove(position,newPosition,null));
            }
        }
        return validMoves;
    };
    private Collection<ChessMove> rookMoves(ChessBoard board, ChessPosition position) {
        Collection<ChessMove> validMoves = new HashSet<>();
        ChessPosition newPosition = position;
        boolean canMove = true;

        //up
        while (canMove) {
            newPosition=up(newPosition);
            if (newPosition != null) {
                if (board.getPiece(newPosition) != null) {
                    ChessPiece blockage=board.getPiece(newPosition);
                    if (blockage.getTeamColor() == this.color) {
                        break;
                    } else {
                        canMove=false;
                    }
                }
                validMoves.add(new ChessMove(position, newPosition, null));
            } else canMove=false;
        }

        canMove = true;
        newPosition = position;
        //down
        while (canMove) {
            newPosition = down(newPosition);
            if (newPosition != null) {
                if (board.getPiece(newPosition) != null) {
                    ChessPiece blockage =board.getPiece(newPosition);
                    if (blockage.getTeamColor() == this.color) {
                        break;
                    }
                    else {
                        canMove = false;
                    }
                }
                validMoves.add(new ChessMove(position, newPosition, null));
            }
            else canMove = false;
        }

        canMove = true;
        newPosition = position;
        //left
        while (canMove) {
            newPosition = left(newPosition);
            if (newPosition != null) {
                if (board.getPiece(newPosition) != null) {
                    ChessPiece blockage =board.getPiece(newPosition);
                    if (blockage.getTeamColor() == this.color) {
                        break;
                    }
                    else {
                        canMove = false;
                    }
                }
                validMoves.add(new ChessMove(position, newPosition, null));
            }
            else canMove = false;
        }

        canMove = true;
        newPosition = position;
        //right
        while (canMove) {
            newPosition = right(newPosition);
            if (newPosition != null) {
                if (board.getPiece(newPosition) != null) {
                    ChessPiece blockage =board.getPiece(newPosition);
                    if (blockage.getTeamColor() == this.color) {
                        break;
                    }
                    else {
                        canMove = false;
                    }
                }
                validMoves.add(new ChessMove(position, newPosition, null));
            }
            else canMove = false;
        }
        return validMoves;
    };
    private Collection<ChessMove> pawnMoves(ChessBoard board, ChessPosition position) {
        Collection<ChessMove> validMoves = new HashSet<>();
        ChessPosition newPosition = position;

        if (color == ChessGame.TeamColor.WHITE) {
            //if first move
            newPosition = position;
            if (position.getRow() == 2) {
                for (int i = 0; i < 2; i++) {
                    newPosition = up(newPosition);
                    if (newPosition != null) {
                        if (board.getPiece(newPosition) != null) {
                            break;
                        } else {
                            validMoves.add(new ChessMove(position, newPosition, null));
                        }
                    }
                }
            }
            else {
                newPosition = up(position);
                if (newPosition != null) {
                    if (board.getPiece(newPosition) == null) {
                        //if end of board
                        if (newPosition.getRow() == 8) {
                            validMoves.add(new ChessMove(position, newPosition, PieceType.QUEEN));
                            validMoves.add(new ChessMove(position, newPosition, PieceType.BISHOP));
                            validMoves.add(new ChessMove(position, newPosition, PieceType.KNIGHT));
                            validMoves.add(new ChessMove(position, newPosition, PieceType.ROOK));
                        }
                        else {
                            validMoves.add(new ChessMove(position, newPosition, null));
                        }
                    }
                }
            }
            //if can capture
            newPosition = upRight(position);
            if (newPosition != null) {
                if (board.getPiece(newPosition) != null) {
                    ChessPiece blockage=board.getPiece(newPosition);
                    if (blockage.getTeamColor() != this.color) {
                        //if end of board
                        if (newPosition.getRow() == 8) {
                            validMoves.add(new ChessMove(position, newPosition, PieceType.QUEEN));
                            validMoves.add(new ChessMove(position, newPosition, PieceType.BISHOP));
                            validMoves.add(new ChessMove(position, newPosition, PieceType.KNIGHT));
                            validMoves.add(new ChessMove(position, newPosition, PieceType.ROOK));
                        }
                        else {
                            validMoves.add(new ChessMove(position, newPosition, null));
                        }
                    }
                }
            }
            newPosition = upLeft(position);
            if (newPosition != null) {
                if (board.getPiece(newPosition) != null) {
                    ChessPiece blockage=board.getPiece(newPosition);
                    if (blockage.getTeamColor() != this.color) {
                        //if end of board
                        if (newPosition.getRow() == 8) {
                            validMoves.add(new ChessMove(position, newPosition, PieceType.QUEEN));
                            validMoves.add(new ChessMove(position, newPosition, PieceType.BISHOP));
                            validMoves.add(new ChessMove(position, newPosition, PieceType.KNIGHT));
                            validMoves.add(new ChessMove(position, newPosition, PieceType.ROOK));
                        }
                        else {
                            validMoves.add(new ChessMove(position, newPosition, null));
                        }
                    }
                }
            }
        }
        if (color == ChessGame.TeamColor.BLACK) {
            //if first move
            newPosition = position;
            if (position.getRow() == 7) {
                for (int i = 0; i < 2; i++) {
                    newPosition = down(newPosition);
                    if (newPosition != null) {
                        if (board.getPiece(newPosition) != null) {
                            break;
                        }
                        else {
                            validMoves.add(new ChessMove(position, newPosition, null));
                        }
                    }
                }
            }
            else {
                newPosition = down(position);
                if (newPosition != null) {
                    if (board.getPiece(newPosition) == null) {
                        //if end of board
                        if (newPosition.getRow() == 1) {
                            validMoves.add(new ChessMove(position, newPosition, PieceType.QUEEN));
                            validMoves.add(new ChessMove(position, newPosition, PieceType.BISHOP));
                            validMoves.add(new ChessMove(position, newPosition, PieceType.KNIGHT));
                            validMoves.add(new ChessMove(position, newPosition, PieceType.ROOK));
                        }
                        else {
                            validMoves.add(new ChessMove(position, newPosition, null));
                        }
                    }
                }
            }
            //if can capture
            newPosition = downRight(position);
            if (newPosition != null) {
                if (board.getPiece(newPosition) != null) {
                    ChessPiece blockage=board.getPiece(newPosition);
                    if (blockage.getTeamColor() != this.color) {
                        //if end of board
                        if (newPosition.getRow() == 1) {
                            validMoves.add(new ChessMove(position, newPosition, PieceType.QUEEN));
                            validMoves.add(new ChessMove(position, newPosition, PieceType.BISHOP));
                            validMoves.add(new ChessMove(position, newPosition, PieceType.KNIGHT));
                            validMoves.add(new ChessMove(position, newPosition, PieceType.ROOK));
                        }
                        else {
                            validMoves.add(new ChessMove(position, newPosition, null));
                        }
                    }
                }
            }
            newPosition = downLeft(position);
            if (newPosition != null) {
                if (board.getPiece(newPosition) != null) {
                    ChessPiece blockage=board.getPiece(newPosition);
                    if (blockage.getTeamColor() != this.color) {
                        //if end of board
                        if (newPosition.getRow() == 1) {
                            validMoves.add(new ChessMove(position, newPosition, PieceType.QUEEN));
                            validMoves.add(new ChessMove(position, newPosition, PieceType.BISHOP));
                            validMoves.add(new ChessMove(position, newPosition, PieceType.KNIGHT));
                            validMoves.add(new ChessMove(position, newPosition, PieceType.ROOK));
                        }
                        else {
                            validMoves.add(new ChessMove(position, newPosition, null));
                        }
                    }
                }
            }
        }
        return validMoves;
    };

    private ChessPosition upRight(ChessPosition currPosition) {
        if ((currPosition.getRow() >= 8) || (currPosition.getColumn() >= 8)) return null;
        return new ChessPosition(currPosition.getRow()+1, currPosition.getColumn()+1);
    }
    private ChessPosition upLeft(ChessPosition currPosition) {
        if ((currPosition.getRow() >= 8) || (currPosition.getColumn() <= 1)) return null;
        return new ChessPosition(currPosition.getRow()+1, currPosition.getColumn()-1);
    }
    private ChessPosition downLeft(ChessPosition currPosition) {
        if ((currPosition.getRow() <= 1) || (currPosition.getColumn() <= 1)) return null;
        return new ChessPosition(currPosition.getRow()-1, currPosition.getColumn()-1);
    }
    private ChessPosition downRight(ChessPosition currPosition) {
        if ((currPosition.getRow() <= 1) || (currPosition.getColumn() >= 8)) return null;
        return new ChessPosition(currPosition.getRow()-1, currPosition.getColumn()+1);
    }
    private ChessPosition up(ChessPosition currPosition) {
        if (currPosition.getRow() >= 8) return null;
        return new ChessPosition(currPosition.getRow()+1, currPosition.getColumn());
    }
    private ChessPosition down(ChessPosition currPosition) {
        if (currPosition.getRow() <= 1) return null;
        return new ChessPosition(currPosition.getRow()-1, currPosition.getColumn());
    }
    private ChessPosition right(ChessPosition currPosition) {
        if (currPosition.getColumn() >= 8) return null;
        return new ChessPosition(currPosition.getRow(), currPosition.getColumn()+1);
    }
    private ChessPosition left(ChessPosition currPosition) {
        if (currPosition.getColumn() <= 1) return null;
        return new ChessPosition(currPosition.getRow(), currPosition.getColumn()-1);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof ChessPiece piece)) return false;
        return color == piece.color && pieceType == piece.pieceType;
    }

    @Override
    public int hashCode() {
        return Objects.hash(color, pieceType);
    }
}


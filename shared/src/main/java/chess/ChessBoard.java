package chess;

import java.util.Arrays;

/**
 * A chessboard that can hold and rearrange chess pieces.
 * <p>
 * Note: You can add to this class, but you may not alter
 * signature of the existing methods.
 */
public class ChessBoard {
    ChessPiece[][] board;
    private ChessPosition whiteKingPos;
    private ChessPosition blackKingPos;

    public ChessBoard() {
        //board constructor should not set up the board
        this.board = new ChessPiece[8][8];
    }

    /**
     * Adds a chess piece to the chessboard
     *
     * @param position where to add the piece to
     * @param piece    the piece to add
     */
    public void addPiece(ChessPosition position, ChessPiece piece) {
        board[position.getRow() - 1][position.getColumn() - 1]=piece;
        if (piece.getPieceType() == ChessPiece.PieceType.KING) {
            switch (piece.getTeamColor()) {
                case WHITE -> setWhiteKingPos(position);
                case BLACK -> setBlackKingPos(position);
            }
        }
    }

    /**
     * Gets a chess piece on the chessboard
     *
     * @param position The position to get the piece from
     * @return Either the piece at the position, or null if no piece is at that
     * position
     */
    public ChessPiece getPiece(ChessPosition position) {
        return board[position.getRow() - 1][position.getColumn() - 1];
    }

    /**
     * Sets the board to the default starting board
     * (How the game of chess normally starts)
     */
    public void resetBoard() {
        //Clear current board
        clearBoard();
        //fill in pawns
        for (int i = 1; i <= 8; i++) {
            ChessPosition position1 = new ChessPosition(2, i);
            ChessPosition position2 = new ChessPosition(7, i);
            addPiece(position1, new ChessPiece((ChessGame.TeamColor.WHITE), ChessPiece.PieceType.PAWN));
            addPiece(position2, new ChessPiece((ChessGame.TeamColor.BLACK), ChessPiece.PieceType.PAWN));
        }
        //Add Rooks
        addPiece(new ChessPosition(1, 1), new ChessPiece(ChessGame.TeamColor.WHITE, ChessPiece.PieceType.ROOK));
        addPiece(new ChessPosition(1, 8), new ChessPiece(ChessGame.TeamColor.WHITE, ChessPiece.PieceType.ROOK));
        addPiece(new ChessPosition(8, 1), new ChessPiece(ChessGame.TeamColor.BLACK, ChessPiece.PieceType.ROOK));
        addPiece(new ChessPosition(8, 8), new ChessPiece(ChessGame.TeamColor.BLACK, ChessPiece.PieceType.ROOK));
        //Add Knights
        addPiece(new ChessPosition(1, 2), new ChessPiece(ChessGame.TeamColor.WHITE, ChessPiece.PieceType.KNIGHT));
        addPiece(new ChessPosition(1, 7), new ChessPiece(ChessGame.TeamColor.WHITE, ChessPiece.PieceType.KNIGHT));
        addPiece(new ChessPosition(8, 2), new ChessPiece(ChessGame.TeamColor.BLACK, ChessPiece.PieceType.KNIGHT));
        addPiece(new ChessPosition(8, 7), new ChessPiece(ChessGame.TeamColor.BLACK, ChessPiece.PieceType.KNIGHT));
        //Add Bishop
        addPiece(new ChessPosition(1, 3), new ChessPiece(ChessGame.TeamColor.WHITE, ChessPiece.PieceType.BISHOP));
        addPiece(new ChessPosition(1, 6), new ChessPiece(ChessGame.TeamColor.WHITE, ChessPiece.PieceType.BISHOP));
        addPiece(new ChessPosition(8, 3), new ChessPiece(ChessGame.TeamColor.BLACK, ChessPiece.PieceType.BISHOP));
        addPiece(new ChessPosition(8, 6), new ChessPiece(ChessGame.TeamColor.BLACK, ChessPiece.PieceType.BISHOP));
        //Add Queens
        addPiece(new ChessPosition(1,4), new ChessPiece(ChessGame.TeamColor.WHITE, ChessPiece.PieceType.QUEEN));
        addPiece(new ChessPosition(8,4), new ChessPiece(ChessGame.TeamColor.BLACK, ChessPiece.PieceType.QUEEN));
        //Add Kings
        addPiece(new ChessPosition(1, 5), new ChessPiece(ChessGame.TeamColor.WHITE, ChessPiece.PieceType.KING));
        addPiece(new ChessPosition(8, 5), new ChessPiece(ChessGame.TeamColor.BLACK, ChessPiece.PieceType.KING));
    }

    private void clearBoard() {
        for (int i = 0; i < board.length; i++) {
            for (int j = 0; j < board.length; j++) {
                if (board[i][j] != null) {
                    removePiece(new ChessPosition(i, j));
                }
            }
        }
    }
    public void removePiece(ChessPosition position) {
        if (board[position.getRow() - 1][position.getColumn() - 1] != null) {
            board[position.getRow() - 1][position.getColumn() - 1] = null;
        }
    }

    public ChessPosition getWhiteKingPos() {
        return whiteKingPos;
    }

    public void setWhiteKingPos(ChessPosition whiteKingPos) {
        this.whiteKingPos=whiteKingPos;
    }

    public ChessPosition getBlackKingPos() {
        return blackKingPos;
    }

    public void setBlackKingPos(ChessPosition blackKingPos) {
        this.blackKingPos=blackKingPos;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof ChessBoard that)) return false;
        return Arrays.deepEquals(board, that.board);
    }

    @Override
    public int hashCode() {
        return Arrays.hashCode(board);
    }

    @Override
    public String toString() {
        String printBoard = "";
        for (int i = 0; i < 8; i++) {
            String row = "";
            for (int j = 0; j  < 8; j++) {
                row += "|" + this.board[i][j]+ "|";
            }
            printBoard= row  +"\n" + printBoard;
        }
        return "print board: " +
                printBoard;
    }
}
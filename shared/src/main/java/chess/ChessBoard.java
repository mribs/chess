package chess;

/**
 * A chessboard that can hold and rearrange chess pieces.
 * <p>
 * Note: You can add to this class, but you may not alter
 * signature of the existing methods.
 */
public class ChessBoard {
    ChessPiece[][] board = new ChessPiece[8][8];


    public ChessBoard() {
        //board constructor should set up the board
        
    }

    /**
     * Adds a chess piece to the chessboard
     *
     * @param position where to add the piece to
     * @param piece    the piece to add
     */
    public void addPiece(ChessPosition position, ChessPiece piece) {
        board[position.getRow() - 1][position.getColumn() - 1] = piece;
////    System.out.println("added " + piece.getPieceType() + " at " + position.getRow() + ", " + position.getColumn());
//        if (piece.getPieceType() == ChessPiece.PieceType.KING) {
//            switch (piece.getTeamColor()) {
//                case WHITE -> setWhiteKingPos(position);
//                case BLACK -> setBlackKingPos(position);
//            }
////      System.out.println("king is at " + position.toString());
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
        throw new RuntimeException("Not implemented");
    }
}

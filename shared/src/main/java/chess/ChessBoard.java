package chess;

import org.junit.jupiter.api.Assertions;

import java.util.Arrays;
import java.util.Objects;

/**
 * A chessboard that can hold and rearrange chess pieces.
 * <p>
 * Note: You can add to this class, but you may not alter
 * signature of the existing methods.
 */
public class ChessBoard {

    //mine: creating a 2d array for the board and they start at 1 and we need a 64 board
    private ChessPiece[][] board = new ChessPiece[9][9];
    //end mine
    public ChessBoard() {

    }

    /**
     * Adds a chess piece to the chessboard
     *
     * @param position where to add the piece to
     * @param piece    the piece to add
     */
    public void addPiece(ChessPosition position, ChessPiece piece) {
        //mine
        board[position.getRow()-1][position.getColumn()-1] = piece;
        //mine, like the birds in nemo
    }

    /**
     * Gets a chess piece on the chessboard
     *
     * @param position The position to get the piece from
     * @return Either the piece at the position, or null if no piece is at that
     * position
     */
    public ChessPiece getPiece(ChessPosition position) {
        //mine
        return board[position.getRow()-1][position.getColumn()-1];
        // mine throw new RuntimeException("Not implemented");
    }
//mine
    @Override
    public boolean equals(Object objjj) {
        if (objjj == null || getClass() != objjj.getClass()) {
            return false;
        }
        //objjj is being cast into "that"
        ChessBoard that = (ChessBoard) objjj;
//        for (int row = 1; row <= 8; row++) {
//            for (int col = 1; col <= 8; col++) {
//                ChessPosition pos = new ChessPosition(row,col);
//                if (getPiece(pos) != that.getPiece(pos)) {
//                    return false;
//                }
//            }
//            }
            return Objects.deepEquals(board, that.board);
            //returns yes if the contents are also equal

        //return true;
    }
/// we dont need the override but its helpful to override the defaut definition of hashcode or equal because it would have ran without this
/// we do the same with toString
/// default equal checks if theyre literally the same thing
/// Hashcode creates a hash data structure, deephashcode hashes the stuff inside the array
    @Override
    public int hashCode() {

        return Arrays.deepHashCode(board);
    }
//mine
    /**
     * Sets the board to the default starting board
     * (How the game of chess normally starts)
     */
    public void resetBoard() {
       //supposed to set up board to default state
        //mine
        board = new ChessPiece[9][9];
        //mine
    }
}

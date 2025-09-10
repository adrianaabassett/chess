package chess;

import java.util.Objects;

/**
 * Represents a single square position on a chess board
 * <p>
 * Note: You can add to this class, but you may not alter
 * signature of the existing methods.
 */
public class ChessPosition {

    //mine
    private final int row;
    private final int col;
    //the final makes it unchangable, it cant be assigned to a new number
    //each instance of this cannot be changed
    //mine

    public ChessPosition(int row, int col) {
        //mine
        this.row = row;
        this.col = col;
        //mine
    }

    /**
     * @return which row this position is in
     * 1 codes for the bottom row
     */
    public int getRow() {
       //mine
        return row;
        //mine
    }

    /**
     * @return which column this position is in
     * 1 codes for the left row
     */
    public int getColumn() {
       //mine
        return col;
        //mine
    }

    //mine

    @Override
    public boolean equals(Object objj) {
        if (objj == null || getClass() != objj.getClass()) {
            return false;
        }
        ChessPosition that = (ChessPosition) objj;
        return row == that.row && col == that.col;
    }

    @Override
    public int hashCode() {
        return Objects.hash(row, col);
    }

    //mine


}

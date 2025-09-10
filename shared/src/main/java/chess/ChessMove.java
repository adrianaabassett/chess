package chess;

import java.util.Objects;

/**
 * Represents moving a chess piece on a chessboard
 * <p>
 * Note: You can add to this class, but you may not alter
 * signature of the existing methods.
 */
public class ChessMove {
    //mine
    public final ChessPosition startPosition;
    public final ChessPosition endPosition;
    public final ChessPiece.PieceType promotionPiece;

    //mine
    public ChessMove(ChessPosition startPosition, ChessPosition endPosition,
                     ChessPiece.PieceType promotionPiece) {
        //mine this makes sure to reference the earlier startPosition, not the parameter
            this.startPosition = startPosition;
            this.endPosition = endPosition;
            this.promotionPiece = promotionPiece;

        //mine
    }

    /**
     * @return ChessPosition of starting location
     */
    public ChessPosition getStartPosition() {
        //mine
        return startPosition;
        // end mine throw new RuntimeException("Not implemented");
    }

    /**
     * @return ChessPosition of ending location
     */
    public ChessPosition getEndPosition() {
        //mine
        return endPosition;
        //mine
    }

    /**
     * Gets the type of piece to promote a pawn to if pawn promotion is part of this
     * chess move
     *
     * @return Type of piece to promote a pawn to, or null if no promotion
     */
    public ChessPiece.PieceType getPromotionPiece() {
        //mine
        return promotionPiece;
        //mine
    }

    //mine


    //mine
    @Override
    public boolean equals(Object objj) {
        if (objj == null || getClass() != objj.getClass()) {
            return false;
        }
        ChessMove chessMove = (ChessMove) objj;
        return Objects.equals(startPosition, chessMove.startPosition) && Objects.equals(endPosition, chessMove.endPosition) && promotionPiece == chessMove.promotionPiece;
    }

    @Override
    public int hashCode() {
        return Objects.hash(startPosition, endPosition, promotionPiece);
    }
    //mine

    @Override
    public String toString() {
        return "ChessMove{" + "startPosition=" + startPosition + ", end Position:" + endPosition + ", promotionPieces" + promotionPiece + "}";
    }
    //endmine
}

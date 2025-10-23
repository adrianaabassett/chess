package chess;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Objects;

/**
 * Represents a single chess piece
 * <p>
 * Note: You can add to this class, but you may not alter
 * signature of the existing methods.
 */
public class ChessPiece {

    //mine

    private final ChessGame.TeamColor pieceColor;
    private final ChessPiece.PieceType pieceType;
    //mine

    public ChessPiece(ChessGame.TeamColor pieceColor,
                      ChessPiece.PieceType type) {
        this.pieceColor = pieceColor;
        pieceType = type;
    }

    /**
     * The various different chess piece options
     */
    //enum is a primitive that describes the values for something
        //enum, enumeration is setting aside values to be PieceType
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
        //mine
        return pieceColor;
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

    public Collection<ChessMove> pawnPieceMoves(ChessBoard board, ChessPosition myPosition){
        int row = myPosition.getRow();
        Collection<ChessMove> colle = new ArrayList<>();
        int col = myPosition.getColumn();
        if (pieceType.equals(PieceType.PAWN) && pieceColor.equals(pieceColor.WHITE)){
            if(checkCANmove(board,row+1, col)){
                if(row >= 7){
                    colle.add(new ChessMove(myPosition, new ChessPosition(row+1,col), PieceType.QUEEN));
                    colle.add(new ChessMove(myPosition, new ChessPosition(row+1,col), PieceType.KNIGHT));
                    colle.add(new ChessMove(myPosition, new ChessPosition(row+1,col), PieceType.ROOK));
                    colle.add(new ChessMove(myPosition, new ChessPosition(row+1,col), PieceType.BISHOP));
                }
                else{colle.add(new ChessMove(myPosition, new ChessPosition(row+1,col), null));
                }
                if(row == 2 && checkCANmove(board,row+2, col)){
                    colle.add(new ChessMove(myPosition, new ChessPosition(row+2, col),null));}}
            if(checkFORenemy(board, row+1, col-1)) {
                if(row >= 7){
                    colle.add(new ChessMove(myPosition, new ChessPosition(row+1,col-1), PieceType.QUEEN));
                    colle.add(new ChessMove(myPosition, new ChessPosition(row+1,col-1), PieceType.KNIGHT));
                    colle.add(new ChessMove(myPosition, new ChessPosition(row+1,col-1), PieceType.ROOK));
                    colle.add(new ChessMove(myPosition, new ChessPosition(row+1,col-1), PieceType.BISHOP));
                }
                else{colle.add(new ChessMove(myPosition, new ChessPosition(row+1,col-1), null));
                }
            }
            //right
            if(checkFORenemy(board, row+1, col+1)) {
                if(row >= 7){
                    colle.add(new ChessMove(myPosition, new ChessPosition(row+1,col+1), PieceType.QUEEN));
                    colle.add(new ChessMove(myPosition, new ChessPosition(row+1,col+1), PieceType.KNIGHT));
                    colle.add(new ChessMove(myPosition, new ChessPosition(row+1,col+1), PieceType.ROOK));
                    colle.add(new ChessMove(myPosition, new ChessPosition(row+1,col+1), PieceType.BISHOP));
                }
                else{colle.add(new ChessMove(myPosition, new ChessPosition(row+1,col+1), null));}}
            if(checkFORenemy(board,row,col+1) && row == 5 && checkCANmove(board,row+1,col+1)){
                colle.add(new ChessMove(myPosition, new ChessPosition(row+1,col+1), null));}
            if(checkFORenemy(board,row,col-1) && row == 5 && checkCANmove(board,row+1,col-1) ){
                colle.add(new ChessMove(myPosition, new ChessPosition(row+1,col-1), null));}}
        if (pieceType.equals(PieceType.PAWN) && pieceColor.equals(pieceColor.BLACK)){
            if(checkCANmove(board,row-1, col)){
                if(row <= 2){
                    colle.add(new ChessMove(myPosition, new ChessPosition(row-1,col), PieceType.QUEEN));
                    colle.add(new ChessMove(myPosition, new ChessPosition(row-1,col), PieceType.KNIGHT));
                    colle.add(new ChessMove(myPosition, new ChessPosition(row-1,col), PieceType.ROOK));
                    colle.add(new ChessMove(myPosition, new ChessPosition(row-1,col), PieceType.BISHOP));
                }
                else{colle.add(new ChessMove(myPosition, new ChessPosition(row-1,col),null));}
                if(row == 7 && checkCANmove(board,row-2, col)){
                    colle.add(new ChessMove(myPosition, new ChessPosition(row-2, col),null));
                }}
            if(checkFORenemy(board, row-1, col-1)) {
                if(row <= 2){
                    colle.add(new ChessMove(myPosition, new ChessPosition(row-1,col-1), PieceType.QUEEN));
                    colle.add(new ChessMove(myPosition, new ChessPosition(row-1,col-1), PieceType.KNIGHT));
                    colle.add(new ChessMove(myPosition, new ChessPosition(row-1,col-1), PieceType.ROOK));
                    colle.add(new ChessMove(myPosition, new ChessPosition(row-1,col-1), PieceType.BISHOP));
                }
                else{colle.add(new ChessMove(myPosition, new ChessPosition(row-1,col-1), null));}
            }
            if(checkFORenemy(board, row-1, col+1)) {
                if(row <= 2){
                    colle.add(new ChessMove(myPosition, new ChessPosition(row-1,col+1), PieceType.QUEEN));
                    colle.add(new ChessMove(myPosition, new ChessPosition(row-1,col+1), PieceType.KNIGHT));
                    colle.add(new ChessMove(myPosition, new ChessPosition(row-1,col+1), PieceType.ROOK));
                    colle.add(new ChessMove(myPosition, new ChessPosition(row-1,col+1), PieceType.BISHOP));
                }
                else{colle.add(new ChessMove(myPosition, new ChessPosition(row-1,col+1), null));
                }
            }
            if(checkFORenemy(board,row,col+1) && board.getPiece(new ChessPosition(row, col+1)).getPieceType() == PieceType.PAWN && row == 4 && checkCANmove(board,row-1,col+1) ){
                colle.add(new ChessMove(myPosition, new ChessPosition(row-1,col+1), null));}
            if(checkFORenemy(board,row,col-1) && row == 4 && checkCANmove(board,row-1,col-1) ){
                colle.add(new ChessMove(myPosition, new ChessPosition(row-1,col-1), null));}}
        if (pieceType.equals(PieceType.KING)){
            if(checkCANmove(board,row+1, col) || checkFORenemy(board,row+1, col)){
                colle.add(new ChessMove(myPosition, new ChessPosition(row+1,col),null));
            }
            if(checkCANmove(board,row+1, col+1)||checkFORenemy(board, row+1,col+1)){
                colle.add(new ChessMove(myPosition, new ChessPosition(row+1,col+1),null));
            }
            if(checkCANmove(board,row, col+1)||checkFORenemy(board,row,col+1)){
                colle.add(new ChessMove(myPosition, new ChessPosition(row,col+1),null));
            }
            if(checkCANmove(board,row-1, col+1)||checkFORenemy(board,row-1,col+1)){
                colle.add(new ChessMove(myPosition, new ChessPosition(row-1,col+1),null));
            }
            if(checkCANmove(board,row-1, col)||checkFORenemy(board,row-1,col)){
                colle.add(new ChessMove(myPosition, new ChessPosition(row-1,col),null));
            }
            if(checkCANmove(board,row-1, col-1)||checkFORenemy(board,row-1,col-1)){
                colle.add(new ChessMove(myPosition, new ChessPosition(row-1,col-1),null));
            }
            if(checkCANmove(board,row, col-1)||checkFORenemy(board,row, col-1)){
                colle.add(new ChessMove(myPosition, new ChessPosition(row,col-1),null));
            }
            if(checkCANmove(board,row+1, col-1)||checkFORenemy(board,row+1,col-1)){
                colle.add(new ChessMove(myPosition, new ChessPosition(row+1,col-1),null));
            }
        }
        return colle;
    }
    public Collection<ChessMove> pieceMoves(ChessBoard board, ChessPosition myPosition) {
        int row = myPosition.getRow();
        int col = myPosition.getColumn();
        Collection<ChessMove> colle = new ArrayList<>();
        if (pieceType.equals(PieceType.PAWN)){
           return pawnPieceMoves(board, myPosition);
        }
        if (pieceType.equals(PieceType.ROOK)){
            int newro = row;
            int newco = col;
            while (checkCANmove(board,newro+1, newco)) {
                newro++;
                colle.add(new ChessMove(myPosition, new ChessPosition(newro, newco), null));
            }
            if(checkFORenemy(board,newro+1,newco)){
                colle.add(new ChessMove(myPosition, new ChessPosition(newro+1, newco), null));
            }
            newro = row;
            newco = col;
            while (checkCANmove(board,newro-1, newco)){ newro--;
                colle.add(new ChessMove(myPosition, new ChessPosition(newro,newco),null));
            }
            if(checkFORenemy(board,newro-1,newco)){
                colle.add(new ChessMove(myPosition, new ChessPosition(newro-1, newco), null));
            }
            newro = row;
            newco = col;
            while (checkCANmove(board,newro, newco+1)){newco++;
                colle.add(new ChessMove(myPosition, new ChessPosition(newro,newco),null));
            }
            if(checkFORenemy(board,newro,newco+1)){
                colle.add(new ChessMove(myPosition, new ChessPosition(newro, newco+1), null));
            }
            newro = row;
            newco = col;
            while (checkCANmove(board,newro, newco-1)){newco--;
                colle.add(new ChessMove(myPosition, new ChessPosition(newro,newco),null));
            }
            if(checkFORenemy(board,newro,newco-1)){
                colle.add(new ChessMove(myPosition, new ChessPosition(newro, newco-1), null));
            }
        }

        if (pieceType.equals(PieceType.BISHOP)){
            int newro = row;
            int newco = col;
            while (checkCANmove(board,newro+1, newco+1)){
                newro++;
                newco++;
                colle.add(new ChessMove(myPosition, new ChessPosition(newro,newco),null));
            }
            if(checkFORenemy(board,newro+1, newco+1)){colle.add(new ChessMove(myPosition, new ChessPosition(newro+1, newco+1),null));}
            newro = row;
            newco = col;
            while (checkCANmove(board,newro-1, newco-1)){
                newro--;
                newco--;
                colle.add(new ChessMove(myPosition, new ChessPosition(newro,newco),null));
            }
            if(checkFORenemy(board,newro-1, newco-1)){colle.add(new ChessMove(myPosition, new ChessPosition(newro-1, newco-1),null));}

            newro = row;
            newco = col;
            while (checkCANmove(board,newro-1, newco+1)){
                newco++;
                newro--;
                colle.add(new ChessMove(myPosition, new ChessPosition(newro,newco),null));
            }
            if(checkFORenemy(board,newro-1, newco+1)){colle.add(new ChessMove(myPosition, new ChessPosition(newro-1, newco+1),null));}

            newro = row;
            newco = col;
            while (checkCANmove(board,newro+1, newco-1)){
                newco--;
                newro++;
                colle.add(new ChessMove(myPosition, new ChessPosition(newro,newco),null));
            }
            if(checkFORenemy(board,newro+1, newco-1)){colle.add(new ChessMove(myPosition, new ChessPosition(newro+1, newco-1),null));}

        }

        if (pieceType.equals(PieceType.QUEEN)){
                int newrow = row;
                int newco = col;
                while (checkCANmove(board,newrow+1, newco)) {
                    newrow++;
                    colle.add(new ChessMove(myPosition, new ChessPosition(newrow, newco), null));
                }
                if(checkFORenemy(board,newrow+1,newco)){
                    colle.add(new ChessMove(myPosition, new ChessPosition(newrow+1, newco), null));
                }
                newrow = row;
                newco = col;
                while (checkCANmove(board,newrow-1, newco)){ newrow--;
                    colle.add(new ChessMove(myPosition, new ChessPosition(newrow,newco),null));
                }
                if(checkFORenemy(board,newrow-1,newco)){
                    colle.add(new ChessMove(myPosition, new ChessPosition(newrow-1, newco), null));
                }
                newrow = row;
                newco = col;
                while (checkCANmove(board,newrow, newco+1)){newco++;
                    colle.add(new ChessMove(myPosition, new ChessPosition(newrow,newco),null));
                }
                if(checkFORenemy(board,newrow,newco+1)){
                    colle.add(new ChessMove(myPosition, new ChessPosition(newrow, newco+1), null));
                }
                newrow = row;
                newco = col;
                while (checkCANmove(board,newrow, newco-1)){newco--;
                    colle.add(new ChessMove(myPosition, new ChessPosition(newrow,newco),null));
                }
                if(checkFORenemy(board,newrow,newco-1)){
                    colle.add(new ChessMove(myPosition, new ChessPosition(newrow, newco-1), null));
                }
                newrow = row;
                newco = col;
                while (checkCANmove(board,newrow+1, newco+1)){
                    newrow++;
                    newco++;
                    colle.add(new ChessMove(myPosition, new ChessPosition(newrow,newco),null));
                }
                if(checkFORenemy(board,newrow+1, newco+1)){colle.add(new ChessMove(myPosition, new ChessPosition(newrow+1, newco+1),null));}
                newrow = row;
                newco = col;
                while (checkCANmove(board,newrow-1, newco-1)){
                    newrow--;
                    newco--;
                    colle.add(new ChessMove(myPosition, new ChessPosition(newrow,newco),null));
                }
                if(checkFORenemy(board,newrow-1, newco-1)){colle.add(new ChessMove(myPosition, new ChessPosition(newrow-1, newco-1),null));}

                newrow = row;
                newco = col;
                while (checkCANmove(board,newrow-1, newco+1)){
                    newco++;
                    newrow--;
                    colle.add(new ChessMove(myPosition, new ChessPosition(newrow,newco),null));
                }
                if(checkFORenemy(board,newrow-1, newco+1)){colle.add(new ChessMove(myPosition, new ChessPosition(newrow-1, newco+1),null));}

                newrow = row;
                newco = col;
                while (checkCANmove(board,newrow+1, newco-1)){
                    newco--;
                    newrow++;
                    colle.add(new ChessMove(myPosition, new ChessPosition(newrow,newco),null));
                }
                if(checkFORenemy(board,newrow+1, newco-1)){colle.add(new ChessMove(myPosition, new ChessPosition(newrow+1, newco-1),null));}




        }

        if (pieceType.equals(PieceType.KNIGHT)){
            if(checkCANmove(board,row+1, col+2)||checkFORenemy(board,row+1, col+2)){colle.add(new ChessMove(myPosition, new ChessPosition(row+1,col+2),null));}
            if(checkCANmove(board,row-1, col+2)||checkFORenemy(board,row-1, col+2)){colle.add(new ChessMove(myPosition, new ChessPosition(row-1,col+2),null));}
            if(checkCANmove(board,row+1, col-2)||checkFORenemy(board,row+1, col-2)){colle.add(new ChessMove(myPosition, new ChessPosition(row+1,col-2),null));}
            if(checkCANmove(board,row-1, col-2)||checkFORenemy(board,row-1, col-2)){colle.add(new ChessMove(myPosition, new ChessPosition(row-1,col-2),null));}
            if(checkCANmove(board,row+2, col+1)||checkFORenemy(board,row+2, col+1)){colle.add(new ChessMove(myPosition, new ChessPosition(row+2,col+1),null));}
            if(checkCANmove(board,row-2, col+1)||checkFORenemy(board,row-2, col+1)){colle.add(new ChessMove(myPosition, new ChessPosition(row-2,col+1),null));}
            if(checkCANmove(board,row+2, col-1)||checkFORenemy(board,row+2, col-1)){colle.add(new ChessMove(myPosition, new ChessPosition(row+2,col-1),null));}
            if(checkCANmove(board,row-2, col-1)||checkFORenemy(board,row-2, col-1)){colle.add(new ChessMove(myPosition, new ChessPosition(row-2,col-1),null));}
        }

    return colle;
    }

    //mine
    public boolean checkFORenemy(ChessBoard board, int row, int col){
        //on board
        if (row<1 || row>8 || col<1 || col>8 ||board.getPiece(new ChessPosition(row, col)) == null){
            return false;
        }
        //same team
        if(board.getPiece(new ChessPosition(row, col)).getTeamColor().equals(pieceColor)){
            return false;
        }
        //not empty, on board, not same team, must be enemy
        return true;
    }
    //mine

        //mine
    public boolean checkCANmove(ChessBoard board, int row, int col){
        //on board
        if (row<1 || row>8 || col<1 || col>8){
            return false;
        }
        //empty
        if(board.getPiece(new ChessPosition(row, col)) == null) {
            return true;
        }

        if(board.getPiece(new ChessPosition(row, col)).getTeamColor().equals(pieceColor)){
            return false;
        }
        //not empty, on board, not same team, must be enemy
        return false;
    }
    //mine

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        ChessPiece that = (ChessPiece) o;
        //make sure its type AND color
        return pieceColor == that.pieceColor && pieceType == that.pieceType;
    }

    @Override
    public int hashCode() {
        //make sure its type AND color
        //also its just hash. hashcode accepts only one parameter, probably
        return Objects.hash(pieceColor, pieceType);
    }

    //mine
}

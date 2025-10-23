package chess;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Objects;

/**
 * For a class that can manage a chess game, making moves on a board
 * <p>
 * Note: You can add to this class, but you may not alter
 * signature of the existing methods.
 */
public class ChessGame {
    TeamColor teamTurn = TeamColor.WHITE;
    ChessBoard board = new ChessBoard();
    boolean isPassantNext = false;
    public ChessGame() {
        board.resetBoard();
    }

    /**
     * @return Which team's turn it is
     */
    public TeamColor getTeamTurn() {
        return teamTurn;
    }

    /**
     * Set's which teams turn it is
     *
     * @param team the team whose turn it is
     */
    public void setTeamTurn(TeamColor team) {
        teamTurn = team;
    }

    /**
     * Enum identifying the 2 possible teams in a chess game
     */
    public enum TeamColor {
        WHITE,
        BLACK
    }

    /**
     * Gets a valid moves for a piece at the given location
     *
     * @param startPosition the piece to get valid moves for
     * @return Set of valid moves for requested piece, or null if no piece at
     * startPosition
     */
    //like piece moves, but now accounting for check
    //basically removes all king pieces that would put it in check

    //every possible move that doesnt leave it in check of whatever piece we give it
    public Collection<ChessMove> validMoves(ChessPosition startPosition) {
       // boolean doublejump = false;
        Collection<ChessMove> moves = new ArrayList<>();
        TeamColor ourColor = board.getPiece(startPosition).getTeamColor();
        ChessBoard savingBoard = new ChessBoard();
        savingBoard = copyBoard(board);
        for(ChessMove current: board.getPiece(startPosition).pieceMoves(board,startPosition)){
            boolean jumpsIntoNull = board.getPiece(current.getEndPosition()) == null;
            board = copyBoard(savingBoard);
            ChessPiece testingPiece =board.getPiece(startPosition);
            board.addPiece(current.getEndPosition(),board.getPiece(startPosition));
            board.addPiece(startPosition,null);
            //en passant possible next turn if row +-2
            //if its a pawn
            if( testingPiece != null && testingPiece.getPieceType() == ChessPiece.PieceType.PAWN){

                //if its making a diagonal move then it must go left or right
                if(startPosition.getColumn()+1 == current.getEndPosition().getColumn()
                        ||  startPosition.getColumn() -1 == current.getEndPosition().getColumn()){
                    //where it jumps
                    //most likely change
                    if(jumpsIntoNull && isPassantNext){
                        moves.add(current);
                    }

                    else if (!jumpsIntoNull){
                        moves.add(current);
                    }
                }
                else if(!isInCheck(ourColor)){
                    moves.add(current);
                }
                //AND its going diagonally AND its an en passant move because its not landing on a piecee where end position is
                // AND its an en passant turn
            }
            else if(!isInCheck(ourColor)){
                //making sure it's not attempting a passant
                if(testingPiece!= null) {
                    if(testingPiece.getPieceType()!= ChessPiece.PieceType.PAWN ||
                            startPosition.getColumn() == current.getEndPosition().getColumn()) {
                        moves.add(current);
                    }
                }
                else{
                    moves.add(current);
                }

            }

        }
//        returning board to former state and returning moves
        board = copyBoard(savingBoard);

        return moves;
    }

    /**
     * Makes a move in a chess game
     *
     * @param move chess move to perform
     * @throws InvalidMoveException if move is invalid
     */
    public void makeMove(ChessMove move) throws InvalidMoveException {
        //this is permanent dont use it in valid
        //in check no piece take own piec
//        board.addPiece(move.getEndPosition(), board.getPiece(move.getStartPosition()));
//        board.addPiece(move.getStartPosition(),null);
        boolean doublejump = false;
        if (move.getStartPosition().getRow() +2== move.getEndPosition().getRow()
                ||move.getStartPosition().getRow() -2 == move.getEndPosition().getRow() )
        {doublejump = true;}
        if(move.getStartPosition() == null ||
                move.getEndPosition().getRow()>8||
                move.getEndPosition().getRow()<1||
                move.getEndPosition().getColumn()<1||
                move.getEndPosition().getColumn()>8||
                board.getPiece(move.getStartPosition()) == null ){
            throw new InvalidMoveException("you can't make this move off the board");}
        else{
            //if move is in all the valid moves
            boolean inList = false;
            for (ChessMove current:validMoves(move.getStartPosition())){
                if(current.equals(move)){
                    inList = true;
                }
            }
            if(!inList){
                throw new InvalidMoveException("not a valid move");
            }
            //inlist now describes whether it is in the list of valid moves
            if(inList && this.getTeamTurn() == board.getPiece(move.getStartPosition()).getTeamColor()){
            //now we can move it
                //en passant
                if(board.getPiece(move.getStartPosition()).getPieceType()==ChessPiece.PieceType.PAWN){
                    int row = move.getStartPosition().getRow();
                    int col = move.getStartPosition().getColumn();
                    int erow = move.getEndPosition().getRow();
                    int ecol= move.getEndPosition().getColumn();
                        //checks it moves up positive
                            //checks that it moves to the right, there is a piece next to it, and that the piece is on its own team
                            if(board.getPiece(move.getStartPosition()).getTeamColor() == TeamColor.WHITE
                                    && row == 5 && row +1 == erow && col+1 == ecol
                                    && board.getPiece(new ChessPosition(row,col+1))!=null
                                    && board.getPiece(new ChessPosition(row,col+1)).getTeamColor() == TeamColor.BLACK){
                                //removes the piece its passing
                                board.addPiece(new ChessPosition(row,col+1 ) ,null);
                            }
                            if(board.getPiece(move.getStartPosition()).getTeamColor() == TeamColor.WHITE
                    &&row == 5 && row +1 == erow && col-1 == ecol
                                    && board.getPiece(new ChessPosition(row,col-1))!=null
                                    && board.getPiece(new ChessPosition(row,col-1)).getTeamColor() == TeamColor.BLACK){
                                board.addPiece(new ChessPosition(row,col-1 ) ,null);


                    }
                        //checks it moves up positive
                            //checks that it moves to the right, there is a piece next to it, and that the piece is on its own team
                            if(board.getPiece(move.getStartPosition()).getTeamColor() == TeamColor.BLACK && row == 4
                                    && row -1 == erow&&col+1 == ecol
                                    && board.getPiece(new ChessPosition(row,col+1))!=null
                                    && board.getPiece(new ChessPosition(row,col+1)).getTeamColor() == TeamColor.WHITE){
                                board.addPiece(new ChessPosition(row,col+1 ) ,null);
                            }
                            if(board.getPiece(move.getStartPosition()).getTeamColor() == TeamColor.BLACK
                                    && row == 4 && row -1 == erow&&col-1 == ecol
                                    && board.getPiece(new ChessPosition(row,col-1))!=null
                                    && board.getPiece(new ChessPosition(row,col-1)).getTeamColor()
                                    == TeamColor.WHITE){
                                board.addPiece(new ChessPosition(row,col-1 ) ,null);
                            }

                }
                //end en passant
                if(move.getPromotionPiece() == null){
                    board.addPiece(move.getEndPosition(),board.getPiece(move.getStartPosition()));
                    board.addPiece(move.getStartPosition(),null);
                }
                else{
                 board.addPiece(move.getEndPosition(),
                         new ChessPiece(board.getPiece(move.getStartPosition()).getTeamColor(),move.getPromotionPiece()));
                 board.addPiece(move.getStartPosition(),null);
                }
                //change team color?
                if(this.getTeamTurn() == TeamColor.WHITE){
                    setTeamTurn(TeamColor.BLACK);
                }
                else{
                    setTeamTurn(TeamColor.WHITE);
                }
            }
            else
            {
                throw new InvalidMoveException("Are you sure its your turn");
            }
            // if en passant
        if(doublejump){isPassantNext = true;}
        else{isPassantNext = false;}


        //call makemoves in validmoves

        }


    }


    /**
     * Determines if the given team is in check
     *
     * @param teamColor which team to check for check
     * @return True if the specified team is in check
     */
    public boolean isInCheck(TeamColor teamColor) {
        ChessPosition kingPosition = whereKing(teamColor);
        //find which one contains the king
        return checkIfAttackable(teamColor, kingPosition);
        //go through all board
        //check if the piece is the same color
        //check if pieces possible moves include king
    }

    public boolean checkIfAttackable(TeamColor teamColor, ChessPosition kingPosition) {
        int row = 1;
        int col = 1;
        ChessPiece checkPiece;
        while(row<9){
            while (col<9){
                checkPiece = board.getPiece(new ChessPosition(row, col));
                if(unNestKing(checkPiece, board, row, col, kingPosition, teamColor)){
                    return true;
                }
                col++;
            }
            col =1;
            row++;
        }
        return false;
    }
    public boolean unNestKing(ChessPiece checkPiece, ChessBoard board, int row, int col,
                              ChessPosition kingPosition, TeamColor teamColor){
    if(checkPiece != null && checkPiece.pieceMoves(board, new ChessPosition(row, col))
            != null&& checkPiece.getTeamColor()!=teamColor) {
        //cycle through all possible moves of every piece;
        Collection<ChessMove> possibleMoves= checkPiece.pieceMoves(board, new ChessPosition(row, col));
        for(ChessMove current : possibleMoves){
            if (current.getEndPosition().equals(kingPosition)) {
                return true;
            }
            //game interviews usually have programming questions
            //every game company needs a different type of person
        }
    }
        return false;
    }

    public ChessPosition whereKing(TeamColor teamColor){
        int row = 1;
        int col = 1;
        ChessPiece thisKing;
        while(row<9){
            while (col<9){
                thisKing = board.getPiece(new ChessPosition(row, col));
                if (thisKing!= null && thisKing.getPieceType() == ChessPiece.PieceType.KING
                        && thisKing.getTeamColor() == teamColor) {
                    return new ChessPosition(row, col);
                }
                col++;
            }
            col = 1;
            row++;
        }
        return null;
    }

    /**
     * Determines if the given team is in checkmate
     *
     * @param teamColor which team to check for checkmate
     * @return True if the specified team is in checkmate
     */


    public boolean isInCheckmate(TeamColor teamColor) {
        if (!isInCheck(teamColor)) {
            return false;
            //no valid moves and its not in check then its a stalemate
        }
        int row = 1;
        int col = 1;
        while(row<9){
            while(col<9){
                if(board.getPiece(new ChessPosition(row, col)) != null){
                    if(board.getPiece(new ChessPosition(row, col)).getTeamColor()==teamColor
                            && validMoves( new ChessPosition(row, col)) != null
                            && !validMoves(new ChessPosition(row,col)).isEmpty()){
                            return false;
                    }
                }
                col++;
            }
            col=1;
            row++;
        }
        //do they not have any possible moves
        return true;
    }

    /**
     * Determines if the given team is in stalemate, which here is defined as having
     * no valid moves while not in check.
     *
     * @param teamColor which team to check for stalemate
     * @return True if the specified team is in stalemate, otherwise false
     */
    public boolean isInStalemate(TeamColor teamColor) {
        if(isInCheck(teamColor)){
            return false;
        }
        int rows = 1;
        int coll = 1;
        while(rows<9){
            while(coll<9){
                if(board.getPiece(new ChessPosition(rows, coll)) != null
                        &&board.getPiece(new ChessPosition(rows, coll)).getTeamColor()==teamColor){
                        if(validMoves( new ChessPosition(rows, coll)) !=null
                                && !validMoves(new ChessPosition(rows,coll)).isEmpty()){
                            return false;
                        }
                }
                coll++;
            }
            coll =1;
            rows++;
        }
        //do they not have any possible moves
        return true;
    }

    /**
     * Sets this game's chessboard with a given board
     *
     * @param board the new board to use
     */
    public void setBoard(ChessBoard board) {
        int row = 1;
        int col = 1;
        while(row<9){
            while (col<9){
                this.board.addPiece(new ChessPosition(row,col), board.getPiece(new ChessPosition(row,col)));
                col++;
            }
            col =1;
            row++;
        }
    }

    public ChessBoard copyBoard(ChessBoard source){
        ChessBoard copyboard = new ChessBoard();
        int row = 1;
        int col = 1;
        while(row<9){
            while (col<9){
               copyboard.addPiece(new ChessPosition(row,col), source.getPiece(new ChessPosition(row,col)));
                col++;
            }
            col =1;
            row++;
        }
        return copyboard;
    }

    /**
     * Gets the current chessboard
     *
     * @return the chessboard
     */
    public ChessBoard getBoard() {
        return board;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        ChessGame chessGame = (ChessGame) o;
        return teamTurn == chessGame.teamTurn && Objects.deepEquals(board,chessGame.board);
    }

    @Override
    public int hashCode() {
        return Objects.hash(teamTurn, board);
    }
}

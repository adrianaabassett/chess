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
    boolean isPassantNext = true;
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
        Collection<ChessMove> moves = new ArrayList<>();
        TeamColor ourColor = board.getPiece(startPosition).getTeamColor();
        //creating collection to return
        if(board.getPiece(startPosition) ==null){
           return null;
        }
        //checking that there is a piece there
        ChessBoard savingBoard = new ChessBoard();
        savingBoard = copyBoard(board);
        //creating a saved board to reference later
        //for each of the possible moves of that one piece
        //reset the board,
        //make the move
        for(ChessMove current:board.getPiece(startPosition).pieceMoves(board,startPosition)){
            board = copyBoard(savingBoard);
            //try {
                board.addPiece(current.getEndPosition(),board.getPiece(startPosition));
                board.addPiece(startPosition,null);
           // }
            //catch(InvalidMoveException wrongm){
                //you cant do this move
            //}
            //en passant possible next turn if row +-2
            //if its a pawn
            if( board.getPiece(startPosition) != null && board.getPiece(startPosition).getPieceType() == ChessPiece.PieceType.PAWN){
                //en passant possible next turn if row +-2
                if(startPosition.getRow() -2 == current.getEndPosition().getRow()||startPosition.getRow() +2 == current.getEndPosition().getRow() ){
                    isPassantNext = true;
                }
                else{isPassantNext = false;}
                //if its making a diagonal move then it must go left or right
                if(startPosition.getColumn() +1 == current.getEndPosition().getColumn() ||startPosition.getColumn() -1 == current.getEndPosition().getColumn()){
                    if(board.getPiece(current.getEndPosition())== null && isPassantNext){
                        moves.add(current);
                    }
                }
                //AND its going diagonally AND its an en passant move because its not landing on a piecee where end position is
                // AND its an en passant turn
            }
            else if(!isInCheck(ourColor)){
                //making sure it's not attempting a passant
                if(board.getPiece(startPosition) != null) {
                    if(board.getPiece(startPosition).getPieceType()!= ChessPiece.PieceType.PAWN || startPosition.getColumn() == current.getEndPosition().getColumn()) {
                        moves.add(current);
                    }
                }
                else{
                    moves.add(current);
                }
                isPassantNext = false;
            }
            else{
                isPassantNext = false;}
        }
//        board.getPiece(startPosition).pieceMoves(board,startPosition);
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
                    if(board.getPiece(move.getStartPosition()).getTeamColor() == TeamColor.WHITE ){
                        //checks it moves up positive
                        if(row == 5 && row +1 == erow){
                            //checks that it moves to the right, there is a piece next to it, and that the piece is on its own team
                            if(col+1 == ecol && board.getPiece(new ChessPosition(row,col+1))!=null && board.getPiece(new ChessPosition(row,col+1)).getTeamColor() == TeamColor.BLACK){
                                //removes the piece its passing
                                board.addPiece(new ChessPosition(row,col+1 ) ,null);
                            }
                            if(col-1 == ecol && board.getPiece(new ChessPosition(row,col-1))!=null && board.getPiece(new ChessPosition(row,col-1)).getTeamColor() == TeamColor.BLACK){
                                board.addPiece(new ChessPosition(row,col-1 ) ,null);
                            }
                        }

                    }
                    if(board.getPiece(move.getStartPosition()).getTeamColor() == TeamColor.BLACK ){
                        //checks it moves up positive
                        if(row == 4 && row -1 == erow){
                            //checks that it moves to the right, there is a piece next to it, and that the piece is on its own team
                            if(col+1 == ecol && board.getPiece(new ChessPosition(row,col+1))!=null && board.getPiece(new ChessPosition(row,col+1)).getTeamColor() == TeamColor.WHITE){
                                //removes the piece its passing
                                board.addPiece(new ChessPosition(row,col+1 ) ,null);
                            }
                            if(col-1 == ecol && board.getPiece(new ChessPosition(row,col-1))!=null && board.getPiece(new ChessPosition(row,col-1)).getTeamColor() == TeamColor.WHITE){
                                board.addPiece(new ChessPosition(row,col-1 ) ,null);
                            }
                        }

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
                if(checkPiece != null && checkPiece.getTeamColor()!=teamColor) {
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
                col++;
            }
            col =1;
            row++;
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
                if (thisKing!= null && thisKing.getPieceType() == ChessPiece.PieceType.KING && thisKing.getTeamColor() == teamColor) {
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
                    if(board.getPiece(new ChessPosition(row, col)).getTeamColor()==teamColor){
                        if(validMoves( new ChessPosition(row, col)) != null  && !validMoves(new ChessPosition(row,col)).isEmpty()){
                            return false;
                        }
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
        int row = 1;
        int col = 1;
        while(row<9){
            while(col<9){
                if(board.getPiece(new ChessPosition(row, col)) != null){
                    if(board.getPiece(new ChessPosition(row, col)).getTeamColor()==teamColor){
                        //
                        if(validMoves( new ChessPosition(row, col)) !=null && !validMoves(new ChessPosition(row,col)).isEmpty()){
                            return false;
                        }
                    }
                }
                col++;
            }
            col =1;
            row++;
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

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
            if(!isInCheck(ourColor)){
                moves.add(current);
            }
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
            if(inList && this.getTeamTurn() == board.getPiece(move.startPosition).getTeamColor()){
            //now we can move it
                if(move.getPromotionPiece() == null){
                    board.addPiece(move.getEndPosition(),board.getPiece(move.getStartPosition()));
                    board.addPiece(move.getStartPosition(),null);
                }
                else{
                 board.addPiece(move.getStartPosition(),
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


    //if something something else can capture offender
    //to block check
    //put it through valid moves.
    //create new board
    //in valid moves, call piecemoves, and then
    //as i iterate i reset the board to the actual board each time
    //this.name = knew String(copy.nametwo)
    //var copy = new DeepCopy(source);
    //make the move and revert the move
    //theres a copy objects under the class github
    // //create a new board where we move knight to all possible options and see
    //if its in check for that color repeatedly
    //return true or false.
    //thiss witll check for both blocking and attacking
    public boolean isInCheckmate(TeamColor teamColor) {
        if (!isInCheck(teamColor)){
            return false;
            //no valid moves and its not in check then its a stalemate
        }
        //check if it can move away
        ChessPosition kingPosition = whereKing(teamColor);
        Collection<ChessMove> possibleMoves= board.getPiece(kingPosition).pieceMoves(board,kingPosition);
        for(ChessMove current : possibleMoves){
            if(!checkIfAttackable(teamColor, current.getEndPosition())){
                return false;
            }
        }
        //ChessBoard tempBoard = new ChessBoard(copy.board);
        //maybe need a deep copy of this
        int row = 1;
        int col = 1;
        //for every piece on the board that is our color, and has any moves that do not lead to being in check
        while(row<9){
            while(col<9){
                ChessPosition pos = new ChessPosition(row, col);
                //checks theres something there, is the same teamcolor, that has valid moves that wont put it in check,
                if(board.getPiece(pos) != null && board.getPiece(pos).getTeamColor() == teamColor && validMoves(pos)!=null){
                    //then return false
                    return false;
                }
                col++;
            }
            col =1;
            row++;
        }

        return true;
        //one good excuse for mistakes is the navahoo tradition of leaving mistakes in everything
        //players like to move fast, feel smart, and have things feel fair
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
        //is it in check
        //check for any valid moves
        if(getTeamTurn() != teamColor){
            return false;
        }
        //is it their turn

        int row = 1;
        int col = 1;
        while(row<9){
            while(col<9){
                if(board.getPiece(new ChessPosition(row, col)) != null){
                    if(board.getPiece(new ChessPosition(row, col)).getTeamColor()==teamColor){
                        if(board.getPiece(new ChessPosition(row, col)).pieceMoves(board,new ChessPosition(row,col))!=null){
                            return false;
                        }
                    }
                }
                col++;
            }
            row++;
        }
        //do they have any possible moves
        return false;
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

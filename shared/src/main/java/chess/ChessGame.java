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
    public Collection<ChessMove> validMoves(ChessPosition startPosition) {
        Collection<ChessMove> moves = new ArrayList<>();
        if(board.getPiece(startPosition) !=null){
            return board.getPiece(startPosition).pieceMoves(board,startPosition);
        }
        return moves;
    }

    /**
     * Makes a move in a chess game
     *
     * @param move chess move to perform
     * @throws InvalidMoveException if move is invalid
     */
    public void makeMove(ChessMove move) throws InvalidMoveException {
        board.addPiece(move.getEndPosition(), board.getPiece(move.getStartPosition()));
        board.addPiece(move.getStartPosition(),null);
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
        boolean shouldreturnfalse = false;
        Collection<ChessMove> movesForFake;
        while(row<9){
            while(col<9){
                ChessPiece checkForMove = tempBoard.getPiece(new ChessPosition(row,col));
                if (checkForMove != null){
                    if (checkForMove.getTeamColor() == teamColor){
                        movesForFake = board.getPiece(new ChessPosition(row,col)).pieceMoves(board, new ChessPosition(row,col));
                        for(ChessMove currentFake : movesForFake){
                            makeMove(currentFake);
                            if(isInCheck(teamColor)){
                                shouldreturnfalse = true;
                            }
                            setBoard(tempBoard);
                        }
                        if(shouldreturnfalse){
                            return false;
                        }
                    }
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
        int ro =1;
        while(ro<9){

        }
        this.board = board;
    }

    public ChessBoard copyBoard(ChessBoard source, ChessBoard copyboard){
        int row = 1;
        int col = 1;
        while(row<9){
            while (col<9){
                
            }
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

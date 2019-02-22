package Board.Pieces;

import Board.Chessboard;
import Engine.Move;

public class Queen extends Piece{

    boolean isWhite;

    public Queen(boolean isWhite) {
        super(isWhite);
        super.setPieceType(Type.Q);
    }
    /*
    @Override
    public boolean validMove(Move move, Chessboard chessboard) {
        int rowDisp = move.getRowDisp(chessboard);
        int colDisp = move.getColDisp(chessboard);
        if(!move.isDiagonal(rowDisp, colDisp)) return false;
        else if (!move.isStraight(rowDisp, colDisp)) return false;
        else if(!basicValidMove(move, chessboard) || !basicValidInLineMove(move, chessboard)) return false;
        return true;
    }
    */


    @Override
    public boolean validMove(Move move, Chessboard chessboard, boolean checking) {
        int rowDisp = move.getRowDisp(chessboard);
        int colDisp = move.getColDisp(chessboard);
        if(!move.isStraight(rowDisp, colDisp) && !move.isDiagonal(rowDisp, colDisp)) return false;
        else if(!basicValidMove(move, chessboard) || !basicValidInLineMove(move, chessboard)) {
            return false;
        }
        return true;
    }

}

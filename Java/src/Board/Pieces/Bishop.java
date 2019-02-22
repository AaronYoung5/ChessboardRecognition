package Board.Pieces;

import Board.Chessboard;
import Engine.Move;

public class Bishop extends Piece{

    public Bishop(boolean isWhite) {
        super(isWhite);
        super.setPieceType(Type.B);
    }

    @Override
    public boolean validMove(Move move, Chessboard chessboard, boolean checking) {
        int rowDisp = move.getRowDisp(chessboard);
        int colDisp = move.getColDisp(chessboard);
        if(!move.isDiagonal(rowDisp, colDisp)) return false;
        if(!basicValidMove(move, chessboard) || !basicValidInLineMove(move, chessboard)) return false;
        return true;
    }
}

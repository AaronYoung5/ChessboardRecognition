package Board.Pieces;

import Board.Chessboard;
import Engine.Move;

public class Knight extends Piece{

    boolean isWhite;

    public Knight(boolean isWhite) {
        super(isWhite);
        super.setPieceType(Type.N);
    }

    @Override
    public boolean validMove(Move move, Chessboard chessboard, boolean checking) {
        int absRowDisp = Math.abs(move.getRowDisp(chessboard));
        int absColDisp = Math.abs(move.getColDisp(chessboard));
        if(!basicValidMove(move, chessboard)) return false;
        else if(absColDisp == 2 && absRowDisp == 1) return true;
        else if(absColDisp == 1 && absRowDisp == 2) return true;
        else return false;
    }
}

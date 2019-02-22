package Board.Pieces;

import Board.Chessboard;
import Engine.Move;

public class Empty extends Piece{

    boolean isWhite;

    public Empty(boolean isWhite) {
        super(isWhite);
        super.setPieceType(Type.E);
    }

    @Override
    public boolean validMove(Move move, Chessboard chessboard, boolean checking) {
        return false;
    }
}

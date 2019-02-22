package Board.Pieces;

import Board.Chessboard;
import Board.Tile;
import Engine.Move;

public class King extends Piece {

    boolean isWhite;

    public King(boolean isWhite) {
        super(isWhite);
        super.setPieceType(Type.K);
    }

    @Override
    public boolean validMove(Move move, Chessboard chessboard, boolean checking) {
        int rowDisp = move.getRowDisp(chessboard);
        int colDisp = move.getColDisp(chessboard);
        if(!checking && validCastleMove(move, chessboard)) chessboard.setCastling(true);
        else if(Math.abs(rowDisp) > 1 || Math.abs(colDisp) > 1) return false;
        else if(!move.isDiagonal(rowDisp, colDisp) && !move.isStraight(rowDisp, colDisp)) return false;
        else if(!basicValidMove(move, chessboard) || !basicValidInLineMove(move, chessboard)) return false;
        if(!checking) hasMoved = true;
        return true;
    }

    public boolean validCastleMove(Move move, Chessboard chessboard) {
        Tile toTile = move.getToTile();
        Rook rook;
        if(move.getToPiece().isWhite() == move.getFromPiece().isWhite() && toTile.getPiece().isTypeRook()) rook = (Rook)toTile.getPiece();
        else return false;
        if(this.hasMoved || rook.hasMoved) return false;
        if(!validCastleInLineMove(move, chessboard)) return false;
        return true;
    }
}

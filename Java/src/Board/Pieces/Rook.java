package Board.Pieces;

import Board.Chessboard;
import Board.Tile;
import Engine.Move;

public class Rook extends Piece {

    boolean isWhite;

    public Rook(boolean isWhite) {
        super(isWhite);
        super.setPieceType(Type.R);
    }

    @Override
    public boolean validMove(Move move, Chessboard chessboard, boolean checking) {
        int rowDisp = move.getRowDisp(chessboard);
        int colDisp = move.getColDisp(chessboard);
        if(!checking && validCastleMove(move, chessboard)) chessboard.setCastling(true);
        else if (!move.isStraight(rowDisp, colDisp)) return false;
        else if (!basicValidMove(move, chessboard) || !basicValidInLineMove(move, chessboard)) return false;
        if(!checking) hasMoved = true;
        return true;
    }

    private boolean validCastleMove(Move move, Chessboard chessboard) {
        Tile toTile = move.getToTile();
        King king;
        if (move.getToPiece().isWhite() == move.getFromPiece().isWhite() && toTile.getPiece().isTypeKing()) king = (King) toTile.getPiece();
        else return false;
        if (this.hasMoved || king.hasMoved) return false;
        if (!validCastleInLineMove(move, chessboard)) return false;
        return true;
    }
}

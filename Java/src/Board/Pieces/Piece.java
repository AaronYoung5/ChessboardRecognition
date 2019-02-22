package Board.Pieces;

import Board.Chessboard;
import Board.Tile;
import Engine.Move;

import java.util.ArrayList;
import java.util.List;

public abstract class Piece {

    private boolean isWhite;

    public boolean hasMoved;

    private Type pieceType;

    public Piece(boolean isWhite) {
        this.isWhite = isWhite;
    }

    public Type getType() {
        return pieceType;
    }

    public void setPieceType(Type pieceType) {
        this.pieceType = pieceType;
    }

    public boolean isWhite() {
        return isWhite;
    }

    public boolean basicValidMove(Move move, Chessboard chessboard) {
        return !move.isSameColor() && !move.isSamePiece();
    }

    public boolean validCastleInLineMove(Move move, Chessboard chessboard) {
        int rowDisp = move.getRowDisp(chessboard);
        int colDisp = move.getColDisp(chessboard);
        int steps = Math.max(Math.abs(rowDisp), Math.abs(colDisp));
        int row = move.getFromRow();
        int col = move.getFromCol();
        for(int i = 0; i < steps; i++) {
            row += Math.signum(rowDisp);
            col += Math.signum(colDisp);
            Tile tempTile = chessboard.getFromIndex(row, col);
            if(i + 1 == steps && tempTile.isEmpty()) return true;
            else if(i + 1 == steps && !tempTile.isEmpty() && !move.isSameColor()) return false;
            else if(i + 1 != steps && !tempTile.isEmpty()) return false;
        }
        return true;
    }

    public boolean basicValidInLineMove(Move move, Chessboard chessboard) {
        int rowDisp = move.getRowDisp(chessboard);
        int colDisp = move.getColDisp(chessboard);
        int steps = Math.max(Math.abs(rowDisp), Math.abs(colDisp));
        int row = move.getFromRow();
        int col = move.getFromCol();
        Tile toTile = move.getToTile();
        for(int i = 0; i < steps; i++) {
            row += Math.signum(rowDisp);
            col += Math.signum(colDisp);
            Tile tempTile = chessboard.getFromIndex(row, col);
            if(tempTile == toTile && tempTile.isEmpty()) return true;
            else if(tempTile != toTile && !tempTile.isEmpty()) return false;
            else if(tempTile == toTile && !tempTile.isEmpty() && !canCapture(move)) return false;
        }
        return true;
    }

    public boolean canCapture(Move move) {
        Piece toPiece = move.getToPiece();
        return toPiece.isWhite() != this.isWhite;
    }

    public boolean isTypeEmpty() {
        return this.pieceType == Type.E;
    }

    public boolean isTypePawn() { return this.pieceType == Type.P; }

    public boolean isTypeRook() { return this.pieceType == Type.R; }

    public boolean isTypeKing() { return this.pieceType == Type.K; }

    public boolean isTypeKnight() {
        return this.pieceType == Type.N;
    }

    public List<Move> possibleMoves(Chessboard chessboard, Tile pieceTile) {
        List<Move> moveList = new ArrayList<>();
        Tile[][] boardArray = chessboard.getBoardArray();
        for(Tile[] row: boardArray) {
            for(Tile tempTile: row) {
                Move tempMove = new Move(pieceTile, tempTile);
                if(this.validMove(tempMove, chessboard, true)) {
                    moveList.add(tempMove);
                }
            }
        }
        return moveList;
    }

    @Override
    public String toString() {
        return getType().toString();
    }

    public abstract boolean validMove(Move move, Chessboard chessboard, boolean checking);
}

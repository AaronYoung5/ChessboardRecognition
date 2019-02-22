package Engine;

import Board.Chessboard;
import Board.Coordinate;
import Board.Pieces.Piece;
import Board.Tile;

public class Move {

    private Tile fromTile, toTile;

    private Piece fromPiece, toPiece;

    private String moveAsString;

    public Move(Tile fromTile, Tile toTile) {
        this.fromTile = fromTile;
        this.toTile = toTile;
        this.fromPiece = fromTile.getPiece();
        this.toPiece = toTile.getPiece();
        this.moveAsString = this.toString();
    }

    public boolean isWhiteMove() {
        return fromTile.getPiece().isWhite();
    }

    public String getMoveAsString() {
        return moveAsString;
    }

    public Tile getFromTile() {
        return fromTile;
    }

    public Tile getToTile() {
        return toTile;
    }

    public Piece getFromPiece() {
        return fromPiece;
    }

    public Piece getToPiece() {
        return toPiece;
    }

    public boolean isDiagonal(int rowDisp, int colDisp) {
        return Math.abs(rowDisp) == Math.abs(colDisp);
    }

    public boolean isStraight(int rowDisp, int colDisp) {
        return !(rowDisp != 0 && colDisp != 0);
    }

    public boolean isSameColor() {
        if(toPiece.isTypeEmpty()) return false;
        return fromPiece.isWhite() == toPiece.isWhite();
    }

    public boolean isSamePiece() {
        return fromPiece == toPiece;
    }

    public int getRowDisp(Chessboard chessboard) {
        int fromRow = fromTile.getCoordinate().getRowIndex();
        int toRow = toTile.getCoordinate().getRowIndex();
        return toRow - fromRow;
    }

    public int getColDisp(Chessboard chessboard) {
        int fromCol = fromTile.getCoordinate().getColIndex();
        int toCol = toTile.getCoordinate().getColIndex();
        return toCol - fromCol;
    }

    public int getFromRow() {
        return fromTile.getCoordinate().getRowIndex();
    }

    public int getFromCol() {
        return fromTile.getCoordinate().getColIndex();
    }

    public int getToRow() {
        return toTile.getCoordinate().getRowIndex();
    }

    public int getToCol() {
        return toTile.getCoordinate().getColIndex();
    }

    public Move getInverseMove() {
        return new Move(toTile, fromTile);
    }

    private String pieceToAlgebraicNotation(Tile tile) {
        String location = tile.getCoordinate().toString();
        Piece piece = tile.getPiece();
        if(piece.isTypePawn()) return location;
        else if(piece.isTypeEmpty()) return location;
        return piece.getType().toString() + location;
    }

    @Override
    public String toString() {
        String fromNotation = pieceToAlgebraicNotation(fromTile);
        String toNotation = pieceToAlgebraicNotation(toTile);
        return fromNotation + " " + toNotation;
    }

}

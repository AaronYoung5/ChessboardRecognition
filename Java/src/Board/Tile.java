package Board;

import Board.Pieces.*;

public class Tile {
    private Piece piece;

    private Coordinate coordinate;

    public Tile(Piece piece, int rowIndex, int colIndex) {
        this.piece = piece;
        coordinate = new Coordinate(rowIndex, colIndex);
    }

    public boolean isEmpty() {
        return piece.getType() == Type.E;
    }

    public Piece getPiece() {
        return piece;
    }

    public void setPiece(Piece piece) {
        this.piece = piece;
    }

    public void setAsEmpty() {
        this.piece = new Empty(false);
    }

    public Coordinate getCoordinate() {
        return coordinate;
    }

    @Override
    public String toString() {
        return piece + " @ " + coordinate;
    }
}

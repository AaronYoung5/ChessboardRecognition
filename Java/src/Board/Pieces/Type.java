package Board.Pieces;

public enum Type {
    P, R, B, N, Q, K, E;

    public Piece createPiece(boolean isWhite) {
        switch(this) {
            case K:
                return new King(isWhite);
            case Q:
                return new Queen(isWhite);
            case R:
                return new Rook(isWhite);
            case B:
                return new Bishop(isWhite);
            case N:
                return new Knight(isWhite);
            case P:
                return new Pawn(isWhite);
            case E:
                return new Empty(isWhite);
        }
        return null;
    }

    public Piece duplicatePiece(Piece oldPiece) {
        boolean isWhite = oldPiece.isWhite();
        switch(this) {
            case K:
                King king = new King(isWhite);
                King oldKing = (King)oldPiece;
                king.hasMoved = oldKing.hasMoved;
                return king;
            case Q:
                return new Queen(isWhite);
            case R:
                Rook rook = new Rook(isWhite);
                Rook oldRook = (Rook)oldPiece;
                rook.hasMoved = oldRook.hasMoved;
                return rook;
            case B:
                return new Bishop(isWhite);
            case N:
                return new Knight(isWhite);
            case P:
                Pawn pawn = new Pawn(isWhite);
                Pawn oldPawn = (Pawn)oldPiece;
                pawn.hasMoved = oldPawn.hasMoved;
                return pawn;
            case E:
                return new Empty(isWhite);
        }
        return null;
    }
}

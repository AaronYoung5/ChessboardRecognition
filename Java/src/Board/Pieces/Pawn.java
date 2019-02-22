package Board.Pieces;

import Board.Chessboard;
import Engine.Move;

public class Pawn extends Piece{

    public boolean justDoubleMoved;

    public Pawn(boolean isWhite) {
        super(isWhite);
        super.setPieceType(Type.P);
        hasMoved = false;
    }

    @Override
    public boolean validMove(Move move, Chessboard chessboard, boolean checking) {
        int absRowDisp = Math.abs(move.getRowDisp(chessboard));
        int absColDisp = Math.abs(move.getColDisp(chessboard));
        if(!correctDirection(move.getRowDisp(chessboard))) return false;
        if(!basicValidMove(move, chessboard)) return false;
        if(absRowDisp > 2 || absColDisp > 1) return false;
        if(move.getToPiece().isTypeEmpty()) {
            if(absColDisp != 0 && !attemptingEnPassant(move, chessboard)) return false;
            if(absRowDisp == 2 && hasMoved || !basicValidInLineMove(move, chessboard)) return false;
            if(absRowDisp == 2 && !checking) justDoubleMoved = true;
        }
        else {
            if(absColDisp != 1 || absRowDisp != absColDisp) return false;
            else if(!canCapture(move)) return false;
        }
        if(!checking) hasMoved = true;
        return true;
    }

    private boolean attemptingEnPassant(Move move, Chessboard chessboard) {
        int absRowDisp = Math.abs(move.getRowDisp(chessboard));
        int absColDisp = Math.abs(move.getColDisp(chessboard));
        int fromRow = move.getFromRow();
        int toCol = move.getToCol();
        Piece capturedPiece = chessboard.get(fromRow, toCol).getPiece();
        if(absRowDisp != 1 || absColDisp != 1) return false; //must be diagonal
        else if((isWhite()) ? fromRow != 4 : fromRow != 3) return false; //must be from correct row every time
        else if(!capturedPiece.isTypePawn()) return false;
        Pawn capturedPawn = (Pawn)capturedPiece;
        if(!capturedPawn.justDoubleMoved) return false;
        chessboard.enPassant = true;
        return true;
    }

    private boolean correctDirection(int rowDisp) {
        return (isWhite()) ? rowDisp > 0: rowDisp < 0;
    }
}

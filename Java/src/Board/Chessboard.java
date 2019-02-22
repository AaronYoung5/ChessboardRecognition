package Board;

import Board.Pieces.*;
import Engine.Move;

import java.util.ArrayList;
import java.util.List;

public class Chessboard {
    private static boolean playerIsWhite;

    public static int boardSize = 8;

    public boolean castling, enPassant;

    public boolean boardInCheck, whiteKingInCheck, blackKingInCheck;

    private static Tile[][] boardArray;

    private static Type[][] initTypeBoardArray = {
            {Type.R, Type.N, Type.B, Type.Q, Type.K, Type.B, Type.N, Type.R},
            {Type.P, Type.P, Type.P, Type.P, Type.P, Type.P, Type.P, Type.P},
            {Type.E, Type.E, Type.E, Type.E, Type.E, Type.E, Type.E, Type.E},
            {Type.E, Type.E, Type.E, Type.E, Type.E, Type.E, Type.E, Type.E},
            {Type.E, Type.E, Type.E, Type.E, Type.E, Type.E, Type.E, Type.E},
            {Type.E, Type.E, Type.E, Type.E, Type.E, Type.E, Type.E, Type.E},
            {Type.P, Type.P, Type.P, Type.P, Type.P, Type.P, Type.P, Type.P},
            {Type.R, Type.N, Type.B, Type.Q, Type.K, Type.B, Type.N, Type.R}};

    public Chessboard(boolean playerIsWhite) {
        Chessboard.playerIsWhite = playerIsWhite;
        boolean pieceIsWhite = !playerIsWhite;
        boardArray = new Tile[boardSize][boardSize];
        for(int row = 0; row < boardSize; row++) {
            if(row == boardSize/2) pieceIsWhite = !pieceIsWhite;
            for(int col = 0; col < boardSize; col++) {
                boardArray[row][col] = new Tile(initTypeBoardArray[row][col].createPiece(pieceIsWhite), row, col);
            }
        }
    }

    public Chessboard(Chessboard chessboard) {
        //this.playerIsWhite = chessboard.playerIsWhite;
        this.castling = chessboard.castling;
        this.enPassant = chessboard.enPassant;
        this.boardArray = copyArray(Chessboard.getBoardArray());
        this.blackKingInCheck = chessboard.blackKingInCheck;
        this.whiteKingInCheck = chessboard.whiteKingInCheck;
        this.boardInCheck = chessboard.boardInCheck;
    }

    public Chessboard(Tile[][] boardArray) {
        Chessboard.boardArray = boardArray;
    }

    private Tile[][] copyArray(Tile[][] oldArray) {
        int boardSize = oldArray.length;
        Tile[][] newBoard = new Tile[boardSize][boardSize];
        for(int row = 0; row < boardSize; row++) {
            for(int col = 0; col < boardSize; col++) {
                newBoard[row][col] = copyTile(oldArray[row][col]);
            }
        }
        return newBoard;
    }

    private Tile copyTile(Tile tile) {
        return new Tile(copyPiece(tile.getPiece()), tile.getCoordinate().getRowIndex(), tile.getCoordinate().getColIndex());
    }

    private Piece copyPiece(Piece oldPiece) {
        return oldPiece.getType().duplicatePiece(oldPiece);
    }

    public void executeMove(Move move) {
        if(castling) {
            executeCastleMove(move);
            castling = false;
            resetJustMoved(null);
        }
        else if(enPassant) {
            executeEnPassant(move);
            enPassant = false;
            resetJustMoved(null);
        }
        else {
            Piece fromPiece = move.getFromPiece();
            if(!fromPiece.isTypePawn()) resetJustMoved(null);
            else resetJustMoved((Pawn)fromPiece);
            //Piece toPiece = move.getToPiece();
            Tile fromTile = move.getFromTile();
            Tile toTile = move.getToTile();
            fromTile.setAsEmpty();
            toTile.setPiece(fromPiece);
        }
        //System.out.println(move);
    }

    private void executeEnPassant(Move move) {
        int fromRow = move.getFromRow();
        int toCol = move.getToCol();
        //Pawn capturedPawn = (Pawn)boardArray[fromRow][toCol].getPiece();
        Tile toTile = move.getToTile();
        Tile fromTile = move.getFromTile();
        Tile capturedTile = boardArray[fromRow][toCol];
        toTile.setPiece(fromTile.getPiece());
        fromTile.setAsEmpty();
        capturedTile.setAsEmpty();
    }

    private void executeCastleMove(Move move) {
        Tile kingTile, rookTile;
        Piece rook, king;
        if(move.getFromPiece().isTypeKing()) {
            kingTile = move.getFromTile();
            rookTile = move.getToTile();
            king = move.getFromPiece();
            rook = move.getToPiece();
        }
        else {
            kingTile = move.getToTile();
            rookTile = move.getFromTile();
            king = move.getToPiece();
            rook = move.getFromPiece();
        }
        int rookCol = rookTile.getCoordinate().getColIndex();
        int kingCol = kingTile.getCoordinate().getColIndex();
        int row = rookTile.getCoordinate().getRowIndex();
        if(rookCol == 0) {
            boardArray[row][rookCol + 3].setPiece(rook);
            boardArray[row][kingCol - 2].setPiece(king);
            kingTile.setAsEmpty();
            rookTile.setAsEmpty();
        }
        else {
            boardArray[row][rookCol - 2].setPiece(rook);
            boardArray[row][kingCol + 2].setPiece(king);
            kingTile.setAsEmpty();
            rookTile.setAsEmpty();
        }
    }

    public static Tile[][] getBoardArray() {
        return boardArray;
    }

    public void setCastling(boolean castling) {
        this.castling = castling;
    }

    public void resetJustMoved(Pawn pawn) {
        for(Tile[] row: boardArray) {
            for(Tile tile: row) {
                Piece tempPiece = tile.getPiece();
                if(tempPiece.isTypePawn()) {
                    Pawn tempPawn = (Pawn)tempPiece;
                    if(tempPawn != pawn) tempPawn.justDoubleMoved = false;
                }
            }
        }
    }

    private static int getRow(int row) {
        return (playerIsWhite) ? (boardSize - 1) - row: row;
    }

    private static int getCol(int col) {
        return (!playerIsWhite) ? (boardSize - 1) - col: col;
    }

    private static Tile get(Coordinate coordinate) {
        return boardArray[getRow(coordinate.getRowIndex())][getCol(coordinate.getColIndex())];
    }

    public static Tile get(int row, int col) {
        return boardArray[getRow(row)][getCol(col)];
    }

    public Tile getFromIndex(int row, int col) {
        return boardArray[row][col];
    }

    private static Tile get(Tile tile) {
        return get(tile.getCoordinate().getRowIndex(), tile.getCoordinate().getColIndex());
    }

    public static Tile get(String stringTile) {
        String colString = stringTile.substring(0,1);
        int row = Integer.parseInt(stringTile.substring(1,2));
        Coordinate coordinate = new Coordinate(row, colString);
        return get(coordinate);
    }

    public List<Move> getPossibleMoves(boolean playerIsWhite) {
        List<Move> moveList = new ArrayList<>();
        for(Tile[] row: boardArray) {
            for (Tile tile : row) {
                Piece piece = tile.getPiece();
                if(playerIsWhite != piece.isWhite()) continue;
                moveList.addAll(piece.possibleMoves(this, tile));
            }
        }
        return moveList;
    }

    public List<Move> getAllPossibleMoves() {
        List<Move> moveList = new ArrayList<>();
        for(Tile[] row: boardArray) {
            for (Tile tile : row) {
                Piece piece = tile.getPiece();
                moveList.addAll(piece.possibleMoves(this, tile));
            }
        }
        return moveList;
    }

    public Move getRealMove(Move move) {
        Tile fromTile = move.getFromTile();
        Tile toTile = move.getToTile();
        return new Move(get(fromTile), get(toTile));
    }

    public void undo(Move move) {
        executeMove(move.getInverseMove());
    }

    @Override
    public String toString() {
        StringBuilder str = new StringBuilder();
        for(Tile[] row: boardArray) {
            for(Tile tile: row) {
                //System.out.println(tile);
                Piece piece = tile.getPiece();
                str.append(piece);
            }
            str.append("\n");
        }
        return str.toString();
    }
}

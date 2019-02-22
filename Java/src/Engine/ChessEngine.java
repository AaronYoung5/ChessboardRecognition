package Engine;

import Board.BoardSaver.BoardSaver;
import Board.ChessGame.ChessGame;
import Board.Chessboard;
import Board.Pieces.Piece;
import Board.Players.Player;
import Board.Players.StockFish;
import Board.Tile;

import java.util.*;

public class ChessEngine {

    private boolean playerIsWhite, whiteTurn;

    private Chessboard chessboard;

    private List<Tile> selections;

    private BoardSaver boardSaver;

    private StockFish stockFish;

    private boolean gameOver;

    private List<Move> whiteMoves, blackMoves;

    public ChessEngine(boolean playerIsWhite) {
        this.playerIsWhite = playerIsWhite;
        chessboard = new Chessboard(playerIsWhite);
        selections = new ArrayList<>();
        boardSaver = new BoardSaver(chessboard);
        stockFish = new StockFish(!playerIsWhite);
        whiteTurn = true;
        gameOver = false;
        whiteMoves = new ArrayList<>();
        blackMoves = new ArrayList<>();
        //toBoardArray();
    }

    /*public void nextMove() {
        whiteMoves = Player.findMoves(new Chessboard(chessboard), true);
        blackMoves = Player.findMoves(new Chessboard(chessboard), false);
        if(ChessGame.useStockFish) {
            if (playerIsWhite == whiteTurn) return;
            List<Move> stockFishMoves = (playerIsWhite) ? blackMoves: whiteMoves;
            Move stockfishMove = stockFish.getRandomMove(stockFishMoves);
            if (stockfishMove == null) gameOver = true;

            tryMove(chessboard.getRealMove(stockfishMove));
        }
    }*/

    public void nextMove() {
        whiteMoves = findMoves(true);
        blackMoves = findMoves(false);
        if(ChessGame.useStockFish) {
            if (playerIsWhite == whiteTurn) return;
            List<Move> stockFishMoves = Player.findMoves(chessboard, !playerIsWhite);
            Move stockfishMove = stockFish.getRandomMove(stockFishMoves);
            if (stockfishMove == null) gameOver = true;

            tryMove(chessboard.getRealMove(stockfishMove));
        }
    }

    private List<Move> findMoves(boolean playerIsWhite) {
        List<Move> moveList = chessboard.getPossibleMoves(playerIsWhite);
        List<Move> toRemoveList = new ArrayList<>();
        for (Move move : moveList) {
            if(move.isWhiteMove() != playerIsWhite) {
                toRemoveList.add(move);
                continue;
            }
            Tile toTile = move.getToTile();
            if (toTile.getPiece().isTypeKing()) {
                chessboard.boardInCheck = true;
                if (toTile.getPiece().isWhite()) chessboard.whiteKingInCheck = true;
                else chessboard.blackKingInCheck = true;
                continue;
            }
            Chessboard tempChessboard = new Chessboard(chessboard);
            tempChessboard.executeMove(move);
            if (ChessEngine.boardInCheck(playerIsWhite, tempChessboard))
                toRemoveList.add(move);
        }
        moveList.removeAll(toRemoveList);
        return moveList;
    }

    private void inCheck () {
        Chessboard tempChessboard = new Chessboard(chessboard);
        List<Move> boardMoves = tempChessboard.getAllPossibleMoves();
        boolean whiteInCheck = false, blackInCheck = false;
        for(Move move: boardMoves) {
            Piece toPiece = move.getToPiece();
            if(toPiece.isWhite() && toPiece.isTypeKing()) whiteInCheck = true;
            else if(!toPiece.isWhite() && toPiece.isTypeKing()) blackInCheck = true;
        }
        chessboard.boardInCheck = whiteInCheck || blackInCheck;
        chessboard.whiteKingInCheck = whiteInCheck;
        chessboard.blackKingInCheck = blackInCheck;
    }

    public static boolean boardInCheck(boolean playerIsWhite, Chessboard chessboard) {
        for(Move move: chessboard.getAllPossibleMoves()) {
            Piece toPiece = move.getToPiece();
            if(toPiece.isWhite() == playerIsWhite && toPiece.isTypeKing()) return true;
        }
        return false;
    }

    public Chessboard getChessboard() {
        return chessboard;
    }

    public void setSelection(int row, int col) {
        Tile tile = chessboard.get(row, col);
        selections.add(tile);
        if(selections.size() == 2){
            Move move = new Move(selections.get(0), selections.get(1));
            tryMove(move);
            selections.clear();
        }
        nextMove();
    }

    public int getSelectionSize() {
        return selections.size();
    }

    public boolean tileInCheck (int row, int col) {
        if(!chessboard.boardInCheck) return false;
        else if(row > 7 || col > 7) return false;
        Piece piece = Chessboard.get(row, col).getPiece();
        if(!piece.isTypeKing()) return false;
        else if(piece.isWhite() && chessboard.whiteKingInCheck) return true;
        else if(!piece.isWhite() && chessboard.blackKingInCheck) return true;
        else return false;
    }

    private void tryMove(Move move) {
        Piece fromPiece = move.getFromPiece();
        //System.out.println(move);
        if(fromPiece.validMove(move, chessboard,false)) {
            if(fromPiece.isWhite() != whiteTurn) return;
            chessboard.executeMove(move);
            boardSaver.add(chessboard, move);
            inCheck();
            if(whiteTurn && chessboard.whiteKingInCheck) simpleUndo();
            else if(!whiteTurn && chessboard.blackKingInCheck) simpleUndo();
            else whiteTurn = !whiteTurn;
            inCheck();
            //System.out.println(toAlgebraicNotationString());
        }
        nextMove();
    }

    public void tryMoveFromList(List<Tile> tileList) {
        Move move;
        if(tileList.get(0).isEmpty())
            move = new Move(tileList.get(1), tileList.get(0));
        else
            move = new Move(tileList.get(0), tileList.get(1));
        tryMove(move);

    }

    private void simpleUndo() {
        boardSaver.undo();
        updateChessboard();
    }

    public void undo() {
        if(playerIsWhite == whiteTurn) {
            boardSaver.undo();
            boardSaver.undo();
        }
        else {
            boardSaver.undo();
            whiteTurn = !whiteTurn;
        }
        updateChessboard();
    }

    public void redo() {
        if(playerIsWhite == whiteTurn) {
            boardSaver.redo();
            boardSaver.redo();
        }
        else {
            boardSaver.redo();
            whiteTurn = !whiteTurn;
        }
        updateChessboard();
    }

    private void updateChessboard() {
        chessboard = boardSaver.get();
    }

    public boolean isGameOver() {
        return gameOver;
    }

    private String toAlgebraicNotationString() {
        StringBuilder str = new StringBuilder();
        int counter = 1;
        for(String algebraicNotation: boardSaver.getAlgebraicNotationList()) {
            str.append(counter++ + ". ");
            str.append(algebraicNotation + " ");
        }
        return str.toString();
    }
}

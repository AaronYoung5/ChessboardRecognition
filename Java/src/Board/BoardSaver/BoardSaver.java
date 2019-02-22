package Board.BoardSaver;

import Board.Chessboard;
import Board.Tile;
import Engine.Move;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class BoardSaver {

    private List<Chessboard> activeBoardList;

    private int activeBoard;

    private List<String> boardListAlgebraicNotation;

    private List<Move> moveList;

    public BoardSaver(Chessboard chessboard) {
        activeBoardList = new ArrayList<>();
        activeBoardList.add(copyBoard(chessboard));
        activeBoard = 0;
        boardListAlgebraicNotation = new ArrayList<>();
        moveList = new ArrayList<>();
    }

    public void add(Chessboard chessboard, Move move) {
        resetBoardAtIndex();
        activeBoardList.add(copyBoard(chessboard));
        moveList.add(move);
        boardListAlgebraicNotation.add(move.getMoveAsString());
        activeBoard++;
    }

    public Chessboard get() {
        return copyBoard(activeBoardList.get(activeBoard));
    }

    public List<String> getAlgebraicNotationList() {
        return boardListAlgebraicNotation;
    }

    public boolean undo() {

        System.out.println("Undo");
        if(activeBoard <= 0) return false;
        activeBoard--;
        return true;
    }

    public boolean redo() {
        if(activeBoard == activeBoardList.size() - 1) return false;
        activeBoard++;
        return true;
    }

    private void resetBoardAtIndex() {
        activeBoardList.subList(activeBoard + 1, activeBoardList.size()).clear();
        //if(moveList.size() > 0) moveList.subList(activeBoard + 1, moveList.size()).clear();
        //if(boardListAlgebraicNotation.size() > 0) boardListAlgebraicNotation.subList(activeBoard + 1, boardListAlgebraicNotation.size()).clear();
    }

    private Chessboard copyBoard(Chessboard originalChessboard) {
        return new Chessboard(originalChessboard);
    }

    public Move getLastMove() {
        if(activeBoard == 0 || moveList.size() == 0)
            return null;
        return moveList.get(activeBoard - 1);
    }
}

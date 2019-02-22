package PythonCommunicator;

import Board.Chessboard;
import Board.Tile;
import Engine.Move;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;

public class BoardInterpreter {

    private File file;

    private LinkedHashMap<Tile, Boolean> hashBoard;

    private Tile[][] boardArray;

    public BoardInterpreter(){
        this.file = BoardReader.file;
        hashBoard = BoardReader.readBoard();
    }

    public void update() {
        hashBoard = BoardReader.readBoard();
    }

    public boolean hasChanged() {
        return !hashBoard.equals(BoardReader.readBoard());
    }

    public List<Tile> findChange() {
        LinkedHashMap<Tile, Boolean> tempBoard = BoardReader.readBoard();
        List<Tile> diffList = new ArrayList<>();
        for(Tile tileKey: hashBoard.keySet()) {
            Boolean tempBoardOccupied = tempBoard.get(tileKey);
            Boolean hashBoardOccupied = hashBoard.get(tileKey);
            if(tempBoardOccupied != hashBoardOccupied)
                diffList.add(tileKey);
        }
        //System.out.println(diffList);
        return diffList;
    }

    public boolean realBoardLive() {
        LinkedHashMap<Tile, Boolean> tempVirtualBoard = boardArrayToLinkedHashMap(Chessboard.getBoardArray());
        LinkedHashMap<Tile, Boolean> tempRealBoard = BoardReader.readBoard();
        for(Tile key: tempRealBoard.keySet()) {
            if(tempVirtualBoard.get(key) != tempRealBoard.get(key)) {
                System.out.println(key);
                return false;
            }
        }
        return true;
    }

    private LinkedHashMap<Tile, Boolean> boardArrayToLinkedHashMap(Tile[][] boardArray) {
        LinkedHashMap<Tile, Boolean> tempBoard = new LinkedHashMap<>();
        for(Tile[] row: boardArray) {
            for(Tile tile: row) {
                tempBoard.put(tile, !tile.isEmpty());
            }
        }
        return tempBoard;
    }

    public LinkedHashMap<Tile, Boolean> getHashBoard() {
        return hashBoard;
    }
}

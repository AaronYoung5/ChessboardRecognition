package Board;

import Board.ChessGame.ChessGame;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Objects;

public class Coordinate {

    private int rowIndex, colIndex;

    private String[] columnStringLocation = {"a", "b", "c", "d", "e", "f", "g", "h"};

    public Coordinate(int rowIndex, int colIndex) {
        this.rowIndex = rowIndex;
        this.colIndex = colIndex;
    }

    public Coordinate(int row, String colString) {
        this.rowIndex = row - 1;
        if(!ChessGame.playerIsWhite) Collections.reverse(Arrays.asList(columnStringLocation));
        this.colIndex = Arrays.asList(columnStringLocation).indexOf(colString);
    }

    public int getRowIndex() {
        return rowIndex;
    }

    public int getColIndex() {
        return colIndex;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Coordinate)) return false;
        Coordinate that = (Coordinate) o;
        return rowIndex == that.rowIndex && colIndex == that.colIndex;
    }

    @Override
    public String toString() {
        String location = columnStringLocation[colIndex] + (rowIndex + 1);
        return location;
    }
}

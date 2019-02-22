package PythonCommunicator;

import Board.Chessboard;
import Board.Tile;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.LinkedHashMap;

public class BoardReader {

    public static File file = new File("/Users/aaronyoung/Coding/Chess6/src/PythonCommunicator/board.txt");

    public static LinkedHashMap<Tile, Boolean> readBoard() {
        BufferedReader reader;
        LinkedHashMap<Tile, Boolean> tempBoard = new LinkedHashMap<>();
        try {
            reader = new BufferedReader(new FileReader(file));
            String str;
            while((str = reader.readLine()) != null) {
                Tile location = splitKey(str);
                Boolean occupied = splitValue(str);
                tempBoard.put(location, occupied);
            }
            return tempBoard;
        }
        catch (IOException e) {
            System.out.println("Read Board Error Board Interpreter");
            return tempBoard;
        }
    }

    private static boolean splitValue(String str) {
        return str.substring(3,4).equals("T");
    }

    private static Tile splitKey(String str) {
        return Chessboard.get(str.substring(0, 2));
    }
}

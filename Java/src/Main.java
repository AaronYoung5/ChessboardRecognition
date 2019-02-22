import Board.ChessGame.ChessGame;
import GUI.ChessGUI;
import PythonCommunicator.BoardReader;

import java.io.*;
import java.util.concurrent.TimeUnit;

public class Main {
    public static void main(String[] args) throws FileNotFoundException, IOException, InterruptedException{
        ChessGame chessGame = new ChessGame(true);
        //chessGame.runGame();
    }
}

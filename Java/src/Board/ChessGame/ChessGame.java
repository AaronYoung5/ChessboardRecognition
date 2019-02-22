package Board.ChessGame;

import Engine.ChessEngine;
import GUI.ChessGUI;
import PythonCommunicator.BoardInterpreter;
import PythonCommunicator.BoardReader;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

public class ChessGame {

    private ChessGUI chessGUI;

    private BoardInterpreter interpreter;

    public static boolean playerIsWhite = false;
    public static boolean useStockFish = true;

    private ChessEngine chessEngine;

    public ChessGame() {
        try {
            Runtime.getRuntime().exec("python /Users/aaronyoung/Coding/Python/OpenCVChessRecognition/main.py");
            chessEngine = new ChessEngine(playerIsWhite);
            chessGUI = new ChessGUI(chessEngine);
            interpreter = new BoardInterpreter();
        }
        catch(IOException e) {
            System.out.println("Chess Game Initialization Error");
            System.exit(0);
        }
    }

    public ChessGame(boolean virtualGame) {
        if(virtualGame) {
            chessEngine = new ChessEngine(playerIsWhite);
            chessGUI = new ChessGUI(chessEngine);
        }
        chessGUI.displayGame();
    }

    public void runGame() throws IOException, InterruptedException{
        chessGUI.displayGame();
        while (!chessGUI.isGameOver()) {
            TimeUnit.SECONDS.sleep(1);
            if(chessGUI.newGame)
                chessEngine = new ChessEngine(playerIsWhite);
                chessGUI.newGame(chessEngine);
            if (interpreter.hasChanged()) {
                System.out.println("Board has changed");
                chessEngine.tryMoveFromList(interpreter.findChange());
                if (!interpreter.realBoardLive()) {
                    System.out.println("Please fix board");
                    continue;
                }
                interpreter.update();
                //chessGUI.inputMove(reader.getMove());
            }
        }
    }
}

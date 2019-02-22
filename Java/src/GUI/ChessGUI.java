package GUI;

import Board.Chessboard;
import Engine.ChessEngine;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

public class ChessGUI {

    private JFrame frame;

    private ToolBar topToolBar, bottomToolBar;

    private BoardGUI boardGUI;

    private int tileSize = 80;

    private ChessEngine chessEngine;

    public boolean newGame = false;

    public ChessGUI(ChessEngine chessEngine) {
        // Initialize the frame
        frame = new JFrame("Chess");
        topToolBar = new ToolBar("top", new myButtonListener());
        bottomToolBar = new ToolBar("bottom", new myButtonListener());
        frame.add(topToolBar, BorderLayout.PAGE_START);
        frame.add(bottomToolBar, BorderLayout.AFTER_LAST_LINE);
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.setLocation(new Point(750, 0));
        frame.setResizable(false);
        frame.pack();
        frame.setMinimumSize(new Dimension(tileSize*8, tileSize*8 + topToolBar.getHeight()*3));
        newGame(chessEngine);
    }

    public boolean isGameOver() {
        return chessEngine.isGameOver();
    }

    public void newGame(ChessEngine chessEngine) {
        this.chessEngine = chessEngine;
        boardGUI = new BoardGUI(chessEngine, tileSize);
        boardGUI.addMouseListener(new BoardMouseListener());
        frame.add(boardGUI);
        frame.pack();
        chessEngine.nextMove();
        newGame = false;
    }

    public void displayGame() {
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                frame.setVisible(true);
            }
        });
    }

    private void update() {
        boardGUI.repaint();
        frame.repaint();
    }

    public class myButtonListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent actionEvent) {
            if(actionEvent.getSource() == topToolBar.newButton) {
                frame.remove(boardGUI);
                newGame = true;
                update();
            }
            else if(actionEvent.getSource() == topToolBar.resignButton) {
                //add implementation later
            }
            else if(actionEvent.getSource() == topToolBar.undoButton) {
                //System.out.println("undo");
                chessEngine.undo();
                update();
            }
            else if(actionEvent.getSource() == topToolBar.redoButton) {
                //System.out.println("redo");
                chessEngine.redo();
                update();
            }
            else if(actionEvent.getSource() == topToolBar.settingsButton) {
                //add implementation later
            }
        }
    }

    private class BoardMouseListener implements MouseListener {

        public BoardMouseListener() {
            //System.out.println("MouseListener" + counter++);
        }

        @Override
        public void mouseClicked(MouseEvent mouseEvent) {
            int x = mouseEvent.getX();
            int y = mouseEvent.getY();
            int col = x/80 * 80;
            int row = y/80 * 80;
            boardGUI.setSelection(row, col);
            update();
        }

        @Override
        public void mouseEntered(MouseEvent mouseEvent) {}

        @Override
        public void mouseExited(MouseEvent mouseEvent) {}

        @Override
        public void mousePressed(MouseEvent mouseEvent) {}

        @Override
        public void mouseReleased(MouseEvent mouseEvent) {}
    }
}

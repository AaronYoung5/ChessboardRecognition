package GUI;

import Board.Pieces.Piece;
import Board.Pieces.Type;
import Engine.ChessEngine;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import javax.swing.*;

public class BoardGUI extends JComponent {

    private Color dark, light, select, check;
    private Font font;

    private int xy, height, width, boardSize;
    private int selectionX, selectionY, selectionRow, selectionCol;
    private boolean playerIsWhite;

    private ChessEngine chessEngine;

    public BoardGUI(ChessEngine chessEngine, int tileSize) {
        dark = new Color(185, 134, 107);
        light = new Color(242, 216, 189);
        select = new Color(122, 150, 116);
        check = new Color(229, 40, 26);
        font = new Font("Helvetica", Font.BOLD, 36);

        xy = tileSize;//Chess.PIECE_XY;
        height = 1000;//Chess.HEIGHT;
        width = 1000;//Chess.WIDTH;
        boardSize = 8;
        playerIsWhite = true;
        selectionX = Integer.MAX_VALUE;
        selectionY = Integer.MAX_VALUE;

        this.chessEngine = chessEngine;
    }

    public void paintComponent(Graphics g) {
        g.setColor(select);
        g.fillRect(0, 0, width, height);

        paintCheckers(g);
        paintPieces(g);

//		g.setColor(select);
//		g.setFont(font);
//		g.drawString("Invalid", 50, Chess.HEIGHT / 2);
    }

    private void paintPieces(Graphics g) {
        for (int row = 0; row < boardSize; row++) {
            for (int col = 0; col < boardSize; col++) {
                Piece piece = chessEngine.getChessboard().get(row, col).getPiece();
                if (piece != null) {
                    paint(piece, g, col * xy, row * xy);
                }
            }
        }
    }

    private void paintCheckers(Graphics g) {
        boolean swap = true;

        for (int x = 0; x < width; x += xy) {
            for (int y = 0; y < height; y += xy) {
                if(x == selectionX && y == selectionY && chessEngine.getSelectionSize() == 1) {
                    g.setColor(select);
                }
                else if(chessEngine.tileInCheck(y / 80, x / 80)) {
                    g.setColor(check);
                }
                else if (swap) {
                    g.setColor(dark);
                } else {
                    g.setColor(light);
                }

                g.fillRect(x, y, xy, xy);
                swap = !swap;
            }
        }

        g.setColor(new Color(123, 59, 59));
        g.fillRect(0, height-xy/2, width, xy);
    }

    private void paint(Piece piece, Graphics g, int x, int y) {
        ImageIcon image = new ImageIcon( getClass().getResource(getFileName(piece)) );
        g.drawImage(image.getImage(), x + getWidthDifference(image)/2, y + getHeightDifference(image)/2, null);
    }

    private int getWidthDifference(ImageIcon image) {
        return xy - image.getIconWidth();
    }

    private int getHeightDifference(ImageIcon image) {
        return xy - image.getIconHeight();
    }

    private String getFileName(Piece p) {
        String fileName = "";
        if(p.getType() == Type.E) return "assets/BlankImage.png";
        fileName += p.isWhite() ? "white": "black";
        fileName += p.getType().name();
        return "assets/" + fileName + ".png";
    }

    public void setSelection(int y, int x) {
        this.selectionY = y;
        this.selectionX = x;
        this.selectionRow = y / 80;
        this.selectionCol = x / 80;
        chessEngine.setSelection(this.selectionRow, this.selectionCol);
    }
}

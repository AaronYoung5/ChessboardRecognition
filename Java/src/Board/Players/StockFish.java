package Board.Players;

import Board.Chessboard;
import Board.Pieces.Piece;
import Board.Tile;
import Engine.Move;

import java.util.ArrayList;
import java.util.List;

public class StockFish {

    private boolean playerIsWhite;

    public StockFish(boolean playerIsWhite) {
        this.playerIsWhite = playerIsWhite;
    }

    public Move getRandomMove(List<Move> moveList) {
        if (moveList.size() == 0)
            return null;
        int randomIndex = (int)(moveList.size() * Math.random());
        return moveList.get(randomIndex);
    }

    public boolean isWhite() {
        return playerIsWhite;
    }
}

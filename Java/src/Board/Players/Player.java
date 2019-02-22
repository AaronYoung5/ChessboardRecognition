package Board.Players;

import Board.Chessboard;
import Board.Pieces.Piece;
import Board.Tile;
import Engine.ChessEngine;
import Engine.Move;

import java.util.ArrayList;
import java.util.List;

public class Player {

    public static List<Move> findMoves(Chessboard chessboard, boolean playerIsWhite) {
        List<Move> moveList = chessboard.getPossibleMoves(playerIsWhite);
        List<Move> toRemoveList = new ArrayList<>();
        for (Move move : moveList) {
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
}

package net.mauhiz.board.checkers.model;

import static java.lang.Math.abs;

import java.awt.Dimension;
import java.util.HashMap;
import java.util.Map;

import net.mauhiz.board.Board;
import net.mauhiz.board.Player;
import net.mauhiz.board.Square;
import net.mauhiz.board.SquareView;

/**
 * @author mauhiz
 */
public class CheckersBoard extends Board {
    public static final int SIZE = 10;

    static Square getSkippedSquare(Square from, Square to) {
        if (isCornerSkip(from, to)) {
            return Square.getInstance((from.x + to.x) / 2, (from.y + to.y) / 2);
        }
        return null;
    }

    static boolean isCornerSkip(Square from, Square to) {
        return abs(getXmove(from, to)) == 2 && abs(getYmove(from, to)) == 2;
    }

    static boolean isForward(Square from, Square to, CheckersPlayer player) {
        return getYmove(from, to) * (player == CheckersPlayer.BLACK ? 1 : -1) > 0;
    }

    static boolean isFrontCorner(Square from, Square to, CheckersPlayer pl) {
        return isCorner(from, to) && isForward(from, to, pl);
    }

    private final Map<Square, CheckersOwnedPiece> piecesMap = new HashMap<Square, CheckersOwnedPiece>();

    private CheckersPlayer turn = CheckersPlayer.BLACK;

    @Override
    public CheckersOwnedPiece getOwnedPieceAt(Square square) {
        return piecesMap.get(square);
    }

    @Override
    public Dimension getSize() {
        return new Dimension(SIZE, SIZE);
    }

    @Override
    public CheckersPlayer getTurn() {
        return turn;
    }

    @Override
    public boolean move(Player pl, Square from, Square to) {
        if (!pl.equals(turn)) {
            return false;
        }
        CheckersOwnedPiece toMove = getOwnedPieceAt(from);
        if (toMove == null || toMove.getPlayer() != pl) {
            return false;
        }

        if (CheckersRule.canGo(this, toMove, from, to)) {
            piecesMap.remove(from);
            piecesMap.put(to, toMove);

            // capture
            piecesMap.remove(getSkippedSquare(from, to));

            // TODO can capture again with same piece if capture
            turn = turn.next();
            return true;
        }

        return false;
    }

    @Override
    public void newGame() {
        piecesMap.clear();

        for (Square square : new SquareView(getSize())) {
            int j = square.y;
            int i = square.x;

            if (j <= 3 || j >= 6) {
                CheckersPlayer pl = j <= 4 ? CheckersPlayer.BLACK : CheckersPlayer.WHITE;

                if ((i + j) % 2 == 0) { // TODO test
                    piecesMap.put(square, new CheckersOwnedPiece(pl, CheckersPiece.PAWN));
                }
            }
        }

        turn = CheckersPlayer.BLACK;
    }
}

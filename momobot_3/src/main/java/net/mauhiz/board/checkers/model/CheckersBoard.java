package net.mauhiz.board.checkers.model;

import java.awt.Dimension;

import net.mauhiz.board.AbstractBoard;
import net.mauhiz.board.Move;
import net.mauhiz.board.Square;

/**
 * @author mauhiz
 */
public class CheckersBoard extends AbstractBoard<CheckersPiece, CheckersPlayer> {
    public static final int SIZE = 10;

    static Square getSkippedSquare(Square from, Square to) {
        if (isCornerSkip(from, to)) {
            return Square.getInstance((from.x + to.x) / 2, (from.y + to.y) / 2);
        }
        return null;
    }

    @Override
    public CheckersOwnedPiece getOwnedPieceAt(Square square) {
        return super.getOwnedPieceAt(square);
    }

    @Override
    public CheckersRule getRule() {
        return super.getRule();
    }

    @Override
    public Dimension getSize() {
        return new Dimension(SIZE, SIZE);
    }

    @Override
    protected void initGameFor(Square square) {
        int j = square.y;
        int i = square.x;

        if (j <= 3 || j >= 6) {
            CheckersPlayer pl = j <= 4 ? CheckersPlayer.BLACK : CheckersPlayer.WHITE;

            if ((i + j) % 2 == 0) { // TODO test
                piecesMap.put(square, new CheckersOwnedPiece(pl, CheckersPiece.PAWN));
            }
        }
    }

    @Override
    protected void initTurn() {
        turn = CheckersPlayer.BLACK;
    }

    @Override
    protected boolean isForward(Square from, Square to, CheckersPlayer player) {
        return from.y != to.y && player == CheckersPlayer.BLACK ^ from.y < to.y;
    }

    public boolean move(Move move) {
        if (!(move instanceof CheckersMove)) {
            return false;
        }
        Square from = ((CheckersMove) move).getFrom();
        CheckersOwnedPiece toMove = getOwnedPieceAt(from);

        if (toMove == null || !toMove.getPlayer().equals(getTurn())) {
            return false;
        }

        if (getRule().isLegalMove(this, move)) {
            Square to = move.getTo();
            piecesMap.remove(from);
            piecesMap.put(to, toMove);

            // capture
            piecesMap.remove(getSkippedSquare(from, to));

            // TODO can capture again with same piece if capture
            nextTurn();
            return true;
        }

        return false;
    }
}

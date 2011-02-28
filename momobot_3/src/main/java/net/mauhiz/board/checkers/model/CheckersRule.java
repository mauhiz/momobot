package net.mauhiz.board.checkers.model;

import net.mauhiz.board.AbstractBoard;
import net.mauhiz.board.Move;
import net.mauhiz.board.Rule;
import net.mauhiz.board.Square;

public class CheckersRule implements Rule<CheckersBoard> {

    /**
     * @param from
     * @param to
     *            is different from 'from'
     * @return
     */
    public static boolean canGo(CheckersBoard b, Square from, Square to) {
        CheckersOwnedPiece op = b.getOwnedPieceAt(from);

        if (b.getOwnedPieceAt(to) != null) {
            return false;
        }

        if (AbstractBoard.isCornerSkip(from, to)) {
            // capture
            Square skipped = CheckersBoard.getSkippedSquare(from, to);
            CheckersOwnedPiece captured = b.getOwnedPieceAt(skipped);
            if (captured == null || captured.getPlayer() == op.getPlayer()) {
                return false;
            }

        } else if (!AbstractBoard.isCorner(from, to)) {
            return false;
        }

        return b.isForward(from, to, op.getPlayer()) || op.isPromoted();
    }

    public static boolean canPromote(CheckersOwnedPiece op, Square to) {
        return op.getPlayer() == CheckersPlayer.BLACK ? to.y == 9 : to.y == 0;
    }

    @Override
    public boolean isLegalMove(CheckersBoard board, Move move) {
        if (move instanceof CheckersMove) {
            return canGo(board, ((CheckersMove) move).getFrom(), move.getTo());
        }
        return false;
    }
}

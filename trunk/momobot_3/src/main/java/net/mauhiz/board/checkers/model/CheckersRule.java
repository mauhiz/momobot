package net.mauhiz.board.checkers.model;

import net.mauhiz.board.Board;
import net.mauhiz.board.OwnedPiece;
import net.mauhiz.board.Square;

public class CheckersRule {

    /**
     * @param op
     * @param from
     * @param to
     *            is different from 'from'
     * @return
     */
    public static boolean canGo(Board b, CheckersOwnedPiece op, Square from, Square to) {
        if (b.getOwnedPieceAt(to) != null) {
            return false;
        }

        if (CheckersBoard.isCornerSkip(from, to)) {
            // capture
            Square skipped = CheckersBoard.getSkippedSquare(from, to);
            OwnedPiece captured = b.getOwnedPieceAt(skipped);
            if (captured == null || captured.getPlayer() == op.getPlayer()) {
                return false;
            }

        } else if (!Board.isCorner(from, to)) {
            return false;
        }

        return CheckersBoard.isForward(from, to, op.getPlayer()) || op.isPromoted();
    }

    public static boolean canPromote(CheckersOwnedPiece op, Square to) {
        return isPromotionZone(op.getPlayer(), to);
    }

    static boolean isPromotionZone(CheckersPlayer pl, Square here) {
        return pl == CheckersPlayer.BLACK ? here.y == 9 : here.y == 0;
    }
}

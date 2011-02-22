package net.mauhiz.board.checkers.model;

import net.mauhiz.board.Board;
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
        if (b.isFriendlyPieceOn(op.getPlayer(), to)) {
            return false;
        }

        if (op.isPromoted()) {
            switch (op.getPiece()) {
                case PAWN: // TODO
                default:
                    throw new IllegalStateException();
            }
        }

        switch (op.getPiece()) {
            case PAWN: // TODO

            default:
                throw new IllegalStateException();
        }
    }

    public static boolean canPromote(CheckersOwnedPiece op, Square to) {
        return isPromotionZone(op.getPlayer(), to);
    }

    static boolean isPromotionZone(CheckersPlayer pl, Square here) {
        return pl == CheckersPlayer.BLACK ? here.y == 9 : here.y == 0;
    }
}

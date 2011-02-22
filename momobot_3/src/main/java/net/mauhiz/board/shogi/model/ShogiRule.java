package net.mauhiz.board.shogi.model;

import static java.lang.Math.abs;
import net.mauhiz.board.Board;
import net.mauhiz.board.Square;

public class ShogiRule {

    public static boolean canDrop(Board b, Square to) {
        return b.getOwnedPieceAt(to) == null;
    }

    /**
     * @param op
     * @param from
     * @param to
     *            is different from 'from'
     * @return
     */
    public static boolean canGo(Board b, ShogiOwnedPiece op, Square from, Square to) {
        if (b.isFriendlyPieceOn(op.getPlayer(), to)) {
            return false;
        }

        if (op.isPromoted()) {
            switch (op.getPiece()) {
                case PAWN:
                case LANCE:
                case SILVER:
                case KNIGHT:
                    return isGoldMove(from, to, op.getPlayer());
                case BISHOP:
                    return Board.isDiagonal(from, to) && !b.isObstruction(from, to) || Board.isCross(from, to);
                case ROOK:
                    return Board.isStraight(from, to) && !b.isObstruction(from, to) || Board.isCorner(from, to);
                default:
                    throw new IllegalStateException();
            }
        }

        switch (op.getPiece()) {
            case PAWN:
                return isPawnMove(from, to, op.getPlayer());
            case LANCE:
                return !b.isObstruction(from, to) && Board.getXmove(from, to) == 0
                        && Board.getYmove(from, to) * (op.getPlayer() == ShogiPlayer.BOTTOM ? 1 : -1) > 0;
            case SILVER:
                return Board.isCorner(from, to) || isPawnMove(from, to, op.getPlayer());
            case KNIGHT:
                return abs(Board.getXmove(from, to)) == 1
                        && Board.getYmove(from, to) == (op.getPlayer() == ShogiPlayer.BOTTOM ? 2 : -2);
            case GOLD:
                return isGoldMove(from, to, op.getPlayer());
            case BISHOP:
                return Board.isDiagonal(from, to) && !b.isObstruction(from, to);
            case ROOK:
                return Board.isStraight(from, to) && !b.isObstruction(from, to);
            case KING:
                return abs(Board.getXmove(from, to)) <= 1 && abs(Board.getYmove(from, to)) <= 1;
            default:
                throw new IllegalStateException();
        }
    }

    static boolean canJump(ShogiPiece p) {
        return ShogiPiece.KNIGHT == p;
    }

    public static boolean canPromote(ShogiOwnedPiece op, Square from, Square to) {
        if (op.getPiece() == ShogiPiece.GOLD || op.getPiece() == ShogiPiece.KING || op.isPromoted()) {
            return false;
        }
        return isPromotionZone(op.getPlayer(), from) || isPromotionZone(op.getPlayer(), to);
    }

    static boolean isGoldMove(Square from, Square to, ShogiPlayer pl) {
        return Board.isCross(from, to) || ShogiBoard.isFrontCorner(from, to, pl);
    }

    static boolean isPawnMove(Square from, Square to, ShogiPlayer pl) {
        return Board.getXmove(from, to) == 0 && Board.getYmove(from, to) == (pl == ShogiPlayer.BOTTOM ? 1 : -1);
    }

    static boolean isPromotionZone(ShogiPlayer pl, Square here) {
        return pl == ShogiPlayer.BOTTOM ? here.y >= 6 : here.y <= 2;
    }
}

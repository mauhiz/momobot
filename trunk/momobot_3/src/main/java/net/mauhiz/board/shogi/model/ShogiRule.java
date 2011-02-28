package net.mauhiz.board.shogi.model;

import static java.lang.Math.abs;
import net.mauhiz.board.AbstractBoard;
import net.mauhiz.board.Move;
import net.mauhiz.board.Rule;
import net.mauhiz.board.Square;

public class ShogiRule implements Rule<ShogiBoard> {

    public static boolean canDrop(ShogiBoard board, Square to) {
        return board.getOwnedPieceAt(to) == null;
    }

    /**
     * @param from
     * @param to
     *            is different from 'from'
     * @return
     */
    public static boolean canGo(ShogiBoard board, Square from, Square to) {
        ShogiOwnedPiece op = board.getOwnedPieceAt(from);

        if (board.isFriendlyPieceOn(op.getPlayer(), to)) {
            return false;
        }

        if (op.isPromoted()) {
            switch (op.getPiece()) {
                case PAWN:
                case LANCE:
                case SILVER:
                case KNIGHT:
                    return board.isGoldMove(from, to, op.getPlayer());
                case BISHOP:
                    return AbstractBoard.isDiagonal(from, to) && !board.isObstruction(from, to)
                            || AbstractBoard.isCross(from, to);
                case ROOK:
                    return AbstractBoard.isStraight(from, to) && !board.isObstruction(from, to)
                            || AbstractBoard.isCorner(from, to);
                default:
                    throw new IllegalStateException();
            }
        }

        switch (op.getPiece()) {
            case PAWN:
                return board.isPawnMove(from, to, op.getPlayer());
            case LANCE:
                return !board.isObstruction(from, to) && AbstractBoard.getXmove(from, to) == 0
                        && AbstractBoard.getYmove(from, to) * (op.getPlayer() == ShogiPlayer.BOTTOM ? 1 : -1) > 0;
            case SILVER:
                return AbstractBoard.isCorner(from, to) || board.isPawnMove(from, to, op.getPlayer());
            case KNIGHT:
                return abs(AbstractBoard.getXmove(from, to)) == 1
                        && AbstractBoard.getYmove(from, to) == (op.getPlayer() == ShogiPlayer.BOTTOM ? 2 : -2);
            case GOLD:
                return board.isGoldMove(from, to, op.getPlayer());
            case BISHOP:
                return AbstractBoard.isDiagonal(from, to) && !board.isObstruction(from, to);
            case ROOK:
                return AbstractBoard.isStraight(from, to) && !board.isObstruction(from, to);
            case KING:
                return abs(AbstractBoard.getXmove(from, to)) <= 1 && abs(AbstractBoard.getYmove(from, to)) <= 1;
            default:
                throw new IllegalStateException();
        }
    }

    public static boolean canPromote(ShogiBoard board, Square from, Square to) {
        ShogiOwnedPiece op = board.getOwnedPieceAt(from);

        if (op.getPiece() == ShogiPiece.GOLD || op.getPiece() == ShogiPiece.KING || op.isPromoted()) {
            return false;
        }

        return isPromotionZone(op.getPlayer(), from) || isPromotionZone(op.getPlayer(), to);
    }

    static boolean isPromotionZone(ShogiPlayer player, Square square) {
        return player == ShogiPlayer.BOTTOM ? square.y >= 6 : square.y <= 2;
    }

    @Override
    public boolean isLegalMove(ShogiBoard board, Move move) {

        if (move instanceof ShogiMove) {
            if (move instanceof Drop) {
                return canDrop(board, move.getTo());
            }

            Square from = ((ShogiMove) move).getFrom();
            ShogiOwnedPiece toMove = board.getOwnedPieceAt(from);

            if (toMove == null || toMove.getPlayer() != board.getTurn()) {
                return false;
            }

            return canGo(board, from, move.getTo());
        }

        return false;
    }
}

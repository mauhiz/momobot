package net.mauhiz.board.chess.model;

import static java.lang.Math.abs;
import net.mauhiz.board.AbstractBoard;
import net.mauhiz.board.Move;
import net.mauhiz.board.Rule;
import net.mauhiz.board.Square;

public class ChessRule implements Rule<ChessBoard> {

    /**
     * @param from
     * @param to
     *            is different from 'from'
     * @return
     */
    public static boolean canGo(ChessBoard b, Square from, Square to) {
        ChessOwnedPiece op = b.getOwnedPieceAt(from);

        if (b.isFriendlyPieceOn(op.getPlayer(), to)) {
            return false;
        }

        switch (op.getPiece()) {
            case PAWN:
                if (b.getOwnedPieceAt(to) == null) {
                    return b.isPawnMove(from, to, op.getPlayer()) || b.canEnPassant(from, to, op.getPlayer());
                }
                return b.isFrontCorner(from, to, op.getPlayer());
            case KNIGHT:
                return abs(AbstractBoard.getXmove(from, to)) == 1 && abs(AbstractBoard.getYmove(from, to)) == 2
                        || abs(AbstractBoard.getXmove(from, to)) == 2 && abs(AbstractBoard.getYmove(from, to)) == 1;
            case BISHOP:
                return AbstractBoard.isDiagonal(from, to) && !b.isObstruction(from, to);
            case ROOK:
                return AbstractBoard.isStraight(from, to) && !b.isObstruction(from, to);
            case KING:
                return abs(AbstractBoard.getXmove(from, to)) <= 1 && abs(AbstractBoard.getYmove(from, to)) <= 1
                        || b.canCastle(from, to, op.getPlayer());
            case QUEEN:
                return (AbstractBoard.isDiagonal(from, to) || AbstractBoard.isStraight(from, to))
                        && !b.isObstruction(from, to);
            default:
                throw new IllegalStateException();
        }
    }

    public static boolean canPromote(ChessOwnedPiece op, Square to) {
        return ChessPiece.PAWN.equals(op.getPiece()) && isPromotionZone(op.getPlayer(), to);
    }

    static boolean isPromotionZone(ChessPlayer pl, Square here) {
        return ChessPlayer.WHITE.equals(pl) ? here.y == 7 : here.y == 0;
    }

    @Override
    public boolean isLegalMove(ChessBoard board, Move move) {
        if (move instanceof ChessMove) {
            return canGo(board, ((ChessMove) move).getFrom(), move.getTo());
        }
        return false;
    }
}

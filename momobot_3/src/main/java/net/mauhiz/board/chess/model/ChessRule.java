package net.mauhiz.board.chess.model;

import static java.lang.Math.abs;
import net.mauhiz.board.Board;
import net.mauhiz.board.OwnedPiece;
import net.mauhiz.board.Piece;
import net.mauhiz.board.Player;
import net.mauhiz.board.Square;

public class ChessRule {

    /**
     * @param op
     * @param from
     * @param to
     *            is different from 'from'
     * @return
     */
    public static boolean canGo(Board b, ChessOwnedPiece op, Square from, Square to) {
        if (b.isFriendlyPieceOn(op.getPlayer(), to)) {
            return false;
        }

        switch (op.getPiece()) {
            case PAWN:
                if (b.getOwnedPieceAt(to) == null) {
                    return isPawnMove(from, to, op.getPlayer());
                }
                return ChessBoard.isFrontCorner(from, to, op.getPlayer());
            case KNIGHT:
                return abs(Board.getXmove(from, to)) == 1 && abs(Board.getYmove(from, to)) == 2
                        || abs(Board.getXmove(from, to)) == 2 && abs(Board.getYmove(from, to)) == 1;
            case BISHOP:
                return Board.isDiagonal(from, to) && !b.isObstruction(from, to);
            case ROOK:
                return Board.isStraight(from, to) && !b.isObstruction(from, to);
            case KING:
                return abs(Board.getXmove(from, to)) <= 1 && abs(Board.getYmove(from, to)) <= 1;
            case QUEEN:
                return (Board.isDiagonal(from, to) || Board.isStraight(from, to)) && !b.isObstruction(from, to);
            default:
                throw new IllegalStateException();
        }
    }

    static boolean canJump(Piece p) {
        return ChessPiece.KNIGHT.equals(p);
    }

    public static boolean canPromote(OwnedPiece op, Square to) {
        return ChessPiece.PAWN.equals(op.getPiece()) && isPromotionZone(op.getPlayer(), to);
    }

    static boolean isPawnMove(Square from, Square to, Player pl) {
        if (Board.getXmove(from, to) == 0) {
            if (ChessPlayer.WHITE.equals(pl)) {
                return from.y == 1 ? Board.getYmove(from, to) <= 2 : Board.getYmove(from, to) == 1;
            }
            return from.y == 6 ? -Board.getYmove(from, to) <= 2 : -Board.getYmove(from, to) == 1;
        }
        return false;
    }

    static boolean isPromotionZone(Player pl, Square here) {
        return ChessPlayer.WHITE.equals(pl) ? here.y == 7 : here.y == 0;
    }
}

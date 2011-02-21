package net.mauhiz.shogi.model;

import static java.lang.Math.abs;

import java.awt.Point;

public class Rule {
    
    public static boolean canDrop(Board b, Point to) {
        return b.getOwnedPieceAt(to) == null;
    }
    
    /**
     * @param op
     * @param from
     * @param to
     *            is different from 'from'
     * @return
     */
    public static boolean canGo(Board b, OwnedPiece op, Point from, Point to) {
        if (b.isFriendlyPieceOn(op.player, to)) {
            return false;
        }
        
        if (op.promoted) {
            switch (op.piece) {
                case PAWN :
                case LANCE :
                case SILVER :
                case KNIGHT :
                    return isGoldMove(from, to, op.player);
                case BISHOP :
                    return Board.isDiagonal(from, to) && !b.isObstruction(from, to) || Board.isCross(from, to);
                case ROOK :
                    return Board.isStraight(from, to) && !b.isObstruction(from, to) || Board.isCorner(from, to);
                default :
                    throw new IllegalStateException();
            }
        }
        
        switch (op.piece) {
            case PAWN :
                return isPawnMove(from, to, op.player);
            case LANCE :
                return !b.isObstruction(from, to) && Board.getXmove(from, to) == 0
                        && Board.getYmove(from, to) * (op.player == Player.BLACK ? 1 : -1) > 0;
            case SILVER :
                return Board.isCorner(from, to) || isPawnMove(from, to, op.player);
            case KNIGHT :
                return abs(Board.getXmove(from, to)) == 1
                        && Board.getYmove(from, to) == (op.player == Player.BLACK ? 2 : -2);
            case GOLD :
                return isGoldMove(from, to, op.player);
            case BISHOP :
                return Board.isDiagonal(from, to) && !b.isObstruction(from, to);
            case ROOK :
                return Board.isStraight(from, to) && !b.isObstruction(from, to);
            case KING :
                return Board.getXmove(from, to) == 1 || Board.getYmove(from, to) == 1;
            default :
                throw new IllegalStateException();
        }
    }
    
    static boolean canJump(Piece p) {
        return Piece.KNIGHT == p;
    }
    
    public static boolean canPromote(OwnedPiece op, Point from, Point to) {
        if (op.piece == Piece.GOLD || op.piece == Piece.KING || op.promoted) {
            return false;
        }
        return isPromotionZone(op.player, from) || isPromotionZone(op.player, to);
    }
    
    static boolean isGoldMove(Point from, Point to, Player pl) {
        return Board.isCross(from, to) || Board.isFrontCorner(from, to, pl);
    }
    
    static boolean isPawnMove(Point from, Point to, Player pl) {
        return Board.getXmove(from, to) == 0 && Board.getYmove(from, to) == (pl == Player.BLACK ? 1 : -1);
    }
    
    static boolean isPromotionZone(Player pl, Point here) {
        return pl == Player.BLACK ? here.x >= 6 : here.x <= 2;
    }
}

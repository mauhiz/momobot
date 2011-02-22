package net.mauhiz.board;

import static java.lang.Math.abs;

import java.awt.Dimension;

public abstract class Board {

    public static int getXmove(Square from, Square to) {
        return to.x - from.x;
    }

    public static int getYmove(Square from, Square to) {
        return to.y - from.y;
    }

    public static boolean isCorner(Square from, Square to) {
        return abs(getXmove(from, to)) == 1 && abs(getYmove(from, to)) == 1;
    }

    public static boolean isCross(Square from, Square to) {
        return abs(getXmove(from, to)) == 1 ^ abs(getYmove(from, to)) == 1;
    }

    public static boolean isDiagonal(Square from, Square to) {
        return abs(getXmove(from, to)) == abs(getYmove(from, to));
    }

    public static boolean isDownToUp(Square from, Square to) {
        return to.y > from.y;
    }

    public static boolean isHorizontal(Square from, Square to) {
        return getYmove(from, to) == 0;
    }

    public static boolean isLeftToRight(Square from, Square to) {
        return to.x > from.x;
    }

    public static boolean isStraight(Square from, Square to) {
        return isHorizontal(from, to) || isVertical(from, to);
    }

    public static boolean isVertical(Square from, Square to) {
        return getXmove(from, to) == 0;
    }

    protected OwnedPiece getOwnedPieceAt(int i, int j) {
        return getOwnedPieceAt(Square.getInstance(i, j));
    }

    public abstract OwnedPiece getOwnedPieceAt(Square to);

    public abstract Dimension getSize();

    public abstract Player getTurn();

    public boolean isFriendlyPieceOn(Player pl, Square to) {
        OwnedPiece op = getOwnedPieceAt(to);
        return op != null && op.getPlayer().equals(pl);
    }

    public boolean isObstruction(Square from, Square to) {
        if (isDiagonal(from, to)) {
            boolean positiveCoef = isDownToUp(from, to) ^ !isLeftToRight(from, to);
            int minX = Math.min(from.x, to.x);
            int maxX = Math.max(from.x, to.x);

            int minY = Math.min(from.y, to.y);
            int maxY = Math.max(from.y, to.y);

            for (int i = 1; i < maxX - minX; i++) {
                if (getOwnedPieceAt(minX + i, positiveCoef ? minY + i : maxY - i) != null) {
                    return true;
                }
            }
            return false;
        } else if (isHorizontal(from, to)) {
            int horIncr = isLeftToRight(from, to) ? 1 : -1;
            for (int i = from.x + horIncr; i != to.x; i += horIncr) {
                if (getOwnedPieceAt(i, to.y) != null) {
                    return true;
                }
            }
            return false;
        } else if (isVertical(from, to)) {
            int vertIncr = isDownToUp(from, to) ? 1 : -1;
            for (int j = from.y + vertIncr; j != to.y; j += vertIncr) {
                if (getOwnedPieceAt(to.x, j) != null) {
                    return true;
                }
            }
            return false;
        } else {
            return false;
        }
    }

    /**
     * @param from
     * @param to
     * @return true if move was allowed
     */
    public abstract boolean move(Player player, Square from, Square to);

    public abstract void newGame();
}

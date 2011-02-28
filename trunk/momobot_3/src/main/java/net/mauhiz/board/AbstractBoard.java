package net.mauhiz.board;

import static java.lang.Math.abs;

import java.util.HashMap;
import java.util.Map;

public abstract class AbstractBoard<P extends Piece, J extends Player> implements Board<P, J> {

    public static int getXmove(Square from, Square to) {
        return to.x - from.x;
    }

    public static int getYmove(Square from, Square to) {
        return to.y - from.y;
    }

    public static boolean isCorner(Square from, Square to) {
        return abs(getXmove(from, to)) == 1 && abs(getYmove(from, to)) == 1;
    }

    public static boolean isCornerSkip(Square from, Square to) {
        return abs(getXmove(from, to)) == 2 && abs(getYmove(from, to)) == 2;
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

    protected final Map<Square, OwnedPiece<P, J>> piecesMap = new HashMap<Square, OwnedPiece<P, J>>();
    private Rule<? extends Board<P, J>> rule;
    protected J turn;

    protected OwnedPiece<P, J> getOwnedPieceAt(int i, int j) {
        return getOwnedPieceAt(Square.getInstance(i, j));
    }

    public <O extends OwnedPiece<P, J>> O getOwnedPieceAt(Square square) {
        return (O) piecesMap.get(square);
    }

    @Override
    public <R extends Rule<? extends Board<P, J>>> R getRule() {
        return (R) rule;
    }

    public J getTurn() {
        return turn;
    }

    protected abstract void initGameFor(Square square);

    protected abstract void initTurn();

    protected abstract boolean isForward(Square from, Square to, J player);

    public boolean isFriendlyPieceOn(J pl, Square to) {
        OwnedPiece<P, J> op = getOwnedPieceAt(to);
        return op != null && op.getPlayer().equals(pl);
    }

    public boolean isFrontCorner(Square from, Square to, J player) {
        return isCorner(from, to) && isForward(from, to, player);
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

    public void newGame() {
        piecesMap.clear();

        for (Square square : new SquareView(getSize())) {
            initGameFor(square);
        }

        initTurn();
    }

    protected void nextTurn() {
        turn = (J) turn.next();
    }

    @Override
    public <R extends Rule<? extends Board<P, J>>> void setRule(R rule) {
        this.rule = rule;
    }
}

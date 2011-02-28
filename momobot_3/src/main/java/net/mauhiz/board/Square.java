package net.mauhiz.board;

public class Square {
    private static final int _CACHE_HEIGHT = 10;
    private static final int _CACHE_WIDTH = 10;
    private static final Square[][] CACHE = new Square[_CACHE_WIDTH][_CACHE_HEIGHT];

    public static Square getInstance(int x, int y) {
        boolean inRange = x >= 0 && x < _CACHE_WIDTH && y >= 0 && y < _CACHE_HEIGHT;
        Square square = inRange ? CACHE[x][y] : null;

        if (square == null) {
            square = new Square(x, y);
            if (inRange) {
                CACHE[x][y] = square;
            }
        }

        return square;
    }

    public final int x;
    public final int y;

    protected Square(int x, int y) {
        this.x = x;
        this.y = y;
    }

    @Override
    public boolean equals(Object obj) {
        return this == obj || obj instanceof Square && ((Square) obj).x == x && ((Square) obj).y == y;
    }

    @Override
    public int hashCode() {
        return x + 377 * y;
    }

    @Override
    public String toString() {
        return "(" + x + "," + y + ")";
    }
}

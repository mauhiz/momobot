package net.mauhiz.board.impl.common.data;

import net.mauhiz.board.model.data.Square;

public class SquareImpl implements Square {

	private static final int _CACHE_HEIGHT = 10;
	private static final int _CACHE_WIDTH = 10;
	private static final Square[][] CACHE = new Square[_CACHE_WIDTH][_CACHE_HEIGHT];

	public static Square getInstance(int x, int y) {
		boolean inRange = x >= 0 && x < _CACHE_WIDTH && y >= 0 && y < _CACHE_HEIGHT;
		Square square = inRange ? CACHE[x][y] : null;

		if (square == null) {
			square = new SquareImpl(x, y);
			if (inRange) {
				CACHE[x][y] = square;
			}
		}

		return square;
	}

	public final int x;
	public final int y;

	public SquareImpl(int x, int y) {
		this.x = x;
		this.y = y;
	}

	@Override
	public boolean equals(Object obj) {
		return this == obj || obj instanceof Square && ((Square) obj).getX() == x && ((Square) obj).getY() == y;
	}

	public int getX() {
		return x;
	}

	public int getY() {
		return y;
	}

	@Override
	public int hashCode() {
		return x + 377 * y;
	}

	@Override
	public String toString() {
		// indices are 0-based, so add a 1 offset to display
		return "(" + (x + 1) + "," + (y + 1) + ")";
	}

}

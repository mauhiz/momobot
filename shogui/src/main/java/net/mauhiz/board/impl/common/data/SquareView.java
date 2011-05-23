package net.mauhiz.board.impl.common.data;

import java.awt.Dimension;
import java.util.Iterator;

import net.mauhiz.board.model.data.Square;

public class SquareView implements Iterable<Square>, Iterator<Square> {

	private final Dimension boardSize;
	private int counter;

	public SquareView(Dimension boardSize) {
		super();
		this.boardSize = boardSize;
	}

	private int getNumSquares() {
		return boardSize.height * boardSize.width;
	}

	public boolean hasNext() {
		return counter < getNumSquares();
	}

	public Iterator<Square> iterator() {
		counter = 0;
		return this;
	}

	public Square next() {
		int x = counter % boardSize.width;
		int y = (counter - x) / boardSize.width;
		counter++;
		return SquareImpl.getInstance(x, y);
	}

	public void remove() {
		throw new UnsupportedOperationException();
	}
}

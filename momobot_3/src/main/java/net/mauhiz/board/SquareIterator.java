package net.mauhiz.board;

import java.awt.Dimension;
import java.util.Iterator;

public class SquareIterator implements Iterator<Square> {

    private final Dimension boardSize;
    private int counter;

    public SquareIterator(Dimension boardSize) {
        super();
        this.boardSize = boardSize;
    }

    private int getNumSquares() {
        return boardSize.height * boardSize.width;
    }

    @Override
    public boolean hasNext() {
        return counter < getNumSquares();
    }

    @Override
    public Square next() {
        int x = counter % boardSize.width;
        int y = (counter - x) / boardSize.width;
        counter++;
        return Square.getInstance(x, y);
    }

    @Override
    public void remove() {
        throw new UnsupportedOperationException();
    }
}

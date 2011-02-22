package net.mauhiz.board;

import java.awt.Dimension;
import java.util.Iterator;

public class SquareView implements Iterable<Square> {

    private final Dimension boardSize;

    public SquareView(Dimension boardSize) {
        super();
        this.boardSize = boardSize;
    }

    @Override
    public Iterator<Square> iterator() {
        return new SquareIterator(boardSize);
    }
}

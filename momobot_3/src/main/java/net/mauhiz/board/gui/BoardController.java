package net.mauhiz.board.gui;

import java.awt.Dimension;

import net.mauhiz.board.Board;
import net.mauhiz.board.Square;
import net.mauhiz.board.SquareView;

public abstract class BoardController {
    protected Board board;
    protected IBoardGui display;
    protected Square selectedSquare;

    public void cancelSelection() {
        selectedSquare = null;
        refresh();
    }

    protected void clear() {
        display.clear();
    }

    public Board getBoard() {
        return board;
    }

    public Dimension getBoardSize() {
        return getBoard().getSize();
    }

    protected IBoardGui getDisplay() {
        return display;
    }

    public Iterable<Square> getSquares() {
        return new SquareView(getBoardSize());
    }

    public void init() {
        clear();

        getBoard().newGame();
        final Dimension size = getBoardSize();

        display.initLayout(size);

        for (Square square : getSquares()) {
            display.appendSquare(square, size);
        }

        refresh();
    }

    public abstract void movePiece(Square to);

    protected abstract void refresh();

    public abstract void selectPiece(Square at);
}

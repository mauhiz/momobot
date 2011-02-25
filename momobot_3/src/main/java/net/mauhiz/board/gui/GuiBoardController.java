package net.mauhiz.board.gui;

import java.awt.Dimension;

import net.mauhiz.board.Board;
import net.mauhiz.board.IBoardController;
import net.mauhiz.board.Move;
import net.mauhiz.board.Square;
import net.mauhiz.board.SquareView;

public abstract class GuiBoardController<B extends Board, M extends Move<B>> implements IBoardController<B, M> {
    protected B board;
    protected IBoardGui<B, M> display;
    protected Square selectedSquare;

    protected GuiBoardController(IBoardGui<B, M> display, B board) {
        super();
        this.display = display;
        this.board = board;
    }

    public void cancelSelection() {
        selectedSquare = null;
        refresh();
    }

    protected void clear() {
        display.clear();
    }

    public void doMove(M move) {
        movePiece(move.getTo());
    }

    public B getBoard() {
        return board;
    }

    protected Dimension getBoardSize() {
        return getBoard().getSize();
    }

    protected IBoardGui<B, M> getDisplay() {
        return display;
    }

    public Iterable<Square> getSquares() {
        return new SquareView(getBoardSize());
    }

    public void init() {
        clear();

        getBoard().newGame();
        final Dimension size = getBoardSize();

        getDisplay().initLayout(size);

        for (Square square : getSquares()) {
            getDisplay().appendSquare(square, size);
        }

        refresh();
    }

    public abstract void movePiece(Square to);

    public abstract void refresh();

    public final void selectPiece(Square at) {
        selectedSquare = at;
        refresh();
    }
}

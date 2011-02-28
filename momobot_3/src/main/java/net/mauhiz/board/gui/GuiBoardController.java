package net.mauhiz.board.gui;

import java.awt.Dimension;

import net.mauhiz.board.Board;
import net.mauhiz.board.Move;
import net.mauhiz.board.Piece;
import net.mauhiz.board.Player;
import net.mauhiz.board.Square;

public abstract class GuiBoardController<B extends Board<? extends Piece, ? extends Player>, M extends Move> extends
        AbstractBoardController<B, M> {
    private final IBoardGui<B, M> display;
    protected Square selectedSquare;

    protected GuiBoardController(IBoardGui<B, M> display, B board) {
        super(board);
        this.display = display;
    }

    public void cancelSelection() {
        selectedSquare = null;
        refresh();
    }

    protected void clear() {
        display.clear();
    }

    @Override
    public void doMove(M move) {
        movePiece(move.getTo());
    }

    protected <G extends IBoardGui<B, M>> G getDisplay() {
        return (G) display;
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

package net.mauhiz.board.gui;

import java.awt.Color;
import java.awt.Dimension;
import java.util.HashMap;
import java.util.Map;

import net.mauhiz.board.Board;
import net.mauhiz.board.IBoardController;
import net.mauhiz.board.Move;
import net.mauhiz.board.Square;
import net.mauhiz.util.AbstractAction;

public abstract class AbstractBoardGui<B extends Board, M extends Move<B>> implements IBoardGui<B, M> {
    protected final Map<Square, AbstractAction> listeners = new HashMap<Square, AbstractAction>();

    public void addCancelAction(Square square, GuiBoardController<B, M> controller) {
        enableSquare(square, new CancelAction<B, M>(controller));
    }

    public void addMoveAction(Square square, GuiBoardController<B, M> controller) {
        enableSquare(square, new MoveAction<B, M>(controller, square));
    }

    public void addSelectAction(Square square, GuiBoardController<B, M> controller) {
        enableSquare(square, new SelectAction<B, M>(controller, square));
    }

    protected abstract void enableSquare(Square square, AbstractAction action);

    public Dimension getDefaultSize() {
        return new Dimension(400, 400);
    }

    @Override
    public Dimension getMinimumSize() {
        return new Dimension(0, 0);
    }

    public Color getSquareBgcolor(Square square) {
        return (square.x + square.y) % 2 == 0 ? Color.DARK_GRAY : Color.LIGHT_GRAY;
    }

    protected abstract String getWindowTitle();

    protected abstract GuiBoardController<B, M> newController();

    public void newGame(IBoardController<B, M> controller) {
        controller.init();
    }

}

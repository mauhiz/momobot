package net.mauhiz.board.gui;

import java.awt.Color;
import java.awt.Dimension;
import java.util.HashMap;
import java.util.Map;

import net.mauhiz.board.Board;
import net.mauhiz.board.IBoardController;
import net.mauhiz.board.Move;
import net.mauhiz.board.Piece;
import net.mauhiz.board.Player;
import net.mauhiz.board.Square;
import net.mauhiz.util.IAction;

public abstract class AbstractBoardGui<B extends Board<? extends Piece, ? extends Player>, M extends Move> implements
        IBoardGui<B, M> {
    protected final Map<Square, IAction> listeners = new HashMap<Square, IAction>();

    public void addCancelAction(Square square, IGuiBoardController<B, M> controller) {
        enableSquare(square, new CancelAction<B, M>(controller));
    }

    public void addMoveAction(Square square, IGuiBoardController<B, M> controller) {
        enableSquare(square, new MoveAction<B, M>(controller, square));
    }

    public void addSelectAction(Square square, IGuiBoardController<B, M> controller) {
        enableSquare(square, new SelectAction<B, M>(controller, square));
    }

    protected abstract void enableSquare(Square square, IAction action);

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

    protected abstract IBoardController<B, M> newController();

    public void newGame(IBoardController<B, M> controller) {
        controller.init();
    }

}

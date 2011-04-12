package net.mauhiz.board.gui;

import net.mauhiz.board.Board;
import net.mauhiz.board.Move;
import net.mauhiz.board.Piece;
import net.mauhiz.board.Player;
import net.mauhiz.util.AbstractAction;

/**
 * @author mauhiz
 */
public class ExitAction<B extends Board<? extends Piece, ? extends Player>, M extends Move> extends AbstractAction {
    private final IBoardGui<B, M> gui;

    public ExitAction(IBoardGui<B, M> gui) {
        super();
        this.gui = gui;
    }

    @Override
    protected void doAction() {
        gui.close();
    }

    @Override
    protected boolean isAsynchronous() {
        return false; // I want to quit now!
    }
}

package net.mauhiz.board.gui;

import net.mauhiz.board.Board;
import net.mauhiz.board.Move;
import net.mauhiz.util.AbstractAction;

/**
 * @author mauhiz
 */
public class ExitAction<B extends Board, M extends Move<B>> extends AbstractAction {
    private final IBoardGui<B, M> gui;

    public ExitAction(IBoardGui<B, M> gui) {
        this.gui = gui;
    }

    @Override
    protected void doAction() {
        gui.close();
    }
}

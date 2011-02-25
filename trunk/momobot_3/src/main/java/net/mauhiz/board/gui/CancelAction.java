package net.mauhiz.board.gui;

import net.mauhiz.board.Board;
import net.mauhiz.board.Move;
import net.mauhiz.util.AbstractAction;

/**
 * @author mauhiz
 */
public class CancelAction<B extends Board, M extends Move<B>> extends AbstractAction {

    private final GuiBoardController<B, M> gui;

    public CancelAction(GuiBoardController<B, M> gui) {
        this.gui = gui;
    }

    @Override
    protected void doAction() {
        gui.cancelSelection();
    }

}

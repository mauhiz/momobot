package net.mauhiz.board.gui;

import net.mauhiz.util.AbstractAction;

/**
 * @author mauhiz
 */
public class CancelAction extends AbstractAction {

    private final BoardController gui;

    public CancelAction(BoardController gui) {
        this.gui = gui;
    }

    @Override
    protected void doAction() {
        gui.cancelSelection();
    }

}

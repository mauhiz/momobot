package net.mauhiz.board.gui;

import net.mauhiz.util.AbstractAction;

/**
 * @author mauhiz
 */
public class ExitAction extends AbstractAction {
    private final IBoardGui gui;

    public ExitAction(IBoardGui gui) {
        this.gui = gui;
    }

    @Override
    protected void doAction() {
        gui.close();
    }
}

package net.mauhiz.board.gui;

import net.mauhiz.util.AbstractAction;

/**
 * @author mauhiz
 */
public class StartAction extends AbstractAction {

    private final BoardController controller;
    private final IBoardGui gui;

    public StartAction(IBoardGui gui, BoardController controller) {
        super();
        this.gui = gui;
        this.controller = controller;
    }

    @Override
    public void doAction() {
        gui.newGame(controller);
    }
}

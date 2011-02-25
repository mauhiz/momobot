package net.mauhiz.board.gui;

import net.mauhiz.board.Board;
import net.mauhiz.board.IBoardController;
import net.mauhiz.board.Move;
import net.mauhiz.util.AbstractAction;

/**
 * @author mauhiz
 */
public class StartAction<B extends Board, M extends Move<B>> extends AbstractAction {

    private final IBoardController<B, M> controller;
    private final IBoardGui<B, M> gui;

    public StartAction(IBoardGui<B, M> gui, IBoardController<B, M> controller) {
        super();
        this.gui = gui;
        this.controller = controller;
    }

    @Override
    public void doAction() {
        gui.newGame(controller);
    }
}

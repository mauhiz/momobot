package net.mauhiz.board.gui;

import net.mauhiz.board.Board;
import net.mauhiz.board.Move;
import net.mauhiz.board.Square;
import net.mauhiz.util.AbstractAction;

/**
 * @author mauhiz
 */
public class SelectAction<B extends Board, M extends Move<B>> extends AbstractAction {

    private final Square at;
    private final GuiBoardController<B, M> controller;

    public SelectAction(GuiBoardController<B, M> controller, Square at) {
        this.controller = controller;
        this.at = at;
    }

    @Override
    protected void doAction() {
        controller.selectPiece(at);
    }
}

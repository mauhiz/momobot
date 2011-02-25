package net.mauhiz.board.gui;

import net.mauhiz.board.Board;
import net.mauhiz.board.Move;
import net.mauhiz.board.Square;
import net.mauhiz.util.AbstractAction;

/**
 * @author mauhiz
 */
public class MoveAction<B extends Board, M extends Move<B>> extends AbstractAction {

    private final GuiBoardController<B, M> controller;
    private final Square to;

    public MoveAction(GuiBoardController<B, M> controller, Square to) {
        this.to = to;
        this.controller = controller;
    }

    @Override
    protected void doAction() {
        controller.movePiece(to);
    }
}

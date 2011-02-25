package net.mauhiz.board.gui;

import net.mauhiz.board.Square;
import net.mauhiz.util.AbstractAction;

/**
 * @author mauhiz
 */
public class MoveAction extends AbstractAction {

    private final BoardController controller;
    private final Square to;

    public MoveAction(BoardController controller, Square to) {
        this.to = to;
        this.controller = controller;
    }

    @Override
    protected void doAction() {
        controller.movePiece(to);
    }
}

package net.mauhiz.board.gui;

import net.mauhiz.board.Square;
import net.mauhiz.util.AbstractAction;

/**
 * @author mauhiz
 */
public class SelectAction extends AbstractAction {

    private final Square at;
    private final BoardController controller;

    public SelectAction(BoardController controller, Square at) {
        this.controller = controller;
        this.at = at;
    }

    @Override
    protected void doAction() {
        controller.selectPiece(at);
    }
}

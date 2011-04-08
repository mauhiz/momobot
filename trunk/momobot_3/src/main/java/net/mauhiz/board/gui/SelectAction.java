package net.mauhiz.board.gui;

import net.mauhiz.board.Board;
import net.mauhiz.board.Move;
import net.mauhiz.board.Piece;
import net.mauhiz.board.Player;
import net.mauhiz.board.Square;
import net.mauhiz.util.AbstractAction;

/**
 * @author mauhiz
 */
public class SelectAction<B extends Board<? extends Piece, ? extends Player>, M extends Move> extends AbstractAction {

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

    @Override
    protected boolean isAsynchronous() {
        return false; // this should be quick
    }
}

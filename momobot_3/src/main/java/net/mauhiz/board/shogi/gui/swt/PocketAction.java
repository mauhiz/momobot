package net.mauhiz.board.shogi.gui.swt;

import net.mauhiz.board.shogi.gui.ShogiBoardController;
import net.mauhiz.board.shogi.model.ShogiPiece;
import net.mauhiz.util.AbstractAction;

public class PocketAction extends AbstractAction {

    private final ShogiBoardController controller;
    private final ShogiPiece piece;

    public PocketAction(ShogiBoardController controller, ShogiPiece piece) {
        super();
        this.controller = controller;
        this.piece = piece;
    }

    @Override
    protected void doAction() {
        controller.selectPiece(piece);
    }

    @Override
    protected boolean isAsynchronous() {
        return false; // should be quick
    }
}

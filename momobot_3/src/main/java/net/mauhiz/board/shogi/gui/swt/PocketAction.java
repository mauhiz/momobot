package net.mauhiz.board.shogi.gui.swt;

import net.mauhiz.board.shogi.gui.ShogiBoardController;
import net.mauhiz.board.shogi.model.ShogiPiece;

import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;

public class PocketAction implements SelectionListener {

    private ShogiBoardController controller;
    private ShogiPiece piece;

    public PocketAction(ShogiBoardController controller, ShogiPiece piece) {
        this.controller = controller;
        this.piece = piece;
    }

    @Override
    public void widgetDefaultSelected(SelectionEvent arg0) {
        // TODO Auto-generated method stub

    }

    @Override
    public void widgetSelected(SelectionEvent arg0) {
        controller.selectPiece(piece);
    }

}

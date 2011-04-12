package net.mauhiz.board.shogi.gui.awt;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import net.mauhiz.board.shogi.gui.ShogiBoardController;
import net.mauhiz.board.shogi.model.ShogiPiece;

public class PocketAction implements ActionListener {

    private final ShogiBoardController controller;
    private final ShogiPiece piece;

    public PocketAction(ShogiBoardController controller, ShogiPiece piece) {
        this.controller = controller;
        this.piece = piece;
    }

    @Override
    public void actionPerformed(ActionEvent arg0) {
        controller.selectPiece(piece);
    }

}

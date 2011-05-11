package net.mauhiz.board.impl.checkers.gui;

import java.awt.Color;

import net.mauhiz.board.impl.checkers.data.CheckersPlayerType;
import net.mauhiz.board.impl.common.gui.SwingGuiAssistant;
import net.mauhiz.board.impl.common.gui.rotation.RotatingJButton;
import net.mauhiz.board.model.data.Piece;
import net.mauhiz.board.model.gui.BoardGui;

public class CheckersSwingAssistant extends SwingGuiAssistant {

    public CheckersSwingAssistant(BoardGui parent) {
        super(parent);
    }

    @Override
    protected void decorate(RotatingJButton button, Piece piece) {
        if (piece == null) {
            button.setText("");
        } else {
            button.setText(piece.getPieceType().toString());
            button.setForeground(piece.getPlayerType() == CheckersPlayerType.BLACK ? Color.BLACK : Color.WHITE);
        }
    }

}

package net.mauhiz.board.impl.chess.gui;

import java.awt.Color;
import java.awt.GridLayout;

import javax.swing.JButton;
import javax.swing.JDialog;

import net.mauhiz.board.impl.chess.data.ChessPieceType;
import net.mauhiz.board.impl.chess.data.ChessPlayerType;
import net.mauhiz.board.impl.common.gui.SwingGuiAssistant;
import net.mauhiz.board.impl.common.gui.rotation.RotatingJButton;
import net.mauhiz.board.model.data.NormalMove;
import net.mauhiz.board.model.data.Piece;
import net.mauhiz.board.model.gui.BoardGui;

/**
 * @author mauhiz
 */
public class SwingChessGuiAssistant extends SwingGuiAssistant {

    public SwingChessGuiAssistant(BoardGui parent) {
        super(parent);
    }

    @Override
    public void decorate(RotatingJButton button, Piece op) {
        if (op == null) {
            button.setText("");
        } else {
            button.setText(op.getPieceType().toString());
            button.setForeground(op.getPlayerType() == ChessPlayerType.BLACK ? Color.BLACK : Color.WHITE);
        }
    }

    public void showPromotionDialog(ChessPieceType[] promotions, NormalMove nmove) {
        final JDialog popup = new JDialog(frame, "Promotion?");
        popup.setLayout(new GridLayout(1, promotions.length));
        popup.setModal(true);

        for (final ChessPieceType promotion : promotions) {
            JButton promoButton = new JButton(promotion.getName());
            promoButton.addActionListener(new PromoteAction(nmove, parent, promotion));
            popup.add(promoButton);
        }
        popup.pack();
        popup.setVisible(true);
    }
}

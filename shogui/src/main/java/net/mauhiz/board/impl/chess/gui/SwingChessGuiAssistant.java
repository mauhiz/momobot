package net.mauhiz.board.impl.chess.gui;

import java.awt.Color;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JDialog;

import net.mauhiz.board.impl.chess.PromoteMove;
import net.mauhiz.board.impl.chess.data.ChessPieceType;
import net.mauhiz.board.impl.chess.data.ChessPlayerType;
import net.mauhiz.board.impl.common.gui.SwingGuiAssistant;
import net.mauhiz.board.impl.common.gui.rotation.RotatingJButton;
import net.mauhiz.board.model.data.Move;
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

	public void decorate(RotatingJButton button, Piece op) {
		if (op == null) {
			button.setText("");
		} else {
			button.setText(op.getPieceType().toString());
			button.setForeground(op.getPlayerType() == ChessPlayerType.BLACK ? Color.BLACK : Color.WHITE);
		}
	}

	public void showPromotionDialog(ChessPieceType[] promotions, final NormalMove nmove) {
		final JDialog popup = new JDialog(frame, "Promotion?");
		popup.setLayout(new GridLayout(1, promotions.length));
		popup.setModal(true);
		final BoardGui lparent = parent;

		for (final ChessPieceType promotion : promotions) {
			JButton promoButton = new JButton(promotion.getName());
			promoButton.addActionListener(new ActionListener() {

				@Override
				public void actionPerformed(ActionEvent e) {
					Move promoteMove = new PromoteMove(nmove, promotion);
					lparent.sendMove(promoteMove);
				}
			});
			popup.add(promoButton);
		}
		popup.pack();
		popup.setVisible(true);
	}
}

package net.mauhiz.board.impl.chess.gui;

import java.awt.Button;
import java.awt.Color;
import java.awt.Dialog;
import java.awt.Frame;
import java.awt.GridLayout;

import net.mauhiz.board.impl.chess.data.ChessPieceType;
import net.mauhiz.board.impl.chess.data.ChessPlayerType;
import net.mauhiz.board.impl.common.assistant.applet.AwtGuiAssistant;
import net.mauhiz.board.impl.common.assistant.swing.button.RotatingJButton;
import net.mauhiz.board.model.data.NormalMove;
import net.mauhiz.board.model.data.PieceType;
import net.mauhiz.board.model.data.PlayerType;
import net.mauhiz.board.model.gui.BoardGui;

/**
 * @author mauhiz
 */
public class AwtChessGuiAssistant extends AwtGuiAssistant implements IChessGuiAssistant {

	public AwtChessGuiAssistant(BoardGui parent) {
		super(parent);
	}

	@Override
	public void decorate(RotatingJButton button, PieceType op, PlayerType player) {
		if (op == null) {
			button.setText("");
		} else {
			button.setText(op.toString());
			button.setForeground(player == ChessPlayerType.BLACK ? Color.BLACK : Color.WHITE);
		}
	}

	public void showPromotionDialog(ChessPieceType[] promotions, NormalMove nmove) {
		final Dialog popup = new Dialog(Frame.getFrames()[0], "Promotion?");
		popup.setLayout(new GridLayout(1, promotions.length));
		popup.setModal(true);

		for (final ChessPieceType promotion : promotions) {
			Button promoButton = new Button(promotion.getName());
			promoButton.addActionListener(new PromoteAction(nmove, getParent(), promotion));
			popup.add(promoButton);
		}
		popup.pack();
		popup.setVisible(true);
	}
}

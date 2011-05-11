package net.mauhiz.board.impl.chess.gui;

import java.awt.Button;
import java.awt.Color;
import java.awt.Dialog;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import net.mauhiz.board.impl.chess.PromoteMove;
import net.mauhiz.board.impl.chess.data.ChessPieceType;
import net.mauhiz.board.impl.chess.data.ChessPlayerType;
import net.mauhiz.board.impl.common.gui.AwtGuiAssistant;
import net.mauhiz.board.model.data.Move;
import net.mauhiz.board.model.data.NormalMove;
import net.mauhiz.board.model.data.Piece;
import net.mauhiz.board.model.gui.BoardGui;

/**
 * @author mauhiz
 */
public class AwtChessGuiAssistant extends AwtGuiAssistant {

	public AwtChessGuiAssistant(BoardGui parent) {
		super(parent);
	}

	public void decorate(Button button, Piece op) {
		if (op == null) {
			button.setLabel("");
		} else {
			button.setLabel(op.getPieceType().toString());
			button.setForeground(op.getPlayerType() == ChessPlayerType.BLACK ? Color.BLACK : Color.WHITE);
		}
	}

	public void showPromotionDialog(ChessPieceType[] promotions, final NormalMove nmove) {
		final Dialog popup = new Dialog(frame, "Promotion?");
		popup.setLayout(new GridLayout(1, promotions.length));
		popup.setModal(true);
		final BoardGui lparent = parent;

		for (final ChessPieceType promotion : promotions) {
			Button promoButton = new Button(promotion.getName());
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

	@Override
	public void start() {
		// TODO Auto-generated method stub

	}

	@Override
	public void refresh() {
		// TODO Auto-generated method stub

	}
}

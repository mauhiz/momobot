package net.mauhiz.board.impl.shogi.gui;

import java.awt.Button;
import java.awt.Color;
import java.awt.Dialog;
import java.awt.GridLayout;
import java.awt.Panel;

import net.mauhiz.board.impl.common.gui.PocketAwtGuiAssistant;
import net.mauhiz.board.impl.shogi.data.ShogiPlayerType;
import net.mauhiz.board.model.data.NormalMove;
import net.mauhiz.board.model.data.Piece;
import net.mauhiz.board.model.gui.PocketBoardGui;
import net.mauhiz.util.AbstractAction;
import net.mauhiz.util.ExecutionType;

/**
 * @author mauhiz
 */
public class ShogiAwtAssistant extends PocketAwtGuiAssistant implements IShogiGuiAssistant {

	public ShogiAwtAssistant(PocketBoardGui parent) {
		super(parent);
	}

	@Override
	public void decorate(Button button, Piece op) {
		if (op == null) {
			button.setLabel("");
		} else {
			button.setLabel(op.getPieceType().toString());
			button.setForeground(op.getPlayerType() == ShogiPlayerType.SENTE ? Color.BLACK : Color.WHITE);
		}
	}

	@Override
	protected ShogiGui getParent() {
		return (ShogiGui) super.getParent();
	}

	@Override
	public void initPockets() {
		Panel pocket = new Panel();
		frame.add(pocket, 0);
		pockets.put(ShogiPlayerType.SENTE, pocket);
		pocket = new Panel();
		frame.add(pocket);
		pockets.put(ShogiPlayerType.GOTE, pocket);
	}

	public void showPromotionDialog(final NormalMove move) {
		final Dialog popup = new Dialog(frame, "Promotion?");
		popup.setLayout(new GridLayout(1, 2, 10, 10));
		popup.setModal(true);

		Button promoButton = new Button("Yes");
		promoButton.addActionListener(new AbstractAction() {

			@Override
			protected void doAction() {
				getParent().afterPromotionDialog(move, true);
				popup.dispose();
			}

			@Override
			protected ExecutionType getExecutionType() {
				return ExecutionType.NON_GUI;
			}
		});
		popup.add(promoButton);

		Button cancelButton = new Button("No");
		cancelButton.addActionListener(new AbstractAction() {

			@Override
			public void doAction() {
				getParent().afterPromotionDialog(move, false);
				popup.dispose();
			}

			@Override
			protected ExecutionType getExecutionType() {
				return ExecutionType.NON_GUI;
			}
		});
		popup.add(cancelButton);

		popup.pack();
		popup.setVisible(true);
	}
}

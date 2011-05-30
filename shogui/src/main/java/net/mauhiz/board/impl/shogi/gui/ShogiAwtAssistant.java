package net.mauhiz.board.impl.shogi.gui;

import java.awt.Button;
import java.awt.Color;
import java.awt.Dialog;
import java.awt.GridLayout;
import java.awt.Panel;

import net.mauhiz.board.impl.common.gui.PocketAwtGuiAssistant;
import net.mauhiz.board.impl.shogi.data.ShogiPlayerType;
import net.mauhiz.board.model.data.NormalMove;
import net.mauhiz.board.model.data.PieceType;
import net.mauhiz.board.model.data.PlayerType;
import net.mauhiz.board.model.gui.PocketBoardGui;
import net.mauhiz.util.AbstractAction;
import net.mauhiz.util.ExecutionType;

/**
 * @author mauhiz
 */
public class ShogiAwtAssistant extends PocketAwtGuiAssistant implements IShogiGuiAssistant {

	class PromotionAcceptedAction extends AbstractAction {
		private final NormalMove move;
		private final Dialog popup;

		PromotionAcceptedAction(Dialog popup, NormalMove move) {
			super();
			this.popup = popup;
			this.move = move;
		}

		@Override
		protected ExecutionType getExecutionType() {
			return ExecutionType.GUI_ASYNCHRONOUS;
		}

		@Override
		public void trun() {
			getParent().afterPromotionDialog(move, true);
			popup.dispose();
		}
	}

	class PromotionRefusedAction extends AbstractAction {
		private final NormalMove move;
		private final Dialog popup;

		PromotionRefusedAction(Dialog popup, NormalMove move) {
			super();
			this.popup = popup;
			this.move = move;
		}

		@Override
		protected ExecutionType getExecutionType() {
			return ExecutionType.GUI_ASYNCHRONOUS;
		}

		@Override
		public void trun() {
			getParent().afterPromotionDialog(move, false);
			popup.dispose();
		}
	}

	public ShogiAwtAssistant(PocketBoardGui parent) {
		super(parent);
	}

	@Override
	public void decorate(Button button, PieceType op, PlayerType player) {
		if (op == null) {
			button.setLabel("");
		} else {
			button.setLabel(op.toString());
			button.setForeground(player == ShogiPlayerType.SENTE ? Color.BLACK : Color.WHITE);
		}
	}

	@Override
	protected ShogiGui getParent() {
		return (ShogiGui) super.getParent();
	}

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
		promoButton.addActionListener(new PromotionAcceptedAction(popup, move));
		popup.add(promoButton);

		Button cancelButton = new Button("No");
		cancelButton.addActionListener(new PromotionRefusedAction(popup, move));
		popup.add(cancelButton);

		popup.pack();
		popup.setVisible(true);
	}
}

package net.mauhiz.board.impl.shogi.gui;

import java.awt.GridLayout;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JPanel;

import net.mauhiz.board.impl.common.gui.PocketSwingGuiAssistant;
import net.mauhiz.board.impl.common.gui.rotation.RotatingJButton;
import net.mauhiz.board.impl.shogi.data.ShogiPlayerType;
import net.mauhiz.board.model.data.NormalMove;
import net.mauhiz.board.model.data.Piece;
import net.mauhiz.board.model.gui.PocketBoardGui;
import net.mauhiz.util.AbstractAction;
import net.mauhiz.util.ExecutionType;

/**
 * @author mauhiz
 */
public class ShogiSwingAssistant extends PocketSwingGuiAssistant implements IShogiGuiAssistant {

	public ShogiSwingAssistant(PocketBoardGui parent) {
		super(parent);
	}

	@Override
	public void decorate(RotatingJButton button, Piece op) {
		if (op == null) {
			button.setText("", false);
		} else {
			button.setText(op.getPieceType().toString(), op.getPlayerType() == ShogiPlayerType.GOTE);
		}
	}

	@Override
	protected ShogiGui getParent() {
		return (ShogiGui) super.getParent();
	}

	public void initPockets() {
		JPanel pocket = newPocket();
		boardAndPocketsPanel.add(pocket, 0);
		pockets.put(ShogiPlayerType.GOTE, pocket);
		pocket = newPocket();
		boardAndPocketsPanel.add(pocket);
		pockets.put(ShogiPlayerType.SENTE, pocket);
	}

	private JPanel newPocket() {
		JPanel pocket = new JPanel();
		GridLayout layout = new GridLayout(1, 7);
		pocket.setLayout(layout);
		pocket.setSize(7 * 30, 30);
		pocket.setToolTipText("Pocket");
		return pocket;
	}

	public void showPromotionDialog(final NormalMove move) {
		final JDialog popup = new JDialog(frame, "Promotion?");
		popup.setLayout(new GridLayout(1, 2, 10, 10));
		popup.setModal(true);

		JButton promoButton = new JButton("Yes");
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

		JButton cancelButton = new JButton("No");
		cancelButton.addActionListener(new AbstractAction() {

			@Override
			protected void doAction() {
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

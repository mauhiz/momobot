package net.mauhiz.board.impl.shogi.gui;

import java.awt.GridLayout;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JPanel;

import net.mauhiz.board.impl.common.gui.PocketSwingGuiAssistant;
import net.mauhiz.board.impl.common.gui.rotation.RotatingJButton;
import net.mauhiz.board.impl.shogi.data.ShogiPieceType;
import net.mauhiz.board.impl.shogi.data.ShogiPlayerType;
import net.mauhiz.board.model.data.NormalMove;
import net.mauhiz.board.model.data.PieceType;
import net.mauhiz.board.model.data.PlayerType;
import net.mauhiz.board.model.gui.PocketBoardGui;
import net.mauhiz.util.AbstractAction;
import net.mauhiz.util.ExecutionType;

import org.apache.log4j.Logger;

/**
 * @author mauhiz
 */
public class ShogiSwingAssistant extends PocketSwingGuiAssistant implements IShogiGuiAssistant {

	private static final Logger LOG = Logger.getLogger(ShogiSwingAssistant.class);

	public ShogiSwingAssistant(PocketBoardGui parent) {
		super(parent);
	}

	@Override
	public void decorate(RotatingJButton button, PieceType piece, PlayerType player) {
		if (piece == null) {
			button.setText("", false);
		} else {
			String ji = piece.toString();
			if (piece == ShogiPieceType.KING && player == ShogiPlayerType.SENTE) {
				ji = "玉"; // special case!
			}
			button.setText(ji, player == ShogiPlayerType.GOTE);
		}
	}

	@Override
	protected ShogiGui getParent() {
		return (ShogiGui) super.getParent();
	}

	public void initPockets() {
		JPanel pocket = newPocket();
		boardAndPocketsPanel.add(pocket, 0);
		addPocket(ShogiPlayerType.GOTE, pocket);
		LOG.debug("Init top pocket: " + pocket);
		pocket = newPocket();
		boardAndPocketsPanel.add(pocket);
		addPocket(ShogiPlayerType.SENTE, pocket);
		LOG.debug("Init bottom pocket: " + pocket);
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
		LOG.debug("Showing promotion dialog for move: " + move);
		final JDialog popup = new JDialog(frame, "Promotion?");
		popup.setLayout(new GridLayout(1, 2, 10, 10));
		popup.setModal(true);

		JButton promoButton = new JButton();
		promoButton.setText("Yes");
		promoButton.addActionListener(new AbstractAction() {

			@Override
			protected ExecutionType getExecutionType() {
				return ExecutionType.GUI_ASYNCHRONOUS;
			}

			@Override
			public void trun() {
				getParent().afterPromotionDialog(move, true);
				popup.dispose();
			}
		});
		popup.add(promoButton);

		JButton cancelButton = new JButton("No");
		cancelButton.addActionListener(new AbstractAction() {

			@Override
			protected ExecutionType getExecutionType() {
				return ExecutionType.GUI_ASYNCHRONOUS;
			}

			@Override
			public void trun() {
				getParent().afterPromotionDialog(move, false);
				popup.dispose();
			}
		});
		popup.add(cancelButton);

		popup.pack();
		popup.setVisible(true);
	}
}

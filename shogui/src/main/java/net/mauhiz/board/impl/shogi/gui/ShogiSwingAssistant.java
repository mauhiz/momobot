package net.mauhiz.board.impl.shogi.gui;

import java.awt.GridLayout;

import javax.swing.JOptionPane;
import javax.swing.JPanel;

import net.mauhiz.board.impl.common.assistant.swing.PocketSwingGuiAssistant;
import net.mauhiz.board.impl.common.assistant.swing.button.RotatingJButton;
import net.mauhiz.board.impl.shogi.data.ShogiPieceType;
import net.mauhiz.board.impl.shogi.data.ShogiPlayerType;
import net.mauhiz.board.model.data.NormalMove;
import net.mauhiz.board.model.data.PieceType;
import net.mauhiz.board.model.data.PlayerType;
import net.mauhiz.board.model.gui.PocketBoardGui;

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
			String ji;
			if (piece == ShogiPieceType.KING && player == ShogiPlayerType.SENTE) {
				ji = "玉"; // special case!
			} else {
				ji = piece.toString();
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

	protected JPanel newPocket() {
		JPanel pocket = new JPanel();
		GridLayout layout = new GridLayout(1, 7);
		pocket.setLayout(layout);
		pocket.setSize(7 * 30, 30);
		pocket.setToolTipText("Pocket");
		return pocket;
	}

	public void showPromotionDialog(final NormalMove move) {
		LOG.debug("Showing promotion dialog for move: " + move);
		int result = JOptionPane.showConfirmDialog(null, "Promotion?", "Promotion?", JOptionPane.YES_NO_OPTION);
		getParent().afterPromotionDialog(move, result == JOptionPane.YES_OPTION);
	}
}

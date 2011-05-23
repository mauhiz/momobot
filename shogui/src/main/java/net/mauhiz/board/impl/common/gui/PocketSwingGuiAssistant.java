package net.mauhiz.board.impl.common.gui;

import java.awt.Dimension;
import java.util.Collection;
import java.util.Collections;
import java.util.Map;
import java.util.TreeMap;

import javax.swing.BoxLayout;
import javax.swing.JPanel;
import javax.swing.JSplitPane;

import net.mauhiz.board.impl.common.gui.rotation.RotatingJButton;
import net.mauhiz.board.impl.shogi.data.ShogiPlayerType;
import net.mauhiz.board.model.data.Piece;
import net.mauhiz.board.model.data.PieceType;
import net.mauhiz.board.model.data.PlayerType;
import net.mauhiz.board.model.gui.PocketBoardGui;
import net.mauhiz.board.model.gui.PocketGuiAssistant;

import org.apache.log4j.Logger;

public abstract class PocketSwingGuiAssistant extends SwingGuiAssistant implements PocketGuiAssistant {

	private static final Logger LOG = Logger.getLogger(PocketSwingGuiAssistant.class);
	protected final JPanel boardAndPocketsPanel = new JPanel();
	protected final Map<PlayerType, JPanel> pockets = new TreeMap<PlayerType, JPanel>();

	public PocketSwingGuiAssistant(PocketBoardGui parent) {
		super(parent);
	}

	public void addToPocket(Piece piece) {
		PlayerType player = piece.getPlayerType();
		PieceType pieceType = piece.getPieceType();
		RotatingJButton button = new RotatingJButton();
		button.setText(pieceType.toString(), player == ShogiPlayerType.GOTE);
		button.setSize(30, 30);
		pockets.get(player).add(button);
		button.addActionListener(new SelectPocketAction(getParent(), pieceType, player));
		LOG.debug("Adding to pocket: " + piece);
	}

	@Override
	public void clear() {
		super.clear();
		refreshPockets(Collections.<Piece> emptySet());
	}

	@Override
	protected PocketBoardGui getParent() {
		return (PocketBoardGui) super.getParent();
	}

	@Override
	public void initLayout(Dimension size) {
		boardAndPocketsPanel.setLayout(new BoxLayout(boardAndPocketsPanel, BoxLayout.Y_AXIS));
		LOG.debug("Board and pockets layout: " + boardAndPocketsPanel.getLayout());
		super.initLayout(size);
		initPockets();
	}

	@Override
	protected void initPanels() {
		boardAndHistory.setOrientation(JSplitPane.HORIZONTAL_SPLIT);
		boardAndHistory.setSize(frame.getWidth() - 2, frame.getHeight() - 2);
		frame.add(boardAndHistory);
		LOG.debug("Init board and history split panel: " + boardAndHistory);

		boardAndPocketsPanel.setSize(boardAndHistory.getWidth() / 2 - 2, boardAndHistory.getHeight());
		boardAndHistory.add(boardAndPocketsPanel, JSplitPane.LEFT);
		LOG.debug("Init Board and pockets: " + boardAndPocketsPanel);

		boardAndPocketsPanel.setSize(boardAndHistory.getWidth() / 2 - 2, boardAndHistory.getHeight());
		boardAndPocketsPanel.add(historyPanel, JSplitPane.RIGHT);
		LOG.debug("Init History panel: " + historyPanel);

		boardPanel.setSize(boardAndPocketsPanel.getWidth(), boardAndPocketsPanel.getHeight() - 100);
		boardAndPocketsPanel.add(boardPanel);
		LOG.debug("Init Board panel: " + boardPanel);

	}

	public void refreshPockets(Collection<? extends Piece> contents) {
		LOG.trace("Emptying pockets");
		for (JPanel pocket : pockets.values()) {
			pocket.removeAll();
		}
		if (contents != null) {
			LOG.debug("Refreshing pockets with contents: " + contents);
			for (Piece piece : contents) {
				addToPocket(piece);
			}
		}
	}
}

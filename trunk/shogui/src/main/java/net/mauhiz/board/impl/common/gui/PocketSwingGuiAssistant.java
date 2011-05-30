package net.mauhiz.board.impl.common.gui;

import java.awt.Component;
import java.awt.Dimension;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.SortedMap;
import java.util.TreeMap;

import javax.swing.BoxLayout;
import javax.swing.JPanel;
import javax.swing.JSplitPane;

import net.mauhiz.board.impl.common.gui.rotation.RotatingJButton;
import net.mauhiz.board.model.data.Piece;
import net.mauhiz.board.model.data.PieceType;
import net.mauhiz.board.model.data.PlayerType;
import net.mauhiz.board.model.gui.PocketBoardGui;
import net.mauhiz.board.model.gui.PocketGuiAssistant;

import org.apache.log4j.Logger;

public abstract class PocketSwingGuiAssistant extends SwingGuiAssistant implements PocketGuiAssistant {

	private static final Logger LOG = Logger.getLogger(PocketSwingGuiAssistant.class);
	protected final JPanel boardAndPocketsPanel = new JPanel();
	private final Map<RotatingJButton, Piece> pocketButtons = new HashMap<RotatingJButton, Piece>();
	private final SortedMap<PlayerType, JPanel> pockets = new TreeMap<PlayerType, JPanel>();

	public PocketSwingGuiAssistant(PocketBoardGui parent) {
		super(parent);
	}

	protected JPanel addPocket(PlayerType playerType, JPanel pocket) {
		synchronized (pockets) {
			return pockets.put(playerType, pocket);
		}
	}

	public void addToPocket(PieceType pieceType, PlayerType player) {
		RotatingJButton button = new RotatingJButton();
		button.setBackground(getParent().getSquareBgcolor(null));
		button.setSize(30, 30);
		decorate(button, pieceType, player);
		button.addActionListener(new SelectPocketAction(getParent(), pieceType, player));
		getPocket(player).add(button);
		LOG.debug("Adding to pocket: " + pieceType);
		refreshPocketActions(player);
	}

	@Override
	public void clear() {
		super.clear();
		clearPockets();
	}

	public void clearPockets() {
		LOG.trace("Emptying pockets");
		synchronized (pockets) {
			for (JPanel pocket : pockets.values()) {
				pocket.removeAll();
			}
		}
		synchronized (pocketButtons) {
			pocketButtons.clear();
		}
	}

	@Override
	protected PocketBoardGui getParent() {
		return (PocketBoardGui) super.getParent();
	}

	protected JPanel getPocket(PlayerType playerType) {
		synchronized (pockets) {
			return pockets.get(playerType);
		}
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

	public void refreshPocketActions(PlayerType player) {
		synchronized (pockets) {
			for (Entry<PlayerType, JPanel> pocket : pockets.entrySet()) {
				for (Component comp : pocket.getValue().getComponents()) {
					if (comp instanceof RotatingJButton) {
						comp.setEnabled(player == pocket.getKey());
					}
				}
			}
		}
	}

	public void removeFromPocket(PieceType piece, PlayerType player) {
		LOG.debug("Removing from " + player + " pocket: " + piece);
		synchronized (pocketButtons) {
			for (Iterator<Entry<RotatingJButton, Piece>> i = pocketButtons.entrySet().iterator(); i.hasNext();) {
				Entry<RotatingJButton, Piece> candidate = i.next();
				Piece inPocket = candidate.getValue();
				if (inPocket.getPieceType() == piece && inPocket.getPlayerType() == player) {
					RotatingJButton button = candidate.getKey();
					i.remove();
					getPocket(player).remove(button);
					button.removeActionListener(null); // clear listeners
					break;
				}
			}
		}
	}
}

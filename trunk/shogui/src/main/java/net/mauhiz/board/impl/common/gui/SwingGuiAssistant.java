package net.mauhiz.board.impl.common.gui;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.ActionListener;
import java.util.HashMap;
import java.util.Map;

import javax.swing.JFrame;
import javax.swing.JList;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JSplitPane;

import net.mauhiz.board.impl.common.data.SquareImpl;
import net.mauhiz.board.impl.common.gui.rotation.RotatingJButton;
import net.mauhiz.board.model.data.Piece;
import net.mauhiz.board.model.data.Square;
import net.mauhiz.board.model.gui.BoardGui;
import net.mauhiz.board.remote.NewRemoteGameAction;
import net.mauhiz.util.IAction;

import org.apache.commons.lang.ObjectUtils;
import org.apache.log4j.Logger;

public abstract class SwingGuiAssistant extends AbstractGuiAssistant {
	private static final Logger LOG = Logger.getLogger(SwingGuiAssistant.class);
	protected final JSplitPane boardAndHistory = new JSplitPane();
	protected final JPanel boardPanel = new JPanel();
	private final Map<Square, RotatingJButton> buttons = new HashMap<Square, RotatingJButton>();
	protected final JFrame frame = new JFrame();
	protected final JList historyPanel = new JList();

	public SwingGuiAssistant(BoardGui parent) {
		super(parent);
	}

	public void appendSquare(Square square, Dimension size) {
		Square cartesianSquare = SquareImpl.getInstance(square.getX(), size.height - square.getY() - 1);
		RotatingJButton button = new RotatingJButton();
		buttons.put(cartesianSquare, button);
		button.setBackground(getParent().getSquareBgcolor(square));
		button.setSize(30, 30);
		boardPanel.add(button);
		LOG.trace("Square appended: " + square + " at position: " + cartesianSquare);
	}

	public void clear() {
		if (boardPanel.getComponentCount() > 0) {
			boardPanel.removeAll();
			LOG.debug("Board panel cleared");
		}
		if (historyPanel.getComponentCount() > 0) {
			historyPanel.removeAll();
			LOG.debug("History panel cleared");
		}
		if (!buttons.isEmpty()) {
			buttons.clear();
			LOG.debug("Buttons cleared");
		}
		if (!listeners.isEmpty()) {
			listeners.clear();
			LOG.debug("Listeners cleared");
		}
	}

	public void close() {
		frame.dispose();
		LOG.info("Display disposed");
	}

	protected abstract void decorate(RotatingJButton button, Piece piece);

	public void decorate(Square square, Piece piece) {
		RotatingJButton button = getButton(square);
		decorate(button, piece);
		if (piece != null) {
			LOG.trace("Square: " + square + " decorated with piece: " + piece);
		}
	}

	public void disableSquare(Square square) {
		RotatingJButton button = getButton(square);
		Color fore = button.getForeground();
		Color back = button.getBackground();
		ActionListener action = listeners.remove(square);
		if (action != null) {
			button.removeActionListener(action);
			LOG.debug("Square disabled: " + square + " (former action: " + action + ")");
		}
		button.setEnabled(false);
		button.setForeground(fore);
		button.setBackground(back);
	}

	public void enableSquare(Square square, IAction action) {
		RotatingJButton button = getButton(square);
		Color fore = button.getForeground();
		Color back = button.getBackground();
		button.addActionListener(action);
		IAction former = listeners.put(square, action);
		if (former == null) {
			LOG.debug("Square: " + square + ", enabled with action: " + action);
		} else if (!ObjectUtils.equals(former, action)) {
			LOG.debug("Square: " + square + ", action: " + former + " with action: " + action);
		}
		button.setEnabled(true);
		button.setForeground(fore);
		button.setBackground(back);
	}

	public RotatingJButton getButton(Square at) {
		return buttons.get(at);
	}

	public void initDisplay() {
		initMenu();
		frame.setTitle(getParent().getWindowTitle());

		Dimension defaultSize = getParent().getDefaultSize();
		frame.setSize(defaultSize);

		Dimension minSize = getParent().getMinimumSize();
		frame.setMinimumSize(minSize);

		initPanels();

		frame.setVisible(true);
		LOG.info("Display initialized");
	}

	public void initLayout(Dimension size) {
		GridLayout gridLayout = new GridLayout(size.width, size.height, 0, 0);
		boardPanel.setLayout(gridLayout);
		LOG.debug("Board layout initialized: " + boardPanel.getLayout());
	}

	protected void initMenu() {

		/* menu */
		JMenuBar menuBar = new JMenuBar();
		JMenu fileMenu = new JMenu("File");

		JMenuItem fileStartItem = new JMenuItem("New Game");
		fileMenu.add(fileStartItem);
		fileStartItem.addActionListener(new StartAction(getParent()));

		JMenuItem fileRemoteItem = new JMenuItem("New &Network Game");
		fileMenu.add(fileRemoteItem);
		fileRemoteItem.addActionListener(new NewRemoteGameAction(getParent()));

		JMenuItem fileExitItem = new JMenuItem("Exit");
		fileMenu.add(fileExitItem);
		fileExitItem.addActionListener(new ExitAction(getParent()));

		menuBar.add(fileMenu);
		frame.setJMenuBar(menuBar);
		LOG.debug("Menu ready");
	}

	protected void initPanels() {
		boardAndHistory.setOrientation(JSplitPane.HORIZONTAL_SPLIT);
		boardAndHistory.setSize(frame.getWidth() - 2, frame.getHeight() - 2);
		frame.add(boardAndHistory);
		LOG.debug("Init board and history split panel: " + boardAndHistory);

		boardPanel.setSize(boardAndHistory.getWidth() / 2 - 2, boardAndHistory.getHeight());
		boardAndHistory.add(boardPanel, JSplitPane.LEFT);
		LOG.debug("Init board panel: " + boardPanel);

		historyPanel.setSize(boardAndHistory.getWidth() / 2 - 2, boardAndHistory.getHeight());
		boardAndHistory.add(historyPanel, JSplitPane.RIGHT);
		LOG.debug("Init history panel: " + historyPanel);

	}

	public void refresh() {
		boardAndHistory.repaint();
		LOG.trace("Repainted: " + boardAndHistory);
	}

	public void start() {
		initDisplay();
	}
}

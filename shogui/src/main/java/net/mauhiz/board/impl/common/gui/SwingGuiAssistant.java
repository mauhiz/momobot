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
import net.mauhiz.board.model.data.PieceType;
import net.mauhiz.board.model.data.PlayerType;
import net.mauhiz.board.model.data.Square;
import net.mauhiz.board.model.gui.BoardGui;
import net.mauhiz.board.remote.NewRemoteGameAction;
import net.mauhiz.util.AbstractNamedRunnable;
import net.mauhiz.util.ExecutionType;
import net.mauhiz.util.IAction;
import net.mauhiz.util.PerformanceMonitor;
import net.mauhiz.util.ThreadUtils;

import org.apache.commons.lang.ObjectUtils;
import org.apache.log4j.Logger;

public abstract class SwingGuiAssistant extends AbstractGuiAssistant {
	final class ButtonInitializer extends AbstractNamedRunnable {
		private final Color bgColor;
		private final RotatingJButton button;

		ButtonInitializer(Color bgColor, RotatingJButton button) {
			super("Button initializer");
			this.bgColor = bgColor;
			this.button = button;
		}

		@Override
		protected ExecutionType getExecutionType() {
			return ExecutionType.GUI_SYNCHRONOUS;
		}

		@Override
		protected void trun() {
			button.setBackground(bgColor);
			button.setSize(30, 30);
			button.setEnabled(false);
			boardPanel.add(button);
		}
	}

	private static final Logger LOG = Logger.getLogger(SwingGuiAssistant.class);
	protected final JSplitPane boardAndHistory = new JSplitPane();
	protected final JPanel boardPanel = new JPanel();
	private final Map<Square, RotatingJButton> buttons = new HashMap<Square, RotatingJButton>();
	protected final JFrame frame = new JFrame();
	protected final JList<String> historyPanel = new JList<String>();

	public SwingGuiAssistant(BoardGui parent) {
		super(parent);
	}

	public void appendSquares(Iterable<Square> squares, Dimension size) {
		boardPanel.setVisible(false);
		for (Square square : squares) {
			Square cartesianSquare = SquareImpl.getInstance(square.getX(), size.height - square.getY() - 1);
			RotatingJButton button = new RotatingJButton();
			new ButtonInitializer(getParent().getSquareBgcolor(square), button).launch(null);
			synchronized (buttons) {
				buttons.put(cartesianSquare, button);
			}
		}
		boardPanel.setVisible(true);
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
		synchronized (buttons) {
			buttons.clear();
		}

		clearListeners();
	}

	public void close() {
		frame.dispose();
		LOG.info("Display disposed");
	}

	protected abstract void decorate(RotatingJButton button, PieceType piece, PlayerType player);

	public void decorate(Square square, Piece piece) {
		RotatingJButton button = getButton(square);

		if (piece == null) {
			decorate(button, null, null);
		} else {
			decorate(button, piece.getPieceType(), piece.getPlayerType());
		}
	}

	public void disableSquare(Square square) {
		ActionListener action = removeListener(square);
		if (action != null) {
			RotatingJButton button = getButton(square);
			button.removeActionListener(action);
		}
	}

	public void enableSquare(Square square, IAction action) {
		IAction former = putListener(square, action);
		if (ObjectUtils.equals(former, action)) {
			return;
		}

		RotatingJButton button = getButton(square);

		// the order is important to not trigger a disable/enable flip
		button.addActionListener(action);

		if (former != null) {
			button.removeActionListener(former);
		}
	}

	public RotatingJButton getButton(Square at) {
		// This loop waits until the buttons have actually been added.
		while (true) {
			RotatingJButton button;
			synchronized (buttons) {
				button = buttons.get(at);
			}
			if (button == null) {
				// not ready yet
				ThreadUtils.safeSleep(100);
			} else {
				return button;
			}
		}
	}

	public void initDisplay() {
		initMenu();
		frame.setTitle(getParent().getWindowTitle());

		Dimension defaultSize = getParent().getDefaultSize();
		frame.setSize(defaultSize);

		Dimension minSize = getParent().getMinimumSize();
		frame.setMinimumSize(minSize);

		PerformanceMonitor sw = new PerformanceMonitor();
		initPanels();
		sw.perfLog("Panels initialized");

		sw.start();
		frame.setVisible(true);
		sw.perfLog("Frame set to visible");
	}

	public void initLayout(Dimension size) {
		PerformanceMonitor sw = new PerformanceMonitor();
		GridLayout gridLayout = new GridLayout(size.width, size.height, 0, 0);
		boardPanel.setLayout(gridLayout);
		sw.perfLog("Board layout initialized: " + boardPanel.getLayout());
	}

	protected void initMenu() {
		PerformanceMonitor sw = new PerformanceMonitor();
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
		sw.perfLog("Menu ready");
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

	public void refreshBoard() {
		PerformanceMonitor sw = new PerformanceMonitor();
		boardAndHistory.validate();
		sw.perfLog("Repainted: " + boardAndHistory);
	}

	public void start() {
		initDisplay();
	}
}

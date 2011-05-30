package net.mauhiz.board.impl.common.gui;

import java.awt.Button;
import java.awt.Dimension;
import java.awt.Frame;
import java.awt.GridLayout;
import java.awt.Menu;
import java.awt.MenuBar;
import java.awt.MenuItem;
import java.awt.MenuShortcut;
import java.awt.Panel;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.util.HashMap;
import java.util.Map;

import net.mauhiz.board.impl.common.data.SquareImpl;
import net.mauhiz.board.model.data.Piece;
import net.mauhiz.board.model.data.PieceType;
import net.mauhiz.board.model.data.PlayerType;
import net.mauhiz.board.model.data.Square;
import net.mauhiz.board.model.gui.BoardGui;
import net.mauhiz.util.IAction;

import org.apache.commons.lang.ObjectUtils;
import org.apache.log4j.Logger;

public abstract class AwtGuiAssistant extends AbstractGuiAssistant {
	private static final Logger LOG = Logger.getLogger(AwtGuiAssistant.class);
	private final Map<Square, Button> buttons = new HashMap<Square, Button>();

	protected Frame frame = new Frame();
	protected Panel panel;

	public AwtGuiAssistant(BoardGui parent) {
		super(parent);
	}

	public void afterInit() {
		frame.pack();
	}

	public void appendSquares(Iterable<Square> squares, Dimension size) {
		panel.setVisible(false);
		for (Square square : squares) {
			int x = square.getX();
			int y = size.height - square.getY() - 1;
			Button button = new Button();
			buttons.put(SquareImpl.getInstance(x, y), button);
			button.setBackground(getParent().getSquareBgcolor(square));
			button.setSize(30, 30);
			panel.add(button);
		}
		panel.setVisible(true);
	}

	public void clear() {
		for (Button button : buttons.values()) {
			panel.remove(button);
		}
		buttons.clear();
		clearListeners();
	}

	public void close() {
		frame.dispose();
	}

	protected abstract void decorate(Button button, PieceType piece, PlayerType player);

	public void decorate(Square square, Piece piece) {
		Button button = getButton(square);
		if (piece == null) {
			decorate(button, null, null);
		} else {
			decorate(button, piece.getPieceType(), piece.getPlayerType());
		}
	}

	public void disableSquare(Square square) {
		Button button = getButton(square);
		ActionListener action = removeListener(square);
		if (action != null) {
			button.removeActionListener(action);
			button.setEnabled(false);
		}
	}

	public void enableSquare(Square square, IAction action) {
		IAction former = putListener(square, action);
		if (ObjectUtils.equals(former, action)) {
			return;
		}

		Button button = getButton(square);
		if (former == null) {
			LOG.debug("Square: " + square + ", enabled with action: " + action);
			button.addActionListener(action);
			button.setEnabled(true);
		} else {
			LOG.debug("Square: " + square + ", action: " + former + " with action: " + action);
			button.removeActionListener(former);
			button.addActionListener(action);
		}
	}

	public Button getButton(Square at) {
		return buttons.get(at);
	}

	public void initDisplay() {
		initMenu();
		frame.setTitle(getParent().getWindowTitle());

		Dimension defaultSize = getParent().getDefaultSize();
		frame.setSize(defaultSize);

		Dimension minSize = getParent().getMinimumSize();
		frame.setMinimumSize(minSize);

		panel = new Panel();
		frame.add(panel);
		frame.setVisible(true);
	}

	public void initLayout(Dimension size) {

		/* layout */
		GridLayout gridLayout = new GridLayout(size.width, size.height, 0, 0);
		panel.setLayout(gridLayout);
	}

	protected void initMenu() {

		/* menu */
		MenuBar menuBar = new MenuBar();
		Menu fileMenu = new Menu("File");

		MenuItem fileStartItem = new MenuItem("New Game", new MenuShortcut(KeyEvent.VK_G));
		fileMenu.add(fileStartItem);
		fileStartItem.addActionListener(new StartAction(getParent()));

		MenuItem fileExitItem = new MenuItem("Exit", new MenuShortcut(KeyEvent.VK_X));
		fileMenu.add(fileExitItem);
		fileExitItem.addActionListener(new ExitAction(getParent()));

		menuBar.add(fileMenu);
		frame.setMenuBar(menuBar);
	}

	public void refreshBoard() {
		panel.validate();
	}

	public void start() {
		initDisplay();
	}
}

package net.mauhiz.board.impl.common.gui;

import java.awt.Button;
import java.awt.Color;
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
import net.mauhiz.board.model.data.Square;
import net.mauhiz.board.model.gui.BoardGui;
import net.mauhiz.util.IAction;

public abstract class AwtGuiAssistant extends AbstractGuiAssistant {

	public AwtGuiAssistant(BoardGui parent) {
		super(parent);
	}

	private final Map<Square, Button> buttons = new HashMap<Square, Button>();
	protected Frame frame = new Frame();
	protected Panel panel;

	public void afterInit() {
		frame.pack();
	}

	@Override
	public void decorate(Square square, Piece piece) {
		Button button = getButton(square);
		decorate(button, piece);
	}

	protected abstract void decorate(Button button, Piece piece);

	public void appendSquare(Square square, Dimension size) {
		int x = square.getX();
		int y = size.height - square.getY() - 1;
		Button button = new Button();
		buttons.put(SquareImpl.getInstance(x, y), button);
		button.setBackground(parent.getSquareBgcolor(square));
		button.setSize(30, 30);
		panel.add(button);
	}

	public void clear() {
		for (Button button : buttons.values()) {
			panel.remove(button);
		}
		buttons.clear();
		listeners.clear();
	}

	@Override
	public void close() {
		frame.dispose();
	}

	@Override
	public void disableSquare(Square square) {
		Button button = getButton(square);
		Color fore = button.getForeground();
		Color back = button.getBackground();
		ActionListener action = listeners.remove(square);
		if (action != null) {
			button.removeActionListener(action);
		}
		button.setEnabled(false);
		button.setForeground(fore);
		button.setBackground(back);
	}

	@Override
	public void enableSquare(Square square, IAction action) {
		Button button = getButton(square);
		Color fore = button.getForeground();
		Color back = button.getBackground();
		button.addActionListener(action);
		listeners.put(square, action);
		button.setEnabled(true);
		button.setForeground(fore);
		button.setBackground(back);
	}

	public Button getButton(Square at) {
		return buttons.get(at);
	}

	public void initDisplay() {
		initMenu();
		frame.setTitle(parent.getWindowTitle());

		Dimension defaultSize = parent.getDefaultSize();
		frame.setSize(defaultSize);

		Dimension minSize = parent.getMinimumSize();
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
		fileStartItem.addActionListener(new StartAction(parent));

		MenuItem fileExitItem = new MenuItem("Exit", new MenuShortcut(KeyEvent.VK_X));
		fileMenu.add(fileExitItem);
		fileExitItem.addActionListener(new ExitAction(parent));

		menuBar.add(fileMenu);
		frame.setMenuBar(menuBar);
	}

	@Override
	public void refresh() {
		frame.repaint();
	}
	
	@Override
	public void start() {
		initDisplay();
	}
}

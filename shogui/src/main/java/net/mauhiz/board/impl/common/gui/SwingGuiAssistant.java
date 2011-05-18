package net.mauhiz.board.impl.common.gui;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.ActionListener;
import java.util.HashMap;
import java.util.Map;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;

import net.mauhiz.board.impl.common.data.SquareImpl;
import net.mauhiz.board.impl.common.gui.rotation.RotatingJButton;
import net.mauhiz.board.model.data.Piece;
import net.mauhiz.board.model.data.Square;
import net.mauhiz.board.model.gui.BoardGui;
import net.mauhiz.board.remote.NewRemoteGameAction;
import net.mauhiz.util.IAction;

public abstract class SwingGuiAssistant extends AbstractGuiAssistant {

	protected JPanel boardPanel;
	private final Map<Square, RotatingJButton> buttons = new HashMap<Square, RotatingJButton>();
	protected final JFrame frame = new JFrame();
	protected JPanel historyPanel;

	public SwingGuiAssistant(BoardGui parent) {
		super(parent);
	}

	public void appendSquare(Square square, Dimension size) {
		int x = square.getX();
		int y = size.height - square.getY() - 1;
		RotatingJButton button = new RotatingJButton();
		buttons.put(SquareImpl.getInstance(x, y), button);
		button.setBackground(getParent().getSquareBgcolor(square));
		button.setSize(40, 40);
		boardPanel.add(button);
	}

	public void clear() {
		for (JButton button : buttons.values()) {
			boardPanel.remove(button);
		}
		buttons.clear();
		listeners.clear();
	}

	@Override
	public void close() {
		frame.dispose();
	}

	protected abstract void decorate(RotatingJButton button, Piece piece);

	@Override
	public void decorate(Square square, Piece piece) {
		RotatingJButton button = getButton(square);
		decorate(button, piece);
	}

	@Override
	public void disableSquare(Square square) {
		RotatingJButton button = getButton(square);
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
		RotatingJButton button = getButton(square);
		Color fore = button.getForeground();
		Color back = button.getBackground();
		button.addActionListener(action);
		listeners.put(square, action);
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
	}

	public void initLayout(Dimension size) {

		/* layout */
		GridLayout gridLayout = new GridLayout(size.width, size.height, 0, 0);
		boardPanel.setLayout(gridLayout);
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
	}

	protected void initPanels() {
		boardPanel = new JPanel();
		frame.add(boardPanel);

		historyPanel = new JPanel();
		frame.add(historyPanel);
	}

	@Override
	public void refresh() {
		boardPanel.repaint();
	}

	@Override
	public void start() {
		initDisplay();
	}
}

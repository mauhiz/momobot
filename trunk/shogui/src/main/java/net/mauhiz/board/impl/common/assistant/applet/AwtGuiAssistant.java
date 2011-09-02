package net.mauhiz.board.impl.common.assistant.applet;

import java.awt.Container;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.ActionListener;
import java.util.HashMap;
import java.util.Map;

import javax.swing.JApplet;
import javax.swing.JButton;
import javax.swing.JPanel;

import net.mauhiz.board.impl.common.action.ExitAction;
import net.mauhiz.board.impl.common.action.StartAction;
import net.mauhiz.board.impl.common.assistant.AbstractGuiAssistant;
import net.mauhiz.board.impl.common.assistant.swing.button.RotatingJButton;
import net.mauhiz.board.impl.common.data.SquareImpl;
import net.mauhiz.board.model.data.Piece;
import net.mauhiz.board.model.data.PieceType;
import net.mauhiz.board.model.data.PlayerType;
import net.mauhiz.board.model.data.Square;
import net.mauhiz.board.model.gui.BoardGui;
import net.mauhiz.util.IAction;

import org.apache.commons.lang.NotImplementedException;
import org.apache.commons.lang.ObjectUtils;
import org.apache.log4j.Logger;

public abstract class AwtGuiAssistant extends AbstractGuiAssistant {
	private static final Logger LOG = Logger.getLogger(AwtGuiAssistant.class);
	private final Map<Square, RotatingJButton> buttons = new HashMap<>();
	protected final JApplet frame = new JApplet();
	protected final JPanel panel = new JPanel();

	public AwtGuiAssistant(BoardGui parent) {
		super(parent);
	}

	@Override
	public void addToHistory(String value, IAction action) {
		throw new NotImplementedException();
	}

	public void appendSquares(Iterable<Square> squares, Dimension size) {
		panel.setVisible(false);
		for (Square square : squares) {
			int x = square.getX();
			int y = size.height - square.getY() - 1;
			RotatingJButton button = new RotatingJButton();
			buttons.put(SquareImpl.getInstance(x, y), button);
			button.setBackground(getParent().getSquareBgcolor(square));
			button.setSize(30, 30);
			panel.add(button);
		}
		panel.setVisible(true);
	}

	public void clear() {
		for (RotatingJButton button : buttons.values()) {
			panel.remove(button);
		}
		buttons.clear();
		clearListeners();
	}

	public void close() {
		frame.destroy();
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
			button.setEnabled(false);
		}
	}

	public void enableSquare(Square square, IAction action) {
		IAction former = putListener(square, action);
		if (ObjectUtils.equals(former, action)) {
			return;
		}

		RotatingJButton button = getButton(square);
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

	public RotatingJButton getButton(Square at) {
		return buttons.get(at);
	}

	public void initDisplay() {
		initMenu();
		frame.setName(getParent().getWindowTitle());

		Dimension defaultSize = getParent().getDefaultSize();
		frame.setSize(defaultSize);

		Dimension minSize = getParent().getMinimumSize();
		frame.setMinimumSize(minSize);

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
		Container menuBar = new Container();
		menuBar.setLayout(new GridLayout(1, 2));

		JButton fileStartItem = new JButton("New Game");
		fileStartItem.addActionListener(new StartAction(getParent()));
		menuBar.add(fileStartItem);

		JButton fileExitItem = new JButton("Exit");
		fileExitItem.addActionListener(new ExitAction(getParent()));
		menuBar.add(fileExitItem);
		frame.add(menuBar);
	}

	public void refreshBoard() {
		panel.validate();
	}

	public void start() {
		initDisplay();
	}
}

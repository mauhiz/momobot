package net.mauhiz.board.impl.common.assistant.jface;

import java.awt.Dimension;
import java.util.HashMap;
import java.util.Map;

import net.mauhiz.board.impl.common.action.ExitAction;
import net.mauhiz.board.impl.common.action.StartAction;
import net.mauhiz.board.impl.common.assistant.AbstractGuiAssistant;
import net.mauhiz.board.impl.common.data.SquareImpl;
import net.mauhiz.board.model.data.Piece;
import net.mauhiz.board.model.data.PieceType;
import net.mauhiz.board.model.data.PlayerType;
import net.mauhiz.board.model.data.Square;
import net.mauhiz.board.model.gui.BoardGui;
import net.mauhiz.util.AbstractAction;
import net.mauhiz.util.ExecutionType;
import net.mauhiz.util.IAction;
import net.mauhiz.util.ThreadUtils;

import org.apache.commons.lang.NotImplementedException;
import org.apache.commons.lang.ObjectUtils;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.RGB;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.MenuItem;
import org.eclipse.swt.widgets.Shell;

public abstract class SwtGuiAssistant extends AbstractGuiAssistant {
	static class ButtonClearer extends AbstractAction {
		private final Map<Square, Button> lButtons;

		ButtonClearer(Map<Square, Button> lButtons) {
			this.lButtons = lButtons;
		}

		@Override
		public ExecutionType getExecutionType() {
			return ExecutionType.GUI_ASYNCHRONOUS;
		}

		@Override
		protected void trun() {
			synchronized (lButtons) {
				for (Button button : lButtons.values()) {
					button.dispose();
				}

				lButtons.clear();
			}
		}
	}

	class LayoutInitializer extends AbstractAction {
		private final Dimension size;

		LayoutInitializer(Dimension size) {
			this.size = size;
		}

		@Override
		public ExecutionType getExecutionType() {
			return ExecutionType.GUI_ASYNCHRONOUS;
		}

		@Override
		public void trun() {
			GridLayout gridLayout = new GridLayout(size.width, true);
			gridLayout.horizontalSpacing = 0;
			gridLayout.verticalSpacing = 0;
			getShell().setLayout(gridLayout);
			getShell().pack();
		}
	}

	final class ShellRedrawer extends AbstractAction {

		@Override
		public ExecutionType getExecutionType() {
			return ExecutionType.GUI_ASYNCHRONOUS;
		}

		@Override
		public void trun() {
			getShell().redraw();
		}
	}

	class SquareAppender extends AbstractAction {
		private final java.awt.Color color;
		private final Map<Square, Button> lButtons;
		private final int x;
		private final int y;

		SquareAppender(Map<Square, Button> lButtons, java.awt.Color color, int x, int y) {
			this.lButtons = lButtons;
			this.color = color;
			this.x = x;
			this.y = y;
		}

		@Override
		public ExecutionType getExecutionType() {
			return ExecutionType.GUI_ASYNCHRONOUS;
		}

		@Override
		public void trun() {
			Button button = new Button(getShell(), SWT.PUSH | SWT.FLAT);
			button.setSize(30, 30);
			button.setBackground(fromAwt(color));
			synchronized (lButtons) {
				lButtons.put(SquareImpl.getInstance(x, y), button);
			}
		}
	}

	static class SquareDisabler extends AbstractAction {
		private final IAction action;
		private final Button button;

		public SquareDisabler(Button button, IAction action) {
			this.button = button;
			this.action = action;
		}

		@Override
		public ExecutionType getExecutionType() {
			return ExecutionType.GUI_ASYNCHRONOUS;
		}

		@Override
		public void trun() {
			button.removeSelectionListener(action);
			button.setEnabled(false);

		}
	}

	static class SquareEnabler extends AbstractAction {
		private final IAction action;
		private final Button button;
		private final IAction old;

		SquareEnabler(IAction action, Button button, IAction old) {
			this.action = action;
			this.button = button;
			this.old = old;
		}

		@Override
		public ExecutionType getExecutionType() {
			return ExecutionType.GUI_SYNCHRONOUS;
		}

		@Override
		public void trun() {
			button.addSelectionListener(action);
			if (old == null) {
				button.setEnabled(true);
			} else {
				button.removeSelectionListener(old);
			}
		}
	}

	private final Map<Square, Button> buttons = new HashMap<Square, Button>();

	private Shell shell = new Shell(Display.getDefault());

	public SwtGuiAssistant(BoardGui parent) {
		super(parent);
	}

	@Override
	public void addToHistory(String value, IAction action) {
		throw new NotImplementedException();
	}

	public void appendSquares(Iterable<Square> squares, Dimension size) {
		for (Square square : squares) {
			int x = square.getX();
			int y = size.height - square.getY() - 1;
			new SquareAppender(buttons, getParent().getSquareBgcolor(square), x, y).launch(shell.getDisplay());
		}
	}

	public void clear() {
		new ButtonClearer(buttons).launch(shell.getDisplay());

		clearListeners();
	}

	public void close() {
		shell.close();
	}

	protected abstract void decorate(Button button, PieceType piece, PlayerType player);

	public void decorate(Square square, Piece piece) {
		if (piece != null) {
			Button button = getButton(square);
			decorate(button, piece.getPieceType(), piece.getPlayerType());
		}
	}

	public void disableSquare(final Square square) {
		final IAction action = removeListener(square);
		if (action != null) {
			final Button button = getButton(square);
			new SquareDisabler(button, action).launch(button.getDisplay());
		}
	}

	public void enableSquare(final Square square, final IAction action) {
		final IAction old = putListener(square, action);

		if (!ObjectUtils.equals(action, old)) {
			final Button button = getButton(square);
			new SquareEnabler(action, button, old).launch(button.getDisplay());
		}
	}

	protected Color fromAwt(java.awt.Color awtColor) {
		RGB rgb = new RGB(awtColor.getRed(), awtColor.getGreen(), awtColor.getBlue());
		return new Color(shell.getDisplay(), rgb);
	}

	public Button getButton(Square at) {
		while (true) {
			Button button;
			synchronized (buttons) {
				button = buttons.get(at);
			}
			if (button == null) {
				ThreadUtils.safeSleep(100);
			} else {
				return button;
			}
		}
	}

	public Shell getShell() {
		return shell;
	}

	public void initDisplay() {
		initMenu();
		shell.setText(getParent().getWindowTitle());

		Dimension defaultSize = getParent().getDefaultSize();
		shell.setSize(defaultSize.width, defaultSize.height);

		Dimension minSize = getParent().getMinimumSize();
		shell.setMinimumSize(minSize.width, minSize.height);
	}

	public void initLayout(Dimension size) {
		new LayoutInitializer(size).launch(shell.getDisplay());
	}

	protected void initMenu() {
		Menu menuBar = new Menu(shell, SWT.BAR);
		MenuItem fileMenuHeader = new MenuItem(menuBar, SWT.CASCADE);
		fileMenuHeader.setText("&File");

		Menu fileMenu = new Menu(shell, SWT.DROP_DOWN);
		fileMenuHeader.setMenu(fileMenu);

		MenuItem fileStartItem = new MenuItem(fileMenu, SWT.PUSH);
		fileStartItem.setText("New Local &Game");
		fileStartItem.addSelectionListener(new StartAction(getParent()));

		MenuItem fileExitItem = new MenuItem(fileMenu, SWT.PUSH);
		fileExitItem.setText("E&xit");
		fileExitItem.addSelectionListener(new ExitAction(getParent()));
		shell.setMenuBar(menuBar);
	}

	public void refreshBoard() {
		new ShellRedrawer().launch(shell.getDisplay());
	}

	public void start() {
		initDisplay();
		// SWT loop
		shell.open();
		Display display = shell.getDisplay();

		while (!shell.isDisposed()) {
			if (!display.readAndDispatch()) {
				display.sleep();
			}
		}
		display.dispose();
	}
}

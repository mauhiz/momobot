package net.mauhiz.board.impl.common.gui;

import java.awt.Dimension;
import java.util.HashMap;
import java.util.Map;

import net.mauhiz.board.impl.common.data.SquareImpl;
import net.mauhiz.board.model.data.Piece;
import net.mauhiz.board.model.data.PieceType;
import net.mauhiz.board.model.data.PlayerType;
import net.mauhiz.board.model.data.Square;
import net.mauhiz.board.model.gui.BoardGui;
import net.mauhiz.util.ExecutionType;
import net.mauhiz.util.IAction;
import net.mauhiz.util.NamedRunnable;
import net.mauhiz.util.ThreadUtils;

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

	static class ButtonClearer extends NamedRunnable {
		private final Map<Square, Button> lButtons;

		ButtonClearer(Map<Square, Button> lButtons) {
			super("Button Clearer");
			this.lButtons = lButtons;
		}

		@Override
		protected ExecutionType getExecutionType() {
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

	class LayoutInitializer extends NamedRunnable {
		private final Dimension size;

		LayoutInitializer(Dimension size) {
			super("Init Layout");
			this.size = size;
		}

		@Override
		protected ExecutionType getExecutionType() {
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

	class SquareAppender extends NamedRunnable {
		private final Map<Square, Button> lButtons;
		private final Square square;
		private final int x;
		private final int y;

		SquareAppender(Map<Square, Button> lButtons, Square square, int x, int y) {
			super("Square Appender");
			this.lButtons = lButtons;
			this.square = square;
			this.x = x;
			this.y = y;
		}

		@Override
		protected ExecutionType getExecutionType() {
			return ExecutionType.GUI_ASYNCHRONOUS;
		}

		@Override
		public void trun() {
			Button button = new Button(getShell(), SWT.PUSH | SWT.FLAT);
			button.setSize(30, 30);
			button.setBackground(fromAwt(getParent().getSquareBgcolor(square)));
			synchronized (lButtons) {
				lButtons.put(SquareImpl.getInstance(x, y), button);
			}
		}
	}

	static class SquareDisabler extends NamedRunnable {
		private final IAction action;
		private final Button button;

		public SquareDisabler(Button button, IAction action) {
			super("Square Disabler");
			this.button = button;
			this.action = action;
		}

		@Override
		protected ExecutionType getExecutionType() {
			return ExecutionType.GUI_ASYNCHRONOUS;
		}

		@Override
		public void trun() {
			button.removeSelectionListener(action);
			button.setEnabled(false);

		}
	}

	static class SquareEnabler extends NamedRunnable {
		private final IAction action;
		private final Button button;
		private final IAction old;

		SquareEnabler(IAction action, Button button, IAction old) {
			super("Enable Square");
			this.action = action;
			this.button = button;
			this.old = old;
		}

		@Override
		protected ExecutionType getExecutionType() {
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
	private Shell shell;

	public SwtGuiAssistant(BoardGui parent) {
		super(parent);
	}

	public void appendSquares(Iterable<Square> squares, Dimension size) {
		for (Square square : squares) {
			int x = square.getX();
			int y = size.height - square.getY() - 1;
			new SquareAppender(buttons, square, x, y).launch(shell.getDisplay());
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
		shell = new Shell(Display.getDefault());

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
		new NamedRunnable("Shell Redraw") {
			@Override
			protected ExecutionType getExecutionType() {
				return ExecutionType.GUI_ASYNCHRONOUS;
			}

			@Override
			public void trun() {
				getShell().redraw();
			}
		}.launch(shell.getDisplay());
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

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

	final Map<Square, Button> buttons = new HashMap<Square, Button>();
	protected Shell shell;

	public SwtGuiAssistant(BoardGui parent) {
		super(parent);
	}

	public void appendSquares(final Iterable<Square> squares, Dimension size) {

		for (final Square square : squares) {
			final int x = square.getX();
			final int y = size.height - square.getY() - 1;

			new NamedRunnable("Square Appender") {
				@Override
				protected ExecutionType getExecutionType() {
					return ExecutionType.GUI_ASYNCHRONOUS;
				}

				@Override
				public void trun() {
					Button button = new Button(shell, SWT.PUSH | SWT.FLAT);
					button.setSize(30, 30);
					button.setBackground(fromAwt(getParent().getSquareBgcolor(square)));
					synchronized (buttons) {
						buttons.put(SquareImpl.getInstance(x, y), button);
					}
				}
			}.launch(shell.getDisplay());
		}
	}

	public void clear() {
		new NamedRunnable("Button remover") {

			@Override
			protected ExecutionType getExecutionType() {
				return ExecutionType.GUI_ASYNCHRONOUS;
			}

			@Override
			protected void trun() {
				synchronized (buttons) {
					for (Button button : buttons.values()) {
						button.dispose();
					}

					buttons.clear();
				}
			}
		}.launch(shell.getDisplay());

		clearListeners();
	}

	public void close() {
		shell.close();
	}

	protected abstract void decorate(Button button, PieceType piece, PlayerType player);

	public void decorate(Square square, Piece piece) {
		Button button = getButton(square);
		if (piece != null) {
			decorate(button, piece.getPieceType(), piece.getPlayerType());
		}
	}

	public void disableSquare(final Square square) {
		final IAction action = removeListener(square);
		if (action != null) {
			final Button button = getButton(square);
			new NamedRunnable("Square Disabler") {
				@Override
				protected ExecutionType getExecutionType() {
					return ExecutionType.GUI_ASYNCHRONOUS;
				}

				@Override
				public void trun() {
					button.removeSelectionListener(action);
					button.setEnabled(false);

				}
			}.launch(button.getDisplay());
		}
	}

	public void enableSquare(final Square square, final IAction action) {
		final IAction old = putListener(square, action);
		final Button button = getButton(square);
		new NamedRunnable("Enable Square") {
			@Override
			protected ExecutionType getExecutionType() {
				return ExecutionType.GUI_SYNCHRONOUS;
			}

			@Override
			public void trun() {
				if (ObjectUtils.equals(action, old)) {
					return;
				}
				button.addSelectionListener(action);
				if (old == null) {
					button.setEnabled(true);
				} else {
					button.removeSelectionListener(old);
				}
			}
		}.launch(button.getDisplay());
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

	public void initDisplay() {
		shell = new Shell(Display.getDefault());

		initMenu();
		shell.setText(getParent().getWindowTitle());

		Dimension defaultSize = getParent().getDefaultSize();
		shell.setSize(defaultSize.width, defaultSize.height);

		Dimension minSize = getParent().getMinimumSize();
		shell.setMinimumSize(minSize.width, minSize.height);
	}

	public void initLayout(final Dimension size) {
		new NamedRunnable("Init Layout") {
			@Override
			protected ExecutionType getExecutionType() {
				return ExecutionType.GUI_ASYNCHRONOUS;
			}

			@Override
			public void trun() {
				GridLayout gridLayout = new GridLayout(size.width, true);
				shell.setLayout(gridLayout);
				gridLayout.horizontalSpacing = 0;
				gridLayout.verticalSpacing = 0;
				shell.pack();
			}
		}.launch(shell.getDisplay());
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
				shell.redraw();
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

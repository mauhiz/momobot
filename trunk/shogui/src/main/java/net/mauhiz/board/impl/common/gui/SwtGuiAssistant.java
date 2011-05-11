package net.mauhiz.board.impl.common.gui;

import java.awt.Dimension;
import java.util.HashMap;
import java.util.Map;

import net.mauhiz.board.impl.common.data.SquareImpl;
import net.mauhiz.board.model.data.Piece;
import net.mauhiz.board.model.data.Square;
import net.mauhiz.board.model.gui.BoardGui;
import net.mauhiz.util.IAction;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionListener;
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

	public void appendSquare(final Square square, Dimension size) {
		final int x = square.getX();
		final int y = size.height - square.getY() - 1;

		shell.getDisplay().syncExec(new Runnable() {

			@Override
			public void run() {

				Button button = new Button(shell, SWT.PUSH | SWT.FLAT);
				button.setSize(30, 30);
				button.setBackground(fromAwt(parent.getSquareBgcolor(square)));
				buttons.put(SquareImpl.getInstance(x, y), button);
			}

		});
	}

	public void clear() {
		for (Button button : buttons.values()) {
			button.dispose();
		}
		buttons.clear();
		listeners.clear();
	}

	public void close() {
		shell.close();
	}

	public void disableSquare(final Square square) {
		final Button button = getButton(square);
		shell.getDisplay().syncExec(new Runnable() {

			@Override
			public void run() {
				Color fore = button.getForeground();
				Color back = button.getBackground();
				SelectionListener action = listeners.remove(square);
				if (action != null) {
					button.removeSelectionListener(action);
				}
				button.setEnabled(false);
				button.setForeground(fore);
				button.setBackground(back);
			}
		});
	}

	public void enableSquare(final Square square, final IAction action) {
		final Button button = getButton(square);
		button.getDisplay().syncExec(new Runnable() {
			@Override
			public void run() {
				Color fore = button.getForeground();
				Color back = button.getBackground();
				button.addSelectionListener(action);
				listeners.put(square, action);
				button.setEnabled(true);
				button.setForeground(fore);
				button.setBackground(back);
			}
		});
	}

	protected Color fromAwt(java.awt.Color awtColor) {
		Display display = shell.getDisplay();
		RGB rgb = new RGB(awtColor.getRed(), awtColor.getGreen(), awtColor.getBlue());
		return new Color(display, rgb);
	}

	public Button getButton(Square at) {
		return buttons.get(at);
	}

	public void initDisplay() {
		shell = new Shell(Display.getDefault());

		initMenu();
		shell.setText(parent.getWindowTitle());

		Dimension defaultSize = parent.getDefaultSize();
		shell.setSize(defaultSize.width, defaultSize.height);

		Dimension minSize = parent.getMinimumSize();
		shell.setMinimumSize(minSize.width, minSize.height);
	}

	public void initLayout(final Dimension size) {
		shell.getDisplay().syncExec(new Runnable() {

			@Override
			public void run() {
				GridLayout gridLayout = new GridLayout(size.width, true);
				shell.setLayout(gridLayout);
				gridLayout.horizontalSpacing = 0;
				gridLayout.verticalSpacing = 0;
				shell.pack();
			}
		});
	}

	protected void initMenu() {
		Menu menuBar = new Menu(shell, SWT.BAR);
		MenuItem fileMenuHeader = new MenuItem(menuBar, SWT.CASCADE);
		fileMenuHeader.setText("&File");

		Menu fileMenu = new Menu(shell, SWT.DROP_DOWN);
		fileMenuHeader.setMenu(fileMenu);

		MenuItem fileStartItem = new MenuItem(fileMenu, SWT.PUSH);
		fileStartItem.setText("New Local &Game");
		fileStartItem.addSelectionListener(new StartAction(parent));

		MenuItem fileExitItem = new MenuItem(fileMenu, SWT.PUSH);
		fileExitItem.setText("E&xit");
		fileExitItem.addSelectionListener(new ExitAction(parent));
		shell.setMenuBar(menuBar);
	}

	public void refresh() {
		shell.getDisplay().syncExec(new Runnable() {

			@Override
			public void run() {
				shell.redraw();
			}
		});
	}

	@Override
	public void decorate(Square square, Piece piece) {
		Button button = getButton(square);
		decorate(button, piece);
	}

	protected abstract void decorate(Button button, Piece piece);

	@Override
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

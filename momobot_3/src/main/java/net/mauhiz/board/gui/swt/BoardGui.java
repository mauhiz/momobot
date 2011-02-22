package net.mauhiz.board.gui.swt;

import java.awt.Dimension;
import java.util.HashMap;
import java.util.Map;

import net.mauhiz.board.Board;
import net.mauhiz.board.Square;
import net.mauhiz.board.SquareView;
import net.mauhiz.irc.gui.actions.ExitAction;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.PaintEvent;
import org.eclipse.swt.events.PaintListener;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.MenuItem;
import org.eclipse.swt.widgets.Shell;

public abstract class BoardGui {

    protected final Map<Square, Button> buttons = new HashMap<Square, Button>();
    protected final Map<Square, SelectionListener> listeners = new HashMap<Square, SelectionListener>();
    protected Shell shell;

    public abstract void cancelSelection();

    protected void clearBoard() {
        if (buttons.isEmpty()) {
            return;
        }

        for (Square square : new SquareView(getBoardSize())) {
            Button button = buttons.get(square);
            if (button != null) {
                button.dispose();
            }
        }
    }

    protected void disableButton(Square square) {
        Button button = buttons.get(square);
        Color fore = button.getForeground();
        Color back = button.getBackground();
        SelectionListener action = listeners.get(square);
        if (action != null) {
            button.removeSelectionListener(action);
        }
        listeners.remove(square);
        button.setEnabled(false);
        button.setForeground(fore);
        button.setBackground(back);
    }

    protected void enableButton(Square square, SelectionListener action) {
        Button button = buttons.get(square);
        Color fore = button.getForeground();
        Color back = button.getBackground();
        button.addSelectionListener(action);
        listeners.put(square, action);
        button.setEnabled(true);
        button.setForeground(fore);
        button.setBackground(back);
    }

    protected abstract Board getBoard();

    public Dimension getBoardSize() {
        return getBoard().getSize();
    }

    public Button getButton(Square at) {
        return buttons.get(at);
    }

    protected void initBoard() {
        clearBoard();
        final Dimension size = getBoardSize();

        /* layout */
        GridLayout gridLayout = new GridLayout(size.width, true);
        shell.setLayout(gridLayout);
        gridLayout.horizontalSpacing = 0;
        gridLayout.verticalSpacing = 0;

        Color gray = shell.getDisplay().getSystemColor(SWT.COLOR_GRAY);

        for (Square square : new SquareView(size)) {
            int x = square.x;
            int y = size.height - square.y - 1;
            Button button = new Button(shell, SWT.PUSH | SWT.FLAT);
            buttons.put(Square.getInstance(x, y), button);
            button.setBackground(gray);
        }

        shell.addPaintListener(new PaintListener() {

            @Override
            public void paintControl(PaintEvent arg0) {
                for (Square square : new SquareView(size)) {
                    getButton(square).setSize(30, 30);
                }
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
        fileStartItem.setText("New &Game");
        fileStartItem.addSelectionListener(new StartAction(this));

        MenuItem fileExitItem = new MenuItem(fileMenu, SWT.PUSH);
        fileExitItem.setText("E&xit");
        fileExitItem.addSelectionListener(new ExitAction(shell));
        shell.setMenuBar(menuBar);
    }

    public abstract void movePiece(Square to);

    public void newGame() {
        getBoard().newGame();
        initBoard();
        refreshBoard();
    }

    protected abstract void refreshBoard();

    public abstract void selectPiece(Square at);
}

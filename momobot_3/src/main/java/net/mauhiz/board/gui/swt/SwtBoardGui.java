package net.mauhiz.board.gui.swt;

import java.awt.Dimension;
import java.util.HashMap;
import java.util.Map;

import net.mauhiz.board.Board;
import net.mauhiz.board.Move;
import net.mauhiz.board.Piece;
import net.mauhiz.board.Player;
import net.mauhiz.board.Square;
import net.mauhiz.board.gui.AbstractBoardController;
import net.mauhiz.board.gui.AbstractBoardGui;
import net.mauhiz.board.gui.ExitAction;
import net.mauhiz.board.gui.StartAction;
import net.mauhiz.util.AbstractAction;

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

public abstract class SwtBoardGui<B extends Board<? extends Piece, ? extends Player>, M extends Move> extends
        AbstractBoardGui<B, M> {

    private final Map<Square, Button> buttons = new HashMap<Square, Button>();
    protected Shell shell;

    @Override
    public void afterInit() {
        shell.pack();
    }

    @Override
    public void appendSquare(Square square, Dimension size) {
        int x = square.x;
        int y = size.height - square.y - 1;
        Button button = new Button(shell, SWT.PUSH | SWT.FLAT);
        buttons.put(Square.getInstance(x, y), button);
        button.setBackground(fromAwt(getSquareBgcolor(square)));
    }

    @Override
    public void clear() {
        for (Button button : buttons.values()) {
            button.dispose();
        }
        buttons.clear();
        listeners.clear();
    }

    @Override
    public void close() {
        shell.close();
    }

    public void disableSquare(Square square) {
        Button button = getButton(square);
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

    @Override
    protected void enableSquare(Square square, AbstractAction action) {
        Button button = getButton(square);
        Color fore = button.getForeground();
        Color back = button.getBackground();
        button.addSelectionListener(action);
        listeners.put(square, action);
        button.setEnabled(true);
        button.setForeground(fore);
        button.setBackground(back);
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
        shell.setText(getWindowTitle());

        Dimension defaultSize = getDefaultSize();
        shell.setSize(defaultSize.width, defaultSize.height);

        Dimension minSize = getMinimumSize();
        shell.setMinimumSize(minSize.width, minSize.height);

        shell.open();
        shell.pack();
    }

    public void initLayout(Dimension size) {
        GridLayout gridLayout = new GridLayout(size.width, true);
        shell.setLayout(gridLayout);
        gridLayout.horizontalSpacing = 0;
        gridLayout.verticalSpacing = 0;
    }

    protected void initMenu() {
        AbstractBoardController<B, M> controller = newController();
        Menu menuBar = new Menu(shell, SWT.BAR);
        MenuItem fileMenuHeader = new MenuItem(menuBar, SWT.CASCADE);
        fileMenuHeader.setText("&File");

        Menu fileMenu = new Menu(shell, SWT.DROP_DOWN);
        fileMenuHeader.setMenu(fileMenu);

        MenuItem fileStartItem = new MenuItem(fileMenu, SWT.PUSH);
        fileStartItem.setText("New Local &Game");
        fileStartItem.addSelectionListener(new StartAction<B, M>(this, controller));

        MenuItem fileExitItem = new MenuItem(fileMenu, SWT.PUSH);
        fileExitItem.setText("E&xit");
        fileExitItem.addSelectionListener(new ExitAction<B, M>(this));
        shell.setMenuBar(menuBar);
    }

    protected void swtLoop() {
        // SWT loop
        Display display = shell.getDisplay();

        while (!shell.isDisposed()) {
            if (!display.readAndDispatch()) {
                display.sleep();
            }
        }
        display.dispose();
    }
}

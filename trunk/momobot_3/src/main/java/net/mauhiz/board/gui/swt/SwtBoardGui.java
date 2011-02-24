package net.mauhiz.board.gui.swt;

import java.awt.Dimension;
import java.util.HashMap;
import java.util.Map;

import net.mauhiz.board.Square;
import net.mauhiz.board.gui.AbstractBoardGui;
import net.mauhiz.board.gui.BoardController;

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

public abstract class SwtBoardGui extends AbstractBoardGui {

    private final Map<Square, Button> buttons = new HashMap<Square, Button>();
    private final Map<Square, SelectionListener> listeners = new HashMap<Square, SelectionListener>();
    protected Shell shell;

    @Override
    public void addCancelAction(Square square, BoardController controller) {
        enableSquare(square, new CancelAction(controller));
    }

    @Override
    public void addMoveAction(Square square, BoardController controller) {
        enableSquare(square, new MoveAction(controller, square));
    }

    @Override
    public void addSelectAction(Square square, BoardController controller) {
        enableSquare(square, new SelectAction(controller, square));
    }

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

    protected void enableSquare(Square square, SelectionListener action) {
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

package net.mauhiz.board.gui.awt;

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

import net.mauhiz.board.Board;
import net.mauhiz.board.Move;
import net.mauhiz.board.Piece;
import net.mauhiz.board.Player;
import net.mauhiz.board.Square;
import net.mauhiz.board.gui.AbstractBoardGui;
import net.mauhiz.board.gui.ExitAction;
import net.mauhiz.board.gui.StartAction;
import net.mauhiz.util.AbstractAction;

public abstract class AwtBoardGui<B extends Board<? extends Piece, ? extends Player>, M extends Move> extends
        AbstractBoardGui<B, M> {

    private final Map<Square, Button> buttons = new HashMap<Square, Button>();
    protected Frame frame = new Frame();

    private final Map<Square, ActionListener> listeners = new HashMap<Square, ActionListener>();
    protected Panel panel;

    public void afterInit() {
        frame.pack();
    }

    public void appendSquare(Square square, Dimension size) {
        int x = square.x;
        int y = size.height - square.y - 1;
        Button button = new Button();
        buttons.put(Square.getInstance(x, y), button);
        button.setBackground(getSquareBgcolor(square));
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
    protected void enableSquare(Square square, AbstractAction action) {
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

    @Override
    protected String getWindowTitle() {
        return "AWT Gui";
    }

    public void initDisplay() {
        initMenu();
        frame.setTitle(getWindowTitle());

        Dimension defaultSize = getDefaultSize();
        frame.setSize(defaultSize);

        Dimension minSize = getMinimumSize();
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
        fileStartItem.addActionListener(new StartAction<B, M>(this, newController()));

        MenuItem fileExitItem = new MenuItem("Exit", new MenuShortcut(KeyEvent.VK_X));
        fileMenu.add(fileExitItem);
        fileExitItem.addActionListener(new ExitAction<B, M>(this));

        menuBar.add(fileMenu);
        frame.setMenuBar(menuBar);
    }
}

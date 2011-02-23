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
import net.mauhiz.board.Square;
import net.mauhiz.board.SquareView;

public abstract class BoardGui extends Frame {

    protected final Map<Square, Button> buttons = new HashMap<Square, Button>();
    protected final Map<Square, ActionListener> listeners = new HashMap<Square, ActionListener>();

    protected Panel panel;

    public abstract void cancelSelection();

    protected void clearBoard() {
        if (buttons.isEmpty()) {
            return;
        }

        for (Square square : new SquareView(getBoardSize())) {
            Button button = buttons.get(square);
            if (button != null) {
                remove(button);
            }
        }
    }

    protected void disableButton(Square square) {
        Button button = buttons.get(square);
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

    protected void enableButton(Square square, ActionListener action) {
        Button button = buttons.get(square);
        Color fore = button.getForeground();
        Color back = button.getBackground();
        button.addActionListener(action);
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

    protected void init() {

        MenuBar menuBar = new MenuBar();
        Menu fileMenu = new Menu("File");

        MenuItem fileStartItem = new MenuItem("New Game", new MenuShortcut(KeyEvent.VK_G));
        fileMenu.add(fileStartItem);
        fileStartItem.addActionListener(new StartAction(this));

        MenuItem fileExitItem = new MenuItem("Exit", new MenuShortcut(KeyEvent.VK_X));
        fileMenu.add(fileExitItem);
        fileExitItem.addActionListener(new ExitAction(this));

        menuBar.add(fileMenu);
        setMenuBar(menuBar);

        panel = new Panel();
        add(panel);
        setVisible(true);
    }

    protected void initBoard() {
        clearBoard();
        final Dimension size = getBoardSize();

        /* layout */
        GridLayout gridLayout = new GridLayout(size.width, size.height, 0, 0);
        panel.setLayout(gridLayout);

        for (Square square : new SquareView(size)) {
            int x = square.x;
            int y = size.height - square.y - 1;
            Button button = new Button();
            buttons.put(Square.getInstance(x, y), button);
            button.setBackground(Color.GRAY);
            button.setSize(30, 30);
            panel.add(button);
        }
        pack();
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

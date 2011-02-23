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

import net.mauhiz.board.Square;
import net.mauhiz.board.gui.BoardController;
import net.mauhiz.board.gui.IBoardGui;

public abstract class BoardGui extends Frame implements IBoardGui {

    protected final Map<Square, Button> buttons = new HashMap<Square, Button>();
    protected final Map<Square, ActionListener> listeners = new HashMap<Square, ActionListener>();

    protected Panel panel;

    public void addCancelAction(Square square, BoardController controller) {
        enableSquare(square, new CancelAction(controller));
    }

    public void addMoveAction(Square square, BoardController controller) {
        enableSquare(square, new MoveAction(controller, square));
    }

    public void addSelectAction(Square square, BoardController controller) {
        enableSquare(square, new SelectAction(controller, square));
    }

    public void afterInit() {
        pack();
    }

    public void appendSquare(Square square, Dimension size) {
        int x = square.x;
        int y = size.height - square.y - 1;
        Button button = new Button();
        buttons.put(Square.getInstance(x, y), button);
        button.setBackground(Color.GRAY);
        button.setSize(30, 30);
        panel.add(button);
    }

    public void clear() {
        for (Button button : buttons.values()) {
            remove(button);
        }
    }

    @Override
    public void disableSquare(Square square) {
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

    protected void enableSquare(Square square, ActionListener action) {
        Button button = buttons.get(square);
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

    protected abstract BoardController newController();

    public void initDisplay() {
        initMenu();

        panel = new Panel();
        add(panel);
        setVisible(true);
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
        fileStartItem.addActionListener(new StartAction(this));

        MenuItem fileExitItem = new MenuItem("Exit", new MenuShortcut(KeyEvent.VK_X));
        fileMenu.add(fileExitItem);
        fileExitItem.addActionListener(new ExitAction(this));

        menuBar.add(fileMenu);
        setMenuBar(menuBar);
    }

    public void newGame() {
        BoardController controller = newController();
        controller.init();
    }
}

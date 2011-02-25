package net.mauhiz.board.gui.swing;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.ActionListener;
import java.util.HashMap;
import java.util.Map;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;

import net.mauhiz.board.Board;
import net.mauhiz.board.Move;
import net.mauhiz.board.Square;
import net.mauhiz.board.gui.AbstractBoardGui;
import net.mauhiz.board.gui.ExitAction;
import net.mauhiz.board.gui.GuiBoardController;
import net.mauhiz.board.gui.StartAction;
import net.mauhiz.board.gui.remote.NewRemoteGameAction;
import net.mauhiz.util.AbstractAction;

public abstract class SwingBoardGui<B extends Board, M extends Move<B>> extends AbstractBoardGui<B, M> {

    private final Map<Square, JButton> buttons = new HashMap<Square, JButton>();
    protected JFrame frame = new JFrame();

    private final Map<Square, ActionListener> listeners = new HashMap<Square, ActionListener>();
    protected JPanel panel;

    public void afterInit() {
        frame.pack();
    }

    public void appendSquare(Square square, Dimension size) {
        int x = square.x;
        int y = size.height - square.y - 1;
        JButton button = new JButton();
        buttons.put(Square.getInstance(x, y), button);
        button.setBackground(getSquareBgcolor(square));
        button.setSize(40, 40);
        panel.add(button);
    }

    public void clear() {
        for (JButton button : buttons.values()) {
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
        JButton button = getButton(square);
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
        JButton button = getButton(square);
        Color fore = button.getForeground();
        Color back = button.getBackground();
        button.addActionListener(action);
        listeners.put(square, action);
        button.setEnabled(true);
        button.setForeground(fore);
        button.setBackground(back);
    }

    public JButton getButton(Square at) {
        return buttons.get(at);
    }

    @Override
    protected String getWindowTitle() {
        return "Swing GUI";
    }

    public void initDisplay() {
        initMenu();
        frame.setTitle(getWindowTitle());

        Dimension defaultSize = getDefaultSize();
        frame.setSize(defaultSize);

        Dimension minSize = getMinimumSize();
        frame.setMinimumSize(minSize);

        panel = new JPanel();
        frame.add(panel);
        frame.setVisible(true);
    }

    public void initLayout(Dimension size) {

        /* layout */
        GridLayout gridLayout = new GridLayout(size.width, size.height, 0, 0);
        panel.setLayout(gridLayout);
    }

    protected void initMenu() {
        GuiBoardController<B, M> controller = newController();

        /* menu */
        JMenuBar menuBar = new JMenuBar();
        JMenu fileMenu = new JMenu("File");

        JMenuItem fileStartItem = new JMenuItem("New Game");
        fileMenu.add(fileStartItem);
        fileStartItem.addActionListener(new StartAction<B, M>(this, controller));

        JMenuItem fileRemoteItem = new JMenuItem("New &Network Game");
        fileMenu.add(fileRemoteItem);
        fileRemoteItem.addActionListener(new NewRemoteGameAction<B, M>(this, controller));

        JMenuItem fileExitItem = new JMenuItem("Exit");
        fileMenu.add(fileExitItem);
        fileExitItem.addActionListener(new ExitAction<B, M>(this));

        menuBar.add(fileMenu);
        frame.setJMenuBar(menuBar);
    }
}

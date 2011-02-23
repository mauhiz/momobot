package net.mauhiz.board.gui.awt;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * @author mauhiz
 */
public class StartAction implements ActionListener {

    private final BoardGui gui;

    public StartAction(BoardGui gui) {
        super();
        this.gui = gui;
    }

    public void actionPerformed(ActionEvent e) {
        gui.newGame();
    }
}

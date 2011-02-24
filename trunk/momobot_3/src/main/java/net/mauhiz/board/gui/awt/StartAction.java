package net.mauhiz.board.gui.awt;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import net.mauhiz.board.gui.IBoardGui;

/**
 * @author mauhiz
 */
public class StartAction implements ActionListener {

    private final IBoardGui gui;

    public StartAction(IBoardGui gui) {
        super();
        this.gui = gui;
    }

    public void actionPerformed(ActionEvent e) {
        gui.newGame();
    }
}

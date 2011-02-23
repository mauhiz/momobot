package net.mauhiz.board.gui.awt;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * @author mauhiz
 */
public class CancelAction implements ActionListener {

    private final BoardGui gui;

    public CancelAction(BoardGui gui) {
        this.gui = gui;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        gui.cancelSelection();
    }
}

package net.mauhiz.board.gui.awt;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import net.mauhiz.board.gui.BoardController;

/**
 * @author mauhiz
 */
public class CancelAction implements ActionListener {

    private final BoardController controller;

    public CancelAction(BoardController controller) {
        this.controller = controller;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        controller.cancelSelection();
    }
}

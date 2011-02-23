package net.mauhiz.board.gui.awt;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import net.mauhiz.board.Square;

/**
 * @author mauhiz
 */
public class SelectAction implements ActionListener {

    private final Square at;
    private final BoardGui gui;

    public SelectAction(BoardGui gui, Square at) {
        this.gui = gui;
        this.at = at;
    }

    public void actionPerformed(ActionEvent e) {
        gui.selectPiece(at);
    }
}

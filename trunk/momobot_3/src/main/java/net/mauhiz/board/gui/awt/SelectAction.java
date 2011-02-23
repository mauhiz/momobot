package net.mauhiz.board.gui.awt;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import net.mauhiz.board.Square;
import net.mauhiz.board.gui.BoardController;

/**
 * @author mauhiz
 */
public class SelectAction implements ActionListener {

    private final Square at;
    private final BoardController gui;

    public SelectAction(BoardController gui, Square at) {
        this.gui = gui;
        this.at = at;
    }

    public void actionPerformed(ActionEvent e) {
        gui.selectPiece(at);
    }
}

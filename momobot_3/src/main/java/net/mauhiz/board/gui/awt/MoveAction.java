package net.mauhiz.board.gui.awt;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import net.mauhiz.board.Square;
import net.mauhiz.board.gui.BoardController;

/**
 * @author mauhiz
 */
public class MoveAction implements ActionListener {

    private final BoardController gui;
    private final Square to;

    public MoveAction(BoardController gui, Square to) {
        this.to = to;
        this.gui = gui;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        gui.movePiece(to);
    }
}

package net.mauhiz.board.gui.swt;

import net.mauhiz.board.Square;

import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;

/**
 * @author mauhiz
 */
public class MoveAction implements SelectionListener {

    private final BoardGui gui;
    private final Square to;

    public MoveAction(BoardGui gui, Square to) {
        this.to = to;
        this.gui = gui;
    }

    /**
     * @see org.eclipse.swt.events.SelectionListener#widgetDefaultSelected(org.eclipse.swt.events.SelectionEvent)
     */
    public void widgetDefaultSelected(SelectionEvent arg0) {
        /* nothing */
    }

    /**
     * @see org.eclipse.swt.events.SelectionListener#widgetSelected(org.eclipse.swt.events.SelectionEvent)
     */
    public void widgetSelected(SelectionEvent arg0) {
        gui.movePiece(to);
    }
}

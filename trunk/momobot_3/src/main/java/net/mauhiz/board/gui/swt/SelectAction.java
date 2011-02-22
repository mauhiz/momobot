package net.mauhiz.board.gui.swt;

import net.mauhiz.board.Square;

import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;

/**
 * @author mauhiz
 */
public class SelectAction implements SelectionListener {

    private final Square at;
    private final BoardGui gui;

    public SelectAction(BoardGui gui, Square at) {
        this.gui = gui;
        this.at = at;
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
        gui.selectPiece(at);
    }
}

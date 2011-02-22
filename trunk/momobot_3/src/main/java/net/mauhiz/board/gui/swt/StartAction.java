package net.mauhiz.board.gui.swt;

import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;

/**
 * @author mauhiz
 */
public class StartAction implements SelectionListener {
    
    private final BoardGui gui;
    
    public StartAction(BoardGui gui) {
        super();
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
        gui.newGame();
    }
}

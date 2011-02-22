package net.mauhiz.board.gui.swt;

import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.widgets.Shell;

/**
 * @author mauhiz
 */
public class ExitAction implements SelectionListener {
    private final Shell shell;
    
    public ExitAction(Shell shell) {
        this.shell = shell;
    }
    
    /**
     * @see org.eclipse.swt.events.SelectionListener#widgetDefaultSelected(org.eclipse.swt.events.SelectionEvent)
     */
    public void widgetDefaultSelected(SelectionEvent arg0) {
        /* do nothing */
    }
    
    /**
     * @see org.eclipse.swt.events.SelectionListener#widgetSelected(org.eclipse.swt.events.SelectionEvent)
     */
    public void widgetSelected(SelectionEvent arg0) {
        shell.close();
    }
}

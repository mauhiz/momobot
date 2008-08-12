package net.mauhiz.irc.gui.actions;

import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.widgets.Shell;

/**
 * @author mauhiz
 */
public class ExitAction implements SelectionListener {
    Shell shell;
    
    /**
     * @param shell1
     */
    public ExitAction(final Shell shell1) {
        shell = shell1;
    }
    
    /**
     * @see org.eclipse.swt.events.SelectionListener#widgetDefaultSelected(org.eclipse.swt.events.SelectionEvent)
     */
    public void widgetDefaultSelected(final SelectionEvent arg0) {
    }
    
    /**
     * @see org.eclipse.swt.events.SelectionListener#widgetSelected(org.eclipse.swt.events.SelectionEvent)
     */
    public void widgetSelected(final SelectionEvent arg0) {
        shell.close();
    }
}

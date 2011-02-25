package net.mauhiz.util;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;

/**
 * This class is an action for both SWT and AWT/Swing
 * @author mauhiz
 */
public abstract class AbstractAction implements SelectionListener, ActionListener {

    @Override
    public void actionPerformed(ActionEvent e) {
        doAction();
    }

    protected abstract void doAction();

    @Override
    public void widgetDefaultSelected(SelectionEvent arg0) {
        // nothing
    }

    @Override
    public void widgetSelected(SelectionEvent arg0) {
        doAction();
    }

}

package net.mauhiz.util;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.lang.reflect.InvocationTargetException;

import javax.swing.SwingUtilities;

import org.apache.log4j.Logger;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;

/**
 * This class is an action for both SWT and AWT/Swing
 * @author mauhiz
 */
public abstract class AbstractAction implements SelectionListener, ActionListener {
    class ActionRunnable implements Runnable {
        @Override
        public void run() {
            doAction();
        }
    }

    protected static final Logger LOG = Logger.getLogger(AbstractAction.class);

    @Override
    public void actionPerformed(ActionEvent e) {
        if (isAsynchronous()) {
            SwingUtilities.invokeLater(new ActionRunnable());
        } else {
            try {
                SwingUtilities.invokeAndWait(new ActionRunnable());
            } catch (InterruptedException ie) {
                AbstractRunnable.handleInterruption(ie);
            } catch (InvocationTargetException ite) {
                LOG.error(ite.getTargetException(), ite);
            }
        }
    }

    protected abstract void doAction();

    protected abstract boolean isAsynchronous();

    @Override
    public void widgetDefaultSelected(SelectionEvent arg0) {
        // nothing
    }

    @Override
    public void widgetSelected(SelectionEvent arg0) {
        if (isAsynchronous()) {
            arg0.display.asyncExec(new ActionRunnable());
        } else {
            arg0.display.syncExec(new ActionRunnable());
        }
    }

}

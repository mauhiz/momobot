package net.mauhiz.util;

import java.awt.event.ActionEvent;
import java.lang.reflect.InvocationTargetException;

import javax.swing.SwingUtilities;

import org.apache.commons.lang.NotImplementedException;
import org.apache.log4j.Logger;
import org.eclipse.swt.events.SelectionEvent;

/**
 * This class is an action for both SWT and AWT/Swing
 * @author mauhiz
 */
public abstract class AbstractAction implements IAction {
    class ActionRunnable implements Runnable {
        @Override
        public void run() {
            doAction();
        }
    }

    protected static final Logger LOG = Logger.getLogger(AbstractAction.class);

    @Override
    public void actionPerformed(ActionEvent e) {
        ActionRunnable ar = new ActionRunnable();
        switch (getExecutionType()) {
        case NON_GUI:
            new Thread(ar).start();
            break;
        case GUI_ASYNCHRONOUS:
            SwingUtilities.invokeLater(ar);
            break;
        case GUI_SYNCHRONOUS:
            try {
                SwingUtilities.invokeAndWait(ar);
            } catch (InterruptedException ie) {
                AbstractRunnable.handleInterruption(ie);
            } catch (InvocationTargetException ite) {
                LOG.error(ite.getTargetException(), ite);
            }
            break;
        default:
            throw new NotImplementedException();
        }
    }

    protected abstract void doAction();

    protected abstract ExecutionType getExecutionType();

    @Override
    public void widgetDefaultSelected(SelectionEvent arg0) {
        // nothing
    }

    @Override
    public void widgetSelected(SelectionEvent arg0) {
        ActionRunnable ar = new ActionRunnable();
        switch (getExecutionType()) {
        case NON_GUI:
            new Thread(ar).start();
            break;
        case GUI_ASYNCHRONOUS:
            arg0.display.asyncExec(ar);
            break;
        case GUI_SYNCHRONOUS:
            arg0.display.syncExec(ar);
            break;
        default:
            throw new NotImplementedException();
        }
    }

}

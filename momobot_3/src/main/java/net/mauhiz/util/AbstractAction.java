package net.mauhiz.util;

import java.awt.event.ActionEvent;

import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.widgets.Display;

/**
 * This class is an action for both SWT and AWT/Swing
 * @author mauhiz
 */
public abstract class AbstractAction extends AbstractRunnable implements IAction, ISwtRunnable {

    public void actionPerformed(ActionEvent e) {
        launch();
    }

    /**
     * Use this method to launch without stop control.
     * @param display
     */
    public void launch(Display display) {
        ThreadManager.launch(this, display);
    }

    public void widgetDefaultSelected(SelectionEvent arg0) {
        // nothing
    }

    public void widgetSelected(SelectionEvent arg0) {
        launch(arg0.display);
    }
}

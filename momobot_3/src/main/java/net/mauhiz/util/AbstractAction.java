package net.mauhiz.util;

import java.awt.event.ActionEvent;

import org.eclipse.swt.events.SelectionEvent;

/**
 * This class is an action for both SWT and AWT/Swing
 * @author mauhiz
 */
public abstract class AbstractAction extends AbstractRunnable implements IAction {

    public AbstractAction() {
        super();
    }

    public void actionPerformed(ActionEvent e) {
        launch(null);
    }

    public void widgetDefaultSelected(SelectionEvent arg0) {
        // nothing
    }

    public void widgetSelected(SelectionEvent arg0) {
        launch(arg0.display);
    }
}

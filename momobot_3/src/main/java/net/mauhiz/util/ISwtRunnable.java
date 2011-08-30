package net.mauhiz.util;

import org.eclipse.swt.widgets.Display;

/**
 * @author mauhiz
 */
public interface ISwtRunnable extends IRunnable {

    void launch(Display display);
}

package net.mauhiz.util;

import org.eclipse.swt.widgets.Display;

/**
 * @author mauhiz
 */
public interface IRunnable extends Runnable {

    void launch(Display display);
}

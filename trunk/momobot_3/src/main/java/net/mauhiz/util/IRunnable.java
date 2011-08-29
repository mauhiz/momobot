package net.mauhiz.util;

import org.eclipse.swt.widgets.Display;

/**
 * @author mauhiz
 */
public interface IRunnable extends Runnable {

    ExecutionType getExecutionType();

    String getName();

    void launch(Display display);
}

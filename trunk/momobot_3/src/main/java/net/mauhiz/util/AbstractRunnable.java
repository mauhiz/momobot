package net.mauhiz.util;

import org.eclipse.swt.widgets.Display;

/**
 * @author mauhiz
 */
public abstract class AbstractRunnable implements IRunnable {

    public String getName() {
        return getClass().getName();
    }

    /**
     * Use this method to launch without stop control.
     * @param display
     */
    public void launch(Display display) {
        ThreadManager.launch(this, display);
    }

    public final void run() {
        if (getExecutionType() == ExecutionType.DAEMON || getExecutionType() == ExecutionType.PARALLEL_CACHED) {
            // change worker thread name
            Thread currentThread = Thread.currentThread();
            String nameBak = currentThread.getName();
            currentThread.setName(getName());
            trun();
            currentThread.setName(nameBak);
        } else {
            trun();
        }
    }

    protected abstract void trun();
}

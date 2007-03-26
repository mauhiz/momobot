package utils;

import org.apache.log4j.Logger;

/**
 * @author Administrator
 */
public abstract class MyRunnable implements Runnable {
    /**
     * logger.
     */
    private static final Logger LOG     = Logger.getLogger(MyRunnable.class);
    /**
     * si le thread tourne.
     */
    private boolean             running = false;

    /**
     * lance le thread.
     */
    public final void execute() {
        new Thread(this).start();
    }

    /**
     * @return si le runnable est lancé.
     */
    public final boolean isRunning() {
        return this.running;
    }

    /**
     * @see java.lang.Runnable#run()
     */
    public abstract void run();

    /**
     * arrete le runnable.
     * @param running1
     *            un bouleen
     */
    public final void setRunning(final boolean running1) {
        this.running = running1;
    }

    /**
     * @param duree
     *            la durée à dormir
     */
    public final void sleep(final long duree) {
        try {
            Thread.sleep(duree);
        } catch (final InterruptedException e) {
            if (LOG.isErrorEnabled()) {
                LOG.error(e, e);
            }
            setRunning(false);
        }
    }
}

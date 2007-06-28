package utils;

import org.apache.log4j.Logger;

/**
 * @author mauhiz
 */
public abstract class AbstractRunnable implements Runnable {
    /**
     * logger.
     */
    private static final Logger LOG = Logger.getLogger(AbstractRunnable.class);
    /**
     * si le thread tourne.
     */
    private boolean             running;

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
     * arrete le runnable.
     * @param running1
     *            un booléen
     */
    public final void setRunning(final boolean running1) {
        this.running = running1;
    }

    /**
     * @param duree
     *            la durée à dormir
     */
    public final void pause(final long duree) {
        if (!sleep(duree)) {
            setRunning(false);
        }
    }

    /**
     * @param duree
     *            en ms
     * @return si j'ai bien dormi
     */
    public static final boolean sleep(final long duree) {
        try {
            Thread.sleep(duree);
        } catch (final InterruptedException ie) {
            LOG.error(ie, ie);
            return false;
        }
        return true;
    }
}

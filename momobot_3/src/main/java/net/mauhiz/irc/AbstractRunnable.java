package net.mauhiz.irc;

import org.apache.log4j.Logger;

/**
 * @author mauhiz
 */
public abstract class AbstractRunnable implements IRunnable {
    /**
     * logger.
     */
    private static final Logger LOG = Logger.getLogger(AbstractRunnable.class);
    /**
     * @param duree
     *            en ms
     * @return si j'ai bien dormi
     */
    public static final boolean sleep(final long duree) {
        try {
            Thread.sleep(duree);
            return true;
        } catch (final InterruptedException ie) {
            LOG.error(ie, ie);
            Thread.currentThread().interrupt();
            return false;
        }
        
    }
    
    /**
     * si le thread tourne.
     */
    private boolean running;
    
    /**
     * @see net.mauhiz.irc.IRunnable#isRunning()
     */
    public final boolean isRunning() {
        return running;
    }
    
    /**
     * @see net.mauhiz.irc.IRunnable#pause(long)
     */
    public final void pause(final long duree) {
        if (!sleep(duree)) {
            setRunning(false);
        }
    }
    
    /**
     * @see net.mauhiz.irc.IRunnable#setRunning(boolean)
     */
    public final void setRunning(final boolean running1) {
        running = running1;
    }
    
    /**
     * @see net.mauhiz.irc.IRunnable#startAs(java.lang.String)
     */
    public final void startAs(final String name) {
        new Thread(this, name).start();
    }
}

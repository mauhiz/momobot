package net.mauhiz.irc;

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
     * lance le thread.
     * 
     * @param name
     */
    public final void execute(final String name) {
        new Thread(this, name).start();
    }
    
    /**
     * @return si le runnable est lancé.
     */
    public final boolean isRunning() {
        return running;
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
     * arrete le runnable.
     * 
     * @param running1
     *            un booléen
     */
    public final void setRunning(final boolean running1) {
        running = running1;
    }
}

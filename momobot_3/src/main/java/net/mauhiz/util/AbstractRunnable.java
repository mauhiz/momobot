package net.mauhiz.util;

import org.apache.log4j.Logger;

/**
 * @author mauhiz
 */
public abstract class AbstractRunnable implements IRunnable {
    /**
     * logger.
     */
    protected static final Logger LOG = Logger.getLogger(AbstractRunnable.class);
    public static void handleInterruption(InterruptedException ie) {
        LOG.error(ie, ie);
        Thread.currentThread().interrupt();
    }
    
    /**
     * @param duree
     *            en ms
     * @return si j'ai bien dormi
     */
    public static boolean sleep(long duree) {
        try {
            Thread.sleep(duree);
            return true;
        } catch (InterruptedException ie) {
            handleInterruption(ie);
            return false;
        }
        
    }
    
    /**
     * si le thread tourne.
     */
    private boolean running;
    
    /**
     * @see net.mauhiz.util.IRunnable#isRunning()
     */
    public boolean isRunning() {
        return running;
    }
    
    /**
     * @see net.mauhiz.util.IRunnable#pause(long)
     */
    public void pause(long duree) {
        if (!sleep(duree)) {
            stop();
        }
    }
    
    /**
     * @see net.mauhiz.util.IRunnable#startAs(java.lang.String)
     */
    public void startAs(String name) {
        running = true;
        Thread t = new Thread(new Runnable() {
            /**
             * @see java.lang.Runnable#run()
             */
            public void run() {
                AbstractRunnable.this.run();
                stop();
            }
        }, name);
        t.start();
    }
    
    /**
     * @see net.mauhiz.util.IRunnable#stop()
     */
    public void stop() {
        running = false;
    }
}

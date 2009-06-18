package net.mauhiz.util;

/**
 * @author mauhiz
 * 
 * @param <R>
 */
public interface IRunnable extends Runnable {
    
    /**
     * @return si le runnable est lance.
     */
    boolean isRunning();
    
    /**
     * @param duree
     *            la duree a dormir
     */
    void pause(long duree);
    
    /**
     * lance le thread.
     * 
     * @param name
     */
    void startAs(String name);
    
    /**
     * arrete le runnable.
     * 
     * @param running1
     */
    void stop();
}

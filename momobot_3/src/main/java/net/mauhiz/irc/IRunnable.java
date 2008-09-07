package net.mauhiz.irc;

/**
 * @author mauhiz
 * 
 */
public interface IRunnable extends Runnable {
    
    /**
     * lance le thread.
     * 
     * @param name
     */
    void startAs(final String name);
    
    /**
     * @return si le runnable est lancé.
     */
    boolean isRunning();
    
    /**
     * @param duree
     *            la durée à dormir
     */
    void pause(final long duree);
    
    /**
     * arrete le runnable.
     * 
     * @param running1
     *            un booléen
     */
    void setRunning(final boolean running1);
    
}

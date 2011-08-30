package net.mauhiz.util;

/**
 * @author mauhiz
 */
public interface IDaemon extends IRunnable {

    /**
     * @return si le runnable est lance.
     */
    boolean isRunning();

    /**
     * @param duree
     *            la duree a dormir
     */
    void pause(long duree);

    void setName(String newName);

    /**
     * lance le thread, en attendant un {@link #tstop()}
     */
    void tstart();

    /**
     * arrete le runnable.
     */
    void tstop();
}

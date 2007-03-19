package utils;

/**
 * @author Administrator
 */
public abstract class MyRunnable implements Runnable {
    /**
     * si le thread tourne.
     */
    private boolean running = false;

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
            Utils.logError(getClass(), e);
            setRunning(false);
        }
    }
}

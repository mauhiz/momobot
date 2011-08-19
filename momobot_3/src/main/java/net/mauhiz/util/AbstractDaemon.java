package net.mauhiz.util;

public abstract class AbstractDaemon extends Thread implements IDaemon {

    /**
    * si le thread tourne.
    */
    private boolean running;

    public AbstractDaemon(String name) {
        super(name);
        setDaemon(true);
    }

    protected void handleInterruption(InterruptedException ie) {
        tstop();
        ThreadUtils.handleInterruption(ie);
    }

    /**
     * @see IDaemon#isRunning()
     */
    public boolean isRunning() {
        return running;
    }

    /**
     * @see IDaemon#pause(long)
     */
    public void pause(long duree) {
        if (!ThreadUtils.safeSleep(duree)) {
            tstop();
        }
    }

    @Override
    public final void run() {
        running = true;
        try {
            trun();
        } catch (InterruptedException ie) {
            handleInterruption(ie);
        }
    }

    @Override
    @Deprecated
    public synchronized void start() {
        throw new UnsupportedOperationException("Call tstart()");
    }

    protected abstract void trun() throws InterruptedException;

    public void tstart() {
        super.start();
    }

    public void tstop() {
        running = false;
    }
}
package net.mauhiz.util;

import java.util.Iterator;
import java.util.Map.Entry;
import java.util.concurrent.CancellationException;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

class CleanerThread extends AbstractDaemon {
    private static final long CLEANER_SLEEP = 5_000;

    public CleanerThread() {
        super("Future Cleaner");
    }

    @Override
    protected void trun() throws InterruptedException {
        while (isRunning()) {
            pause(CLEANER_SLEEP);
            synchronized (AbstractRunnable.FUTURES) {
                for (Iterator<Entry<Future<?>, String>> it = AbstractRunnable.FUTURES.entrySet().iterator(); it.hasNext();) {
                    Entry<Future<?>, String> ent = it.next();
                    Future<?> future = ent.getKey();

                    if (future.isDone()) {
                        it.remove();
                        try {
                            future.get();
                        } catch (ExecutionException ee) {
                            AbstractRunnable.LOG.error("Error in: " + ent.getValue(), ee.getCause());
                        } catch (CancellationException ce) {
                            AbstractRunnable.LOG.error("Cancelled: " + ent.getValue(), ce);
                        }
                    }
                }
            }
        }
    }
}
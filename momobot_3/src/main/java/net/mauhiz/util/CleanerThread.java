package net.mauhiz.util;

import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.CancellationException;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

import org.apache.log4j.Logger;

public class CleanerThread extends AbstractDaemon {
    private static final long CLEANER_SLEEP = 5_000;
    private static final Logger LOG = Logger.getLogger(CleanerThread.class);

    private final Map<Future<?>, String> futures;

    public CleanerThread(Map<Future<?>, String> futures) {
        super("Future Cleaner");
        this.futures = futures;
    }

    private void handleNextFuture(Iterator<Entry<Future<?>, String>> it) {
        Entry<Future<?>, String> ent = it.next();
        Future<?> future = ent.getKey();

        if (future.isDone()) {
            it.remove();
            try {
                future.get();
            } catch (ExecutionException ee) {
                LOG.error("Error in: " + ent.getValue(), ee.getCause());
            } catch (CancellationException ce) {
                LOG.error("Cancelled: " + ent.getValue(), ce);
            } catch (InterruptedException e) {
                handleInterruption(e);
            }
        }
    }

    @Override
    protected void trun() {
        while (isRunning()) {
            pause(CLEANER_SLEEP);

            synchronized (futures) {
                Iterator<Entry<Future<?>, String>> it = futures.entrySet().iterator();
                while (it.hasNext()) { // bug : eclipse autoformat does not like my for
                    handleNextFuture(it);
                }
            }
        }
    }
}
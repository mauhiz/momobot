package net.mauhiz.util;

import java.awt.EventQueue;
import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import org.apache.log4j.Logger;
import org.eclipse.swt.widgets.Display;

/**
 * @author mauhiz
 */
abstract class AbstractRunnable implements IRunnable {
    class DaemonRunnable extends AbstractDaemon {
        DaemonRunnable(String name) {
            super(name);
        }

        @Override
        protected void trun() {
            AbstractRunnable.this.run();
        }
    }

    protected static final ExecutorService DEFAULT_EXECUTOR = Executors.newCachedThreadPool();
    static final Map<Future<?>, String> FUTURES = new HashMap<>();

    /**
     * logger.
     */
    static final Logger LOG = Logger.getLogger(AbstractRunnable.class);

    static {
        new CleanerThread().tstart();
    }

    protected abstract ExecutionType getExecutionType();

    public String getName() {
        return getClass().getName();
    }

    /**
     * Use this method to launch without stop control.
     * @param display
     */
    public void launch(Display display) {
        ExecutionType et = getExecutionType();
        switch (et) {
            case SINGLE:
                run();
                break;
            case DAEMON:
                new DaemonRunnable(getName()).tstart();
                break;
            case PARALLEL_CACHED:
                synchronized (FUTURES) {
                    FUTURES.put(DEFAULT_EXECUTOR.submit(this), getName());
                }
                break;
            case GUI_ASYNCHRONOUS:
                if (display == null) {
                    if (EventQueue.isDispatchThread()) {
                        run();
                    } else {
                        EventQueue.invokeLater(this);
                    }
                } else {
                    display.asyncExec(this);
                }
                break;
            case GUI_SYNCHRONOUS:
                if (display == null) {
                    if (EventQueue.isDispatchThread()) {
                        run();
                    } else {
                        try {
                            EventQueue.invokeAndWait(this);
                        } catch (InterruptedException ie) {
                            ThreadUtils.handleInterruption(ie);
                        } catch (InvocationTargetException ite) {
                            LOG.error(ite.getTargetException(), ite);
                        }
                    }
                } else {
                    display.syncExec(this);
                }
                break;
            default:
                throw new IllegalStateException("Unknown excution type: " + et);
        }
    }

    public final void run() {
        if (getExecutionType() == ExecutionType.DAEMON || getExecutionType() == ExecutionType.PARALLEL_CACHED) {
            // change worker thread name
            Thread currentThread = Thread.currentThread();
            String nameBak = currentThread.getName();
            currentThread.setName(getName());
            trun();
            currentThread.setName(nameBak);
        } else {
            trun();
        }
    }

    protected abstract void trun();
}

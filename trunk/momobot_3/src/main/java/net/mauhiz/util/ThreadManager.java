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

public enum ThreadManager {
    ;
    private static final ExecutorService DEFAULT_EXECUTOR = Executors.newCachedThreadPool();
    private static final Map<Future<?>, String> FUTURES = new HashMap<>();
    private static final Logger LOG = Logger.getLogger(ThreadManager.class);
    static {
        new CleanerThread(FUTURES).tstart();
    }

    public static void launch(IRunnable runn) {
        launch(runn, null);
    }

    public static void launch(IRunnable runn, Display display) {
        ExecutionType et = runn.getExecutionType();
        switch (et) {
            case SINGLE:
                runn.run();
                break;
            case DAEMON:
                new DaemonRunnable(runn).tstart();
                break;
            case PARALLEL_CACHED:
                synchronized (FUTURES) {
                    FUTURES.put(DEFAULT_EXECUTOR.submit(runn), runn.getName());
                }
                break;
            case GUI_ASYNCHRONOUS:
                if (display == null) {
                    if (EventQueue.isDispatchThread()) {
                        runn.run();
                    } else {
                        EventQueue.invokeLater(runn);
                    }
                } else {
                    display.asyncExec(runn);
                }
                break;
            case GUI_SYNCHRONOUS:
                if (display == null) {
                    if (EventQueue.isDispatchThread()) {
                        runn.run();
                    } else {
                        try {
                            EventQueue.invokeAndWait(runn);
                        } catch (InterruptedException ie) {
                            ThreadUtils.handleInterruption(ie);
                        } catch (InvocationTargetException ite) {
                            LOG.error(ite.getTargetException(), ite);
                        }
                    }
                } else {
                    display.syncExec(runn);
                }
                break;
            default:
                throw new IllegalStateException("Unknown excution type: " + et);
        }
    }

    public static void shutdown() {
        DEFAULT_EXECUTOR.shutdown();
    }

}

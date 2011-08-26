package net.mauhiz.util;

import org.apache.log4j.Logger;

public enum ThreadUtils {
    ;

    /**
     * logger.
     */
    private static final Logger LOG = Logger.getLogger(ThreadUtils.class);

    public static String getCaller() {
        // 0 = Thread.getStackTrace()
        // 1 = ThreadUtils.getCaller()
        // 2 = caller method
        // 3 = real deal
        StackTraceElement[] str = Thread.currentThread().getStackTrace();
        StackTraceElement ste = str[3];
        if (MonitoredRunnable.class.getName().equals(ste.getClassName())) {
            return "";
        }
        return "(" + ste.getFileName() + ":" + ste.getLineNumber() + ") ";
    }

    public static String getThread() {
        return "{" + Thread.currentThread().getName() + "} ";
    }

    public static void handleInterruption(InterruptedException ie) {
        LOG.error(ie, ie);
        Thread.currentThread().interrupt();
    }

    /**
     * @param duree
     *            en ms
     * @return si j'ai bien dormi
     */
    public static boolean safeSleep(long duree) {
        try {
            Thread.sleep(duree);
            return true;
        } catch (InterruptedException ie) {
            handleInterruption(ie);
            return false;
        }
    }
}

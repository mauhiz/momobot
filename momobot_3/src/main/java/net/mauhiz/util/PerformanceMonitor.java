package net.mauhiz.util;

import org.apache.commons.lang3.time.DateFormatUtils;
import org.apache.commons.lang3.time.StopWatch;

public class PerformanceMonitor extends StopWatch {
    private static final long SLOW_THRESHOLD = 100; // time in ms
    private static final long SUPER_SLOW_THRESHOLD = 500; // time in ms

    private static String getTimestamp() {
        return DateFormatUtils.format(System.currentTimeMillis(), "[HH:mm:ss.SSS] ");

    }

    private final long errThrsh;
    private final long outThrsh;

    public PerformanceMonitor() {
        this(SLOW_THRESHOLD, SUPER_SLOW_THRESHOLD);
    }

    public PerformanceMonitor(long outThrsh, long errThrsh) {
        super();
        this.errThrsh = errThrsh;
        this.outThrsh = outThrsh;
        start();
    }

    public void perfLog(String toLog) {
        stop();
        if (getTime() > outThrsh) {
            String logMsg = getTimestamp() + ThreadUtils.getThread() + ThreadUtils.getCaller() + toLog + " in [" + this
                    + "]";
            // do not mix output (System.err and System.out)
            synchronized (System.class) {
                if (getTime() > errThrsh) {
                    System.err.println(logMsg);
                } else {
                    System.out.println(logMsg);
                }
            }

        }
        reset();
    }

    @Override
    public void resume() {
        try {
            super.resume();
        } catch (IllegalStateException ise) {
            // already running...
        }
    }

    @Override
    public void start() {
        try {
            super.start();
        } catch (IllegalStateException ise) {
            // already started...
        }
    }
}

package net.mauhiz.util;

public abstract class MonitoredRunnable extends NamedRunnable {

    protected String logMsg;

    public MonitoredRunnable() {
        this(null);
    }

    public MonitoredRunnable(String name) {
        super(name);
    }

    public MonitoredRunnable(String name, String logMsg) {
        this(name);
        this.logMsg = logMsg;
    }

    public String getLogMsg() {
        return logMsg;
    }

    protected abstract void mrun();

    @Override
    public final void trun() {
        PerformanceMonitor mon = new PerformanceMonitor();
        mrun();
        mon.perfLog(getLogMsg());
    }
}
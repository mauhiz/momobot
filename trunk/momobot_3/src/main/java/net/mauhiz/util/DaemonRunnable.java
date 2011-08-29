package net.mauhiz.util;

public class DaemonRunnable extends AbstractDaemon {
    private final IRunnable proxyRunnable;

    DaemonRunnable(IRunnable proxyRunnable) {
        super(proxyRunnable.getName());
        this.proxyRunnable = proxyRunnable;
    }

    @Override
    protected void trun() {
        proxyRunnable.run();
    }
}
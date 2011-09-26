package net.mauhiz.util;

public abstract class AbstractDaemon extends AbstractThread {

    public AbstractDaemon(String name) {
        super(name);
    }

    @Override
    public ExecutionType getExecutionType() {
        return ExecutionType.DAEMON;
    }

    @Override
    public void tstart() {
        Thread thread = new Thread(this);
        thread.setDaemon(true);
        thread.start();
    }
}
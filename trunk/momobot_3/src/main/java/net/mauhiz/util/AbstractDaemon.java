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
    public void run() {
        Thread.currentThread().setDaemon(true);
        super.run();
    }
}
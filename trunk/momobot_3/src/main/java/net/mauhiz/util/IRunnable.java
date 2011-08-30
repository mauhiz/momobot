package net.mauhiz.util;


/**
 * @author mauhiz
 */
public interface IRunnable extends Runnable {

    ExecutionType getExecutionType();

    String getName();

    void launch();
}

package net.mauhiz.util;

public enum ExecutionType {
    /**
     * Runs in a new background thread
     */
    DAEMON,
    /**
     * Delayed GUI Update
     */
    GUI_ASYNCHRONOUS,
    /**
     * Priority GUI Update
     */
    GUI_SYNCHRONOUS,
    /**
     * Runs in another thread (reused)
     */
    PARALLEL_CACHED,
    /**
     * Runs in the current thread
     */
    SINGLE;
}

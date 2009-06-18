package net.mauhiz.irc.bot;

import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * @author mauhiz
 */
public final class TriggerExecutor extends ThreadPoolExecutor {
    private static final int BASE_CAPACITY = 1;
    private static final TriggerExecutor INSTANCE = new TriggerExecutor();
    private static final int MAX_CAPACITY = 100;
    /**
     * @return {@link #INSTANCE}
     */
    public static TriggerExecutor getInstance() {
        return INSTANCE;
    }
    
    /**
     * default and only ctor
     */
    private TriggerExecutor() {
        super(BASE_CAPACITY, MAX_CAPACITY, 1L, TimeUnit.MINUTES, new LinkedBlockingQueue<Runnable>(MAX_CAPACITY));
    }
}

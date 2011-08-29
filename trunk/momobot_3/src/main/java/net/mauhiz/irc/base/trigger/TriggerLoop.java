package net.mauhiz.irc.base.trigger;

import java.util.concurrent.ForkJoinPool;

import net.mauhiz.irc.base.IIrcControl;
import net.mauhiz.irc.base.msg.IIrcMessage;
import net.mauhiz.util.AbstractNamedRunnable;
import net.mauhiz.util.ExecutionType;

import org.apache.log4j.Logger;

/**
 * @author mauhiz
 * 
 */
public class TriggerLoop extends AbstractNamedRunnable {
    private static final Logger LOG = Logger.getLogger(TriggerLoop.class);
    private final IIrcControl control;
    private final ITriggerManager defaultTriggerManager;
    private final IIrcMessage msg;

    /**
     * @param control
     * @param msg
     * @param defaultTriggerManager TODO
     */
    public TriggerLoop(ITriggerManager defaultTriggerManager, IIrcMessage msg, IIrcControl control) {
        super("Trigger Loop");
        this.defaultTriggerManager = defaultTriggerManager;
        this.control = control;
        this.msg = msg;
    }

    @Override
    public ExecutionType getExecutionType() {
        return ExecutionType.PARALLEL_CACHED;
    }

    @Override
    public void trun() {
        try {
            ForkJoinPool pool = new ForkJoinPool(5);
            for (ITrigger trigger : defaultTriggerManager.getTriggers()) {
                pool.invoke(new TriggerTask(msg, control, trigger));
            }

        } catch (RuntimeException unexpected) {
            LOG.error(unexpected, unexpected);
        }
    }
}
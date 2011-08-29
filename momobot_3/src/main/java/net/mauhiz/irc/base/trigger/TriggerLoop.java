package net.mauhiz.irc.base.trigger;

import java.util.concurrent.ForkJoinPool;

import net.mauhiz.irc.base.IIrcControl;
import net.mauhiz.irc.base.msg.IIrcMessage;
import net.mauhiz.util.AbstractNamedRunnable;
import net.mauhiz.util.ExecutionType;

/**
 * @author mauhiz
 * 
 */
public class TriggerLoop extends AbstractNamedRunnable {
    private final IIrcControl control;
    private final ITriggerManager defaultTriggerManager;
    private final IIrcMessage msg;

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
        ForkJoinPool pool = new ForkJoinPool(5);
        for (ITrigger trigger : defaultTriggerManager.getTriggers()) {
            pool.invoke(new TriggerTask(msg, control, trigger));
        }
    }
}
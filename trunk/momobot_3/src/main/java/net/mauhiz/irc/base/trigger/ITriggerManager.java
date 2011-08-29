package net.mauhiz.irc.base.trigger;

import net.mauhiz.irc.base.IIrcControl;
import net.mauhiz.irc.base.msg.IIrcMessage;

/**
 * @author mauhiz
 */
public interface ITriggerManager {
    /**
     * @return true if message was consumed
     */
    boolean processMsg(IIrcMessage msg, IIrcControl ircControl);

    public abstract Iterable<ITrigger> getTriggers();
}

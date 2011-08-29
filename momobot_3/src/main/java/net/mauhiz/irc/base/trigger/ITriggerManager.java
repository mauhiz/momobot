package net.mauhiz.irc.base.trigger;

import net.mauhiz.irc.base.IIrcControl;
import net.mauhiz.irc.base.MsgState;
import net.mauhiz.irc.base.msg.IIrcMessage;

/**
 * @author mauhiz
 */
public interface ITriggerManager {
    public abstract Iterable<ITrigger> getTriggers();

    /**
     * @return true if message was consumed
     */
    MsgState processMsg(IIrcMessage msg, IIrcControl ircControl);
}

package net.mauhiz.irc.base.trigger;

import net.mauhiz.irc.base.IIrcControl;
import net.mauhiz.irc.base.msg.Kick;

/**
 * @author mauhiz
 */
public interface IKickTrigger extends ITrigger {
    /**
     * @param im
     * @param control
     */
    void doTrigger(Kick im, IIrcControl control);
}

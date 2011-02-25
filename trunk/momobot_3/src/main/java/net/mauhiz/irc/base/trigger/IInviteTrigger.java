package net.mauhiz.irc.base.trigger;

import net.mauhiz.irc.base.IIrcControl;
import net.mauhiz.irc.base.msg.Invite;

/**
 * @author mauhiz
 */
public interface IInviteTrigger extends ITrigger {
    /**
     * @param im
     * @param control
     */
    void doTrigger(Invite im, IIrcControl control);
}

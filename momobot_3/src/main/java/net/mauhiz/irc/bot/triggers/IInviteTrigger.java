package net.mauhiz.irc.bot.triggers;

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
    void doTrigger(final Invite im, final IIrcControl control);
}

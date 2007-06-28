package ircbot.trigger;

import ircbot.Channel;
import ircbot.IrcUser;

/**
 * @author mauhiz
 */
public interface IInviteTrigger {
    /**
     * @param invitedBy
     *            le user
     * @param toChannel
     *            le channel
     */
    void executeInviteTrigger(final Channel toChannel, final IrcUser invitedBy);
}

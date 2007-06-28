package ircbot.trigger;

import ircbot.Channel;
import ircbot.IrcUser;

/**
 * @author mauhiz
 */
public interface IPublicTrigger extends ITrigger {
    /**
     * @param from
     *            le user
     * @param channel
     *            le channel
     * @param message
     *            le message
     */
    void executePublicTrigger(final IrcUser from, final Channel channel, final String message);
}

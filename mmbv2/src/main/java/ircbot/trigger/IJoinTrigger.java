package ircbot.trigger;

import ircbot.Channel;
import ircbot.IrcUser;

/**
 * @author mauhiz
 */
public interface IJoinTrigger {
    /**
     * @param joiner
     *            le user
     * @param channel
     *            le channel
     */
    void executeJoinTrigger(final Channel channel, final IrcUser joiner);
}

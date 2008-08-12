package net.mauhiz.irc.bot.event;

import net.mauhiz.irc.base.data.Channel;

/**
 * @author mauhiz
 */
public interface IChannelEvent {
    /**
     * @return the channel
     */
    Channel getChannel();
    
    /**
     * @return un msg
     */
    String stop();
}

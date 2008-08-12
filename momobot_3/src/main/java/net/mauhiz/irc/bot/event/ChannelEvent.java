package net.mauhiz.irc.bot.event;

import java.util.HashMap;
import java.util.Map;

import net.mauhiz.irc.base.data.Channel;

import org.apache.log4j.Logger;

/**
 * @author mauhiz
 */
public abstract class ChannelEvent implements IChannelEvent {
    public static final Map<Channel, ChannelEvent> CHANNEL_EVENTS = new HashMap<Channel, ChannelEvent>();
    /**
     * logger.
     */
    private static final Logger LOG = Logger.getLogger(ChannelEvent.class);
    /**
     * le nom du channel sur lequel se déroule l'Event.
     */
    private final Channel channel;
    
    /**
     * @param channel1
     *            le channel
     */
    public ChannelEvent(final Channel channel1) {
        channel = channel1;
        if (LOG.isDebugEnabled()) {
            LOG.debug("start sur " + channel1);
        }
        CHANNEL_EVENTS.put(channel, this);
    }
    
    /**
     * @see net.mauhiz.irc.bot.event.IChannelEvent#getChannel()
     */
    public Channel getChannel() {
        return channel;
    }
    
    /**
     * @see net.mauhiz.irc.bot.event.IChannelEvent#stop()
     */
    public final String stop() {
        if (channel != null) {
            if (LOG.isDebugEnabled()) {
                LOG.debug("reset sur " + channel);
            }
            CHANNEL_EVENTS.remove(channel);
        }
        return "Fin du " + getClass().getSimpleName() + " !";
    }
    
    /**
     * @see java.lang.Object#toString()
     */
    @Override
    public abstract String toString();
}

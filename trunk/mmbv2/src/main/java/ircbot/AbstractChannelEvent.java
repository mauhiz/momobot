package ircbot;

import org.apache.log4j.Logger;

/**
 * @author mauhiz
 */
public abstract class AbstractChannelEvent implements IChannelEvent {
    /**
     * logger.
     */
    private static final Logger LOG = Logger.getLogger(AbstractChannelEvent.class);
    /**
     * le nom du channel sur lequel se déroule l'Event.
     */
    private final Channel       channel;

    /**
     * @param channel1
     *            le channel
     */
    public AbstractChannelEvent(final Channel channel1) {
        this.channel = channel1;
        if (LOG.isDebugEnabled()) {
            LOG.debug("start sur " + channel1);
        }
        this.channel.registerEvent(this);
    }

    /**
     * @see ircbot.IChannelEvent#getChannel()
     */
    public Channel getChannel() {
        return this.channel;
    }

    /**
     * @see ircbot.IChannelEvent#stop()
     */
    public final String stop() {
        if (getChannel() != null) {
            if (LOG.isDebugEnabled()) {
                LOG.debug("reset sur " + getChannel());
            }
            getChannel().unRegisterEvent();
        }
        return "Fin du " + getClass().getSimpleName() + " !";
    }

    /**
     * @see java.lang.Object#toString()
     */
    @Override
    public abstract String toString();
}

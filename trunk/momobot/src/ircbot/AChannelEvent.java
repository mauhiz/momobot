package ircbot;

import org.apache.log4j.Logger;

/**
 * @author Administrator
 */
public abstract class AChannelEvent {
    /**
     * logger.
     */
    private static final Logger LOG     = Logger.getLogger(AChannelEvent.class);
    /**
     * le nom du channel sur lequel se déroule l'Event.
     */
    private Channel             channel = null;

    /**
     * @param channel1
     *            le channel
     */
    public AChannelEvent(final Channel channel1) {
        this.channel = channel1;
        if (LOG.isDebugEnabled()) {
            LOG.debug("start sur " + channel1);
        }
        this.channel.registerEvent(this);
    }

    /**
     * @return un msg
     */
    public abstract String status();

    /**
     * @return un msg
     */
    public final String stop() {
        if (LOG.isDebugEnabled()) {
            LOG.debug("reset sur " + this.channel);
        }
        if (this.channel != null) {
            this.channel.unRegisterEvent();
        }
        return "Fin du " + getClass().getSimpleName() + " !";
    }
}

package net.mauhiz.irc.bot.event;

import net.mauhiz.irc.base.data.Channel;

/**
 * @author mauhiz
 */
public abstract class ChannelEvent implements IChannelEvent {
    
    /**
     * @param chan
     *            le channel
     */
    public ChannelEvent(final Channel chan) {
        chan.setEvent(this);
    }
    
    /**
     * @see net.mauhiz.irc.bot.event.IChannelEvent#stop()
     */
    public final String stop() {
        return "Fin du " + getClass().getSimpleName() + " !";
    }
    
    /**
     * @see java.lang.Object#toString()
     */
    @Override
    public abstract String toString();
}

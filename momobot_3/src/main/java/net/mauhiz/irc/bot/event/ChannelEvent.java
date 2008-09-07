package net.mauhiz.irc.bot.event;

import net.mauhiz.irc.base.data.AbstractHook;
import net.mauhiz.irc.base.data.IrcChannel;

/**
 * @author mauhiz
 */
public abstract class ChannelEvent extends AbstractHook<IrcChannel> implements IChannelEvent {
    /**
     * running
     */
    private boolean running = true;
    /**
     * @param chan
     *            le channel
     */
    public ChannelEvent(final IrcChannel chan) {
        super(chan);
    }
    
    /**
     * @see net.mauhiz.irc.bot.event.IChannelEvent#isRunning()
     */
    @Override
    public boolean isRunning() {
        return running;
    }
    
    /**
     * @see net.mauhiz.irc.bot.event.IChannelEvent#stop()
     */
    public final String stop() {
        running = false;
        return "Fin du " + getClass().getSimpleName() + " !";
    }
    
    /**
     * Status
     * 
     * @see java.lang.Object#toString()
     */
    @Override
    public abstract String toString();
}

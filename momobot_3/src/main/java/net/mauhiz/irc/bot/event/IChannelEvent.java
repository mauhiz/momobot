package net.mauhiz.irc.bot.event;

/**
 * @author mauhiz
 */
public interface IChannelEvent {
    boolean isRunning();
    
    /**
     * @return un msg
     */
    String stop();
}

package net.mauhiz.irc.bot.event;

/**
 * @author mauhiz
 */
public interface IChannelEvent {
    /**
     * @return running
     */
    boolean isRunning();
    
    /**
     * @return un msg
     */
    String stop();
}

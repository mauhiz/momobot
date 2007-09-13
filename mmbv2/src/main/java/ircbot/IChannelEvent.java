package ircbot;

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

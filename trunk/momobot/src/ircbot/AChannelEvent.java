package ircbot;

import utils.Utils;

/**
 * @author Administrator
 */
public abstract class AChannelEvent {
    /**
     * le nom du channel sur lequel se déroule l'Event.
     */
    private String channel = "";

    /**
     * @param channel1
     *            le channel
     */
    public AChannelEvent(final String channel1) {
        this.channel = channel1;
        Utils.log(getClass(), "start sur " + channel1);
        Channel.getChannel(channel1).registerEvent(this);
    }

    /**
     * @return un msg
     */
    public abstract String status();

    /**
     * @return un msg
     */
    public final String stop() {
        Utils.log(getClass(), "reset sur " + this.channel);
        Channel.getChannel(this.channel).unRegisterEvent();
        return "Fin du " + getClass().getSimpleName() + " !";
    }
}

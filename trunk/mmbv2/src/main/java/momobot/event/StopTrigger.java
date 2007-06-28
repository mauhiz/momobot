package momobot.event;

import ircbot.Channel;
import ircbot.IrcUser;
import ircbot.trigger.AbstractTrigger;
import ircbot.trigger.IPublicTrigger;
import momobot.MomoBot;

/**
 * @author mauhiz
 */
public class StopTrigger extends AbstractTrigger implements IPublicTrigger {
    /**
     * @param trigger
     *            le trigger
     */
    public StopTrigger(final String trigger) {
        super(trigger);
    }

    /**
     * @see ircbot.trigger.IPublicTrigger#executePublicTrigger(IrcUser, Channel, String)
     */
    @Override
    @SuppressWarnings("unused")
    public void executePublicTrigger(final IrcUser user, final Channel channel, final String message) {
        if (test(message) && channel.hasRunningEvent()) {
            MomoBot.getBotInstance().sendMessage(channel, channel.getEvent().stop());
        }
    }
}

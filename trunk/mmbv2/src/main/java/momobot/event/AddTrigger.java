package momobot.event;

import ircbot.Channel;
import ircbot.IChannelEvent;
import ircbot.IrcUser;
import ircbot.trigger.AbstractTrigger;
import ircbot.trigger.IPublicTrigger;
import momobot.MomoBot;

/**
 * @author mauhiz
 */
public class AddTrigger extends AbstractTrigger implements IPublicTrigger {
    /**
     * @param trigger
     *            le trigger
     */
    public AddTrigger(final String trigger) {
        super(trigger);
    }

    /**
     * @see ircbot.trigger.IPublicTrigger#executePublicTrigger(IrcUser, Channel, String)
     */
    public final void executePublicTrigger(final IrcUser user, final Channel channel, final String message) {
        if (!channel.hasRunningEvent()) {
            MomoBot.getBotInstance().sendMessage(channel, "Aucun gather ou pickup n'est lance.");
            return;
        }
        final IChannelEvent event = channel.getEvent();
        if (event instanceof Gather) {
            MomoBot.getBotInstance().sendMessage(channel, ((Gather) event).add(user));
        } else if (event instanceof Pickup) {
            MomoBot.getBotInstance().sendMessage(channel, ((Pickup) event).add(user, getArgs(message)));
        }
    }
}

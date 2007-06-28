package momobot.event;

import ircbot.AbstractChannelEvent;
import ircbot.Channel;
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
    @Override
    public final void executePublicTrigger(final IrcUser user, final Channel channel, final String message) {
        if (!test(message)) {
            return;
        }
        if (!channel.hasRunningEvent()) {
            MomoBot.getBotInstance().sendMessage(channel, "Aucun gather ou pickup n'est lance.");
            return;
        }
        final AbstractChannelEvent event = channel.getEvent();
        if (event instanceof Gather) {
            MomoBot.getBotInstance().sendMessage(channel, ((Gather) event).add(user).toString());
        } else if (event instanceof Pickup) {
            MomoBot.getBotInstance().sendMessage(channel, ((Pickup) event).add(user, getArgs(message)));
        }
    }
}

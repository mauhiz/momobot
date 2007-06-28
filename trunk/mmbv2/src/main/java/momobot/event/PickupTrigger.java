package momobot.event;

import ircbot.Channel;
import ircbot.IrcUser;
import ircbot.trigger.AbstractTrigger;
import ircbot.trigger.IPublicTrigger;
import momobot.MomoBot;

/**
 * @author mauhiz
 */
public class PickupTrigger extends AbstractTrigger implements IPublicTrigger {
    /**
     * @param trigger
     *            le trigger
     */
    public PickupTrigger(final String trigger) {
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
        if (channel.hasRunningEvent()) {
            MomoBot.getBotInstance().sendMessage(channel,
                    "Un " + channel.getEvent().getClass().getSimpleName() + " est déjà lancé sur " + channel);
        } else {
            new Pickup(channel);
            MomoBot.getBotInstance().sendMessage(channel, "Pickup lancé par " + user);
        }
    }
}

package momobot.event;

import ircbot.Channel;
import ircbot.IrcUser;
import ircbot.trigger.AbstractTrigger;
import ircbot.trigger.IPublicTrigger;
import momobot.MomoBot;

/**
 * @author mauhiz
 */
public class GatherTrigger extends AbstractTrigger implements IPublicTrigger {
    /**
     * @param trigger
     */
    public GatherTrigger(final String trigger) {
        super(trigger);
    }

    /**
     * @see ircbot.trigger.IPublicTrigger#executePublicTrigger(ircbot.IrcUser, ircbot.Channel, java.lang.String)
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
            MomoBot.getBotInstance().sendMessage(channel, "Gather lancé par " + user);
            new Gather(channel).add(user);
        }
    }
}

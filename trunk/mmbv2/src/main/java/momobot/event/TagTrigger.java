package momobot.event;

import ircbot.Channel;
import ircbot.IrcUser;
import ircbot.trigger.AbstractTrigger;
import ircbot.trigger.IPublicTrigger;
import momobot.MomoBot;

/**
 * @author mauhiz
 */
public class TagTrigger extends AbstractTrigger implements IPublicTrigger {
    /**
     * @param trigger
     *            le trigger
     */
    public TagTrigger(final String trigger) {
        super(trigger);
    }

    /**
     * @see ircbot.trigger.IPublicTrigger#executePublicTrigger(IrcUser, Channel, String)
     */
    @SuppressWarnings("unused")
    public final void executePublicTrigger(final IrcUser user, final Channel channel, final String message) {
        /* on test si un gather est lance sur le channel en question */
        if (channel.hasRunningEvent() && channel.getEvent() instanceof Gather) {
            MomoBot.getBotInstance().sendMessage(channel, ((Gather) channel.getEvent()).setTag(getArgs(message)));
        }
    }
}

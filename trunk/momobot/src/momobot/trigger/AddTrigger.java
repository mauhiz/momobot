package momobot.trigger;

import ircbot.Channel;
import ircbot.AChannelEvent;
import ircbot.IrcUser;
import ircbot.ATrigger;
import momobot.MomoBot;
import momobot.event.channel.Gather;
import momobot.event.channel.Pickup;

/**
 * @author Administrator
 */
public class AddTrigger extends ATrigger {
    /**
     * @param trigger
     *            le trigger
     */
    public AddTrigger(final String trigger) {
        super(trigger);
        setPublic(true);
    }

    /**
     * @see ircbot.ATrigger#executePrivateTrigger(ircbot.IrcUser,
     *      java.lang.String)
     */
    @Override
    public final void executePrivateTrigger(final IrcUser user,
            final String message) {
        // rien
    }

    /**
     * @see ircbot.ATrigger#executePublicTrigger(ircbot.IrcUser,
     *      java.lang.String, java.lang.String)
     * @param channel
     *            le channel
     */
    @Override
    public final void executePublicTrigger(final IrcUser user,
            final String channel, final String message) {
        final Channel c = Channel.getChannel(channel);
        if (!c.hasRunningEvent()) {
            MomoBot.getInstance().sendMessage(channel,
                    "Aucun gather ou pickup n'est lance.");
            return;
        }
        final AChannelEvent e = c.getEvent();
        if (e.getClass() == momobot.event.channel.Gather.class) {
            MomoBot.getInstance().sendMessage(channel,
                    ((Gather) e).add(user).toString());
            return;
        }
        if (e.getClass() == momobot.event.channel.Gather.class) {
            MomoBot.getInstance().sendMessage(channel,
                    ((Pickup) e).add(user, getArgs(message)));
            return;
        }
    }
}

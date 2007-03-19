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

public class RmvTrigger extends ATrigger {
    /**
     * @param trigger
     *            le trigger
     */
    public RmvTrigger(final String trigger) {
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
                    ((Gather) e).remove(user));
            return;
        }
        if (e.getClass() == momobot.event.channel.Pickup.class) {
            MomoBot.getInstance().sendMessage(channel,
                    ((Pickup) e).remove(user));
            return;
        }
    }
}

package momobot.trigger;

import ircbot.Channel;
import ircbot.AChannelEvent;
import ircbot.IrcUser;
import ircbot.ATrigger;
import momobot.MomoBot;
import momobot.event.channel.Gather;

/**
 * @author Administrator
 */

public class GatherTrigger extends ATrigger {
    /**
     * @param trigger
     *            le trigger
     */
    public GatherTrigger(final String trigger) {
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
            (new Gather(channel)).add(user);
            MomoBot.getInstance().sendMessage(channel,
                    "Gather lancé par " + user);
            return;
        }
        final AChannelEvent e = c.getEvent();
        if (e != null) {
            MomoBot.getInstance().sendMessage(
                    channel,
                    "Un " + e.getClass().getSimpleName()
                            + " est déjà lancé sur " + channel);
        }
    }
}

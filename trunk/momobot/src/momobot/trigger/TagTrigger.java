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

public class TagTrigger extends ATrigger {
    /**
     * @param trigger
     *            le trigger
     */
    public TagTrigger(final String trigger) {
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
        // on test si un gather est lance sur le channel en question
        final Channel c = Channel.getChannel(channel);
        if (!c.hasRunningEvent()) {
            return;
        }
        final AChannelEvent e = c.getEvent();
        if (e.getClass() == momobot.event.channel.Gather.class) {
            MomoBot.getInstance().sendMessage(channel,
                    ((Gather) e).setTag(getArgs(message)));
        }
    }
}

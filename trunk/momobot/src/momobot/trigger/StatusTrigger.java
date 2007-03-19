package momobot.trigger;

import ircbot.Channel;
import ircbot.AChannelEvent;
import ircbot.IrcUser;
import ircbot.ATrigger;
import momobot.MomoBot;

/**
 * @author Administrator
 */

public class StatusTrigger extends ATrigger {
    /**
     * @param trigger
     *            le trigger
     */
    public StatusTrigger(final String trigger) {
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
            MomoBot.getInstance().sendMessage(channel, "Il se passe rien :x");
            return;
        }
        final AChannelEvent e = c.getEvent();
        MomoBot.getInstance().sendMessage(channel, e.status());
        return;
    }
}

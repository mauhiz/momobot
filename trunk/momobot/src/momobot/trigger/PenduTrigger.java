package momobot.trigger;

import ircbot.Channel;
import ircbot.AChannelEvent;
import ircbot.IrcUser;
import ircbot.ATrigger;
import momobot.MomoBot;
import momobot.event.channel.Pendu;

/**
 * @author Administrator
 */
public class PenduTrigger extends ATrigger {
    /**
     * @param trigger
     *            le trigger
     */
    public PenduTrigger(final String trigger) {
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
                    "Devinez ce mot: " + (new Pendu(channel)).getDevinage());
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

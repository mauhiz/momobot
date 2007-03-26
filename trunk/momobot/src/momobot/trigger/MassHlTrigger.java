package momobot.trigger;

import ircbot.Channel;
import ircbot.IrcUser;
import ircbot.QnetUser;
import ircbot.ATrigger;
import java.util.Iterator;
import momobot.MomoBot;

/**
 * @author Administrator
 */
public class MassHlTrigger extends ATrigger {
    /**
     * @param trigger
     *            le trigger
     */
    public MassHlTrigger(final String trigger) {
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
        /* rien */
    }

    /**
     * @see ircbot.ATrigger#executePublicTrigger(ircbot.IrcUser,
     *      java.lang.String, java.lang.String)
     */
    @Override
    public final void executePublicTrigger(final IrcUser user,
            final String channel, final String message) {
        final StringBuffer msg = new StringBuffer("nudges");
        for (IrcUser nextIrcUser : Channel.getChannel(channel).getAllUsers()) {
            // no bots
            if (((QnetUser) nextIrcUser).iAmQnetService()) {
                continue;
            }
            // no self
            if (nextIrcUser.equals(user)
                    || nextIrcUser.getNick().equalsIgnoreCase(
                            MomoBot.getInstance().getNick())) {
                continue;
            }
            msg.append(' ').append(nextIrcUser);
        }
        MomoBot.getInstance().sendAction(channel, msg.toString());
    }
}

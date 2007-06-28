package momobot.event;

import ircbot.Channel;
import ircbot.IrcUser;
import ircbot.QnetUser;
import ircbot.trigger.AbstractTrigger;
import ircbot.trigger.IPublicTrigger;
import momobot.MomoBot;

import org.apache.commons.lang.text.StrBuilder;

/**
 * @author mauhiz
 */
@SuppressWarnings("unused")
public class MassHlTrigger extends AbstractTrigger implements IPublicTrigger {
    /**
     * @param trigger
     *            le trigger
     */
    public MassHlTrigger(final String trigger) {
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
        final StrBuilder msg = new StrBuilder("nudges");
        for (final IrcUser nextIrcUser : channel.getUsers()) {
            if (nextIrcUser instanceof QnetUser && ((QnetUser) nextIrcUser).iAmQnetService()) {
                /* no bots */
                continue;
            } else if (nextIrcUser.equals(user) ||
                    nextIrcUser.getNick().equalsIgnoreCase(MomoBot.getBotInstance().getNick())) {
                /* no self */
                continue;
            }
            msg.append(' ').append(nextIrcUser);
        }
        MomoBot.getBotInstance().sendAction(channel.getNom(), msg.toString());
    }
}

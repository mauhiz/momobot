package momobot.event;

import ircbot.Channel;
import ircbot.IrcUser;
import ircbot.QnetUser;
import ircbot.trigger.AbstractTrigger;
import ircbot.trigger.IPublicTrigger;
import momobot.MomoBot;

import org.apache.commons.lang.text.StrBuilder;
import org.apache.log4j.Logger;

/**
 * @author mauhiz
 */
@SuppressWarnings("unused")
public class MassHlTrigger extends AbstractTrigger implements IPublicTrigger {
    /**
     * 
     */
    private static final Logger LOG = Logger.getLogger(MassHlTrigger.class);

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
    public final void executePublicTrigger(final IrcUser user, final Channel channel, final String message) {
        int nbUsers = channel.getUsers().size();
        LOG.debug("MassHlTrigger : " + channel + " has " + nbUsers + " users");
        if (nbUsers == 0) {
            LOG.error("no users on channel " + channel);
            return;
        }
        final StrBuilder msg = new StrBuilder("nudges");
        for (final IrcUser nextIrcUser : channel.getUsers()) {
            if (nextIrcUser instanceof QnetUser && ((QnetUser) nextIrcUser).iAmQnetService()) {
                /* no bots */
                LOG.debug("skipping bot : " + nextIrcUser);
                continue;
            } else if (nextIrcUser.equals(user)
                    || nextIrcUser.getNick().equalsIgnoreCase(MomoBot.getBotInstance().getNick())) {
                /* no self */
                LOG.debug("skipping myself : " + nextIrcUser);
                continue;
            }
            LOG.debug("appending : " + nextIrcUser);
            msg.append(' ').append(nextIrcUser);
        }
        MomoBot.getBotInstance().sendAction(channel.getNom(), msg.toString());
    }
}

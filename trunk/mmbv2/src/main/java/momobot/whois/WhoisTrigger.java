package momobot.whois;

import ircbot.Channel;
import ircbot.IrcUser;
import ircbot.trigger.AbstractTrigger;
import ircbot.trigger.IJoinTrigger;
import ircbot.trigger.IPriveTrigger;
import ircbot.trigger.IPublicTrigger;

import java.util.Locale;

import momobot.MomoBot;

import org.apache.log4j.Logger;

/**
 * @author mauhiz
 */
public class WhoisTrigger extends AbstractTrigger implements IPublicTrigger, IPriveTrigger, IJoinTrigger {
    /**
     * 
     */
    private static final Logger LOG = Logger.getLogger(WhoisTrigger.class);

    /**
     * @param trigger
     *            le trigger
     */
    public WhoisTrigger(final String trigger) {
        super(trigger);
    }

    /**
     * j'évite de flooder en joinant clanwar!
     * 
     * @see ircbot.trigger.IJoinTrigger#executeJoinTrigger(ircbot.Channel, ircbot.IrcUser)
     */
    public void executeJoinTrigger(final Channel channel, final IrcUser user) {
        if (channel == null) {
            LOG.error("executeJoinTrigger : channel is null");
        } else if (user == null) {
            LOG.error("executeJoinTrigger : user is null");
        } else if (MomoBot.AUTOJOIN.contains(channel.getNom().toLowerCase(Locale.US))) {
            new Whois(user).execute();
        }
    }

    /**
     * @see ircbot.trigger.IPriveTrigger#executePrivateTrigger(IrcUser, String)
     */
    public final void executePrivateTrigger(final IrcUser user, final String message) {
        new Whois(getArgs(message), true, user).execute();
    }

    /**
     * @see ircbot.trigger.IPublicTrigger#executePublicTrigger(ircbot.IrcUser, ircbot.Channel, java.lang.String)
     */
    @SuppressWarnings("unused")
    public final void executePublicTrigger(final IrcUser user, final Channel channel, final String message) {
        new Whois(getArgs(message), false, user).execute();
    }
}

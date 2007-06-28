package momobot.whois;

import ircbot.Channel;
import ircbot.IrcUser;
import ircbot.trigger.AbstractTrigger;
import ircbot.trigger.IJoinTrigger;
import ircbot.trigger.IPriveTrigger;
import ircbot.trigger.IPublicTrigger;

import java.util.Locale;

import momobot.MomoBot;

/**
 * @author mauhiz
 */
public class WhoisTrigger extends AbstractTrigger implements IPublicTrigger, IPriveTrigger, IJoinTrigger {
    /**
     * @param trigger
     *            le trigger
     */
    public WhoisTrigger(final String trigger) {
        super(trigger);
    }

    /**
     * j'évite de flooder en joinant clanwar!
     * @see ircbot.trigger.IJoinTrigger#executeJoinTrigger(ircbot.Channel, ircbot.IrcUser)
     */
    @Override
    public void executeJoinTrigger(final Channel channel, final IrcUser user) {
        if (MomoBot.AUTOJOIN.contains(channel.getNom().toLowerCase(Locale.US))) {
            new Whois(user).execute();
        }
    }

    /**
     * @see ircbot.trigger.IPriveTrigger#executePrivateTrigger(IrcUser, String)
     */
    @Override
    public final void executePrivateTrigger(final IrcUser user, final String message) {
        if (!test(message)) {
            return;
        }
        new Whois(getArgs(message), true, user).execute();
    }

    /**
     * @see ircbot.trigger.IPublicTrigger#executePublicTrigger(ircbot.IrcUser, ircbot.Channel, java.lang.String)
     */
    @Override
    @SuppressWarnings("unused")
    public final void executePublicTrigger(final IrcUser user, final Channel channel, final String message) {
        if (!test(message)) {
            return;
        }
        new Whois(getArgs(message), false, user).execute();
    }
}

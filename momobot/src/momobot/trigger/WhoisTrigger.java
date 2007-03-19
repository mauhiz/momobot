package momobot.trigger;

import momobot.Whois;
import ircbot.IrcUser;
import ircbot.ATrigger;

/**
 * @author Administrator
 */
public class WhoisTrigger extends ATrigger {
    /**
     * @param trigger
     *            le trigger
     */
    public WhoisTrigger(final String trigger) {
        super(trigger);
        setPrive(true);
        setPublic(true);
    }

    /**
     * @see ircbot.ATrigger#executePrivateTrigger(ircbot.IrcUser,
     *      java.lang.String)
     */
    @Override
    public final void executePrivateTrigger(final IrcUser user,
            final String message) {
        new Whois(getArgs(message), true, user).execute();
    }

    /**
     * @see ircbot.ATrigger#executePublicTrigger(ircbot.IrcUser,
     *      java.lang.String, java.lang.String)
     */
    @Override
    public final void executePublicTrigger(final IrcUser user,
            final String channel, final String message) {
        new Whois(getArgs(message), false, user).execute();
    }
}

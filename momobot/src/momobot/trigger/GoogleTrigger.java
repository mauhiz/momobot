package momobot.trigger;

import ircbot.ATrigger;
import ircbot.IrcUser;
import momobot.MomoBot;
import momobot.WebQuery;

/**
 * @author Administrator
 */
public class GoogleTrigger extends ATrigger {
    /**
     * @param trigger
     *            le trigger
     */
    public GoogleTrigger(final String trigger) {
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
        final WebQuery wq = new WebQuery("google", getArgs(message));
        for (String next : wq.results()) {
            MomoBot.getInstance().sendNotice(user, next);
        }
    }
}

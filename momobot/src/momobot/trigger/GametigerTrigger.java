package momobot.trigger;

import ircbot.ATrigger;
import ircbot.IrcUser;
import momobot.MomoBot;
import momobot.WebQuery;

/**
 * @author Administrator
 */
public class GametigerTrigger extends ATrigger {
    /**
     * @param trigger
     *            le trigger
     */
    public GametigerTrigger(final String trigger) {
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
     * @param channel
     *            le channel
     * @param user
     *            le user
     * @see ircbot.ATrigger#executePublicTrigger(ircbot.IrcUser,
     *      java.lang.String, java.lang.String)
     */
    @Override
    public final void executePublicTrigger(final IrcUser user,
            final String channel, final String message) {
        final WebQuery wq = new WebQuery("gametiger", getArgs(message));
        boolean resultFound = false;
        for (String next : wq.results()) {
            resultFound = true;
            MomoBot.getInstance().sendNotice(user, next);
        }
        if (!resultFound) {
            MomoBot.getInstance().sendNotice(user, "rien trouve :/");
        }
    }
}

package momobot.trigger;

import ircbot.IrcUser;
import ircbot.ATrigger;
import momobot.MomoBot;

/**
 * @author Administrator
 */
public class PartTrigger extends ATrigger {
    /**
     * @param trigger
     *            le trigger
     */
    public PartTrigger(final String trigger) {
        super(trigger);
        setPrive(true);
        setOnlyAdmin(true);
    }

    /**
     * @see ircbot.ATrigger#executePrivateTrigger(ircbot.IrcUser,
     *      java.lang.String)
     */
    @Override
    public final void executePrivateTrigger(final IrcUser user,
            final String message) {
        MomoBot.getInstance().partChannel(getArgs(message));
    }

    /**
     * @see ircbot.ATrigger#executePublicTrigger(ircbot.IrcUser,
     *      java.lang.String, java.lang.String)
     */
    @Override
    public final void executePublicTrigger(final IrcUser user,
            final String channel, final String message) {
        // rien
    }
}

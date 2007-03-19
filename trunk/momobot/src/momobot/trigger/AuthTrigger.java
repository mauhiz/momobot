package momobot.trigger;

import ircbot.IrcUser;
import ircbot.ATrigger;
import momobot.MomoBot;

/**
 * @author Administrator
 */
public class AuthTrigger extends ATrigger {
    /**
     * @param trigger
     *            le trigger
     */
    public AuthTrigger(final String trigger) {
        super(trigger);
        setPrive(true);
    }

    /**
     * @see ircbot.ATrigger#executePrivateTrigger(ircbot.IrcUser,
     *      java.lang.String)
     */
    @Override
    public final void executePrivateTrigger(final IrcUser user,
            final String message) {
        // pour l'instant je hard code le pw. La secu attendra ;x
        if (getArgs(message).equals("boulz")) {
            user.setAdmin(true);
            MomoBot.getInstance().sendMessage(user.getNick(), "Oui, maître");
        }
    }

    /**
     * @see ircbot.ATrigger#executePublicTrigger(ircbot.IrcUser,
     *      java.lang.String, java.lang.String)
     * @param channel
     *            le channel
     */
    @Override
    public final void executePublicTrigger(final IrcUser user,
            final String channel, final String message) {
        // rien
    }
}

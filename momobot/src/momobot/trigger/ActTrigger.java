package momobot.trigger;

import ircbot.IrcUser;
import ircbot.ATrigger;
import momobot.MomoBot;

/**
 * @author Administrator
 */
public class ActTrigger extends ATrigger {
    /**
     * @param trigger
     *            le trigger
     */
    public ActTrigger(final String trigger) {
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
        final String s = getArgs(message);
        final int i = s.indexOf(" ");
        if (i < 1) {
            MomoBot.getInstance().sendMessage(user.getNick(),
                    "pas assez de paramètres.");
            MomoBot.getInstance().sendMessage(user.getNick(),
                    "syntaxe $" + getTriggerText() + " target msg");
        }
        MomoBot.getInstance().sendAction(s.substring(0, i), s.substring(i + 1));
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

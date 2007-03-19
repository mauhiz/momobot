package momobot.trigger;

import ircbot.IrcUser;
import ircbot.ATrigger;
import momobot.MomoBot;

/**
 * @author Administrator
 */
public class SayTrigger extends ATrigger {
    /**
     * @param trigger
     *            le trigger
     */
    public SayTrigger(final String trigger) {
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
                    "syntaxe $say target msg");
        }
        MomoBot.getInstance()
                .sendMessage(s.substring(0, i), s.substring(i + 1));
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

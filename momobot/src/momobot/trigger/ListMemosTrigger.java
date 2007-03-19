package momobot.trigger;

import ircbot.IrcUser;
import ircbot.ATrigger;
import momobot.Db;
import momobot.MomoBot;

/**
 * @author Administrator
 */
public class ListMemosTrigger extends ATrigger {
    /**
     * @param trigger
     *            le trigger
     */
    public ListMemosTrigger(final String trigger) {
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
        // rien
    }

    /**
     * @see ircbot.ATrigger#executePublicTrigger(ircbot.IrcUser,
     *      java.lang.String, java.lang.String)
     */
    @Override
    public final void executePublicTrigger(final IrcUser user,
            final String channel, final String message) {
        MomoBot.getInstance().sendMessage(channel, Db.getMemos());
    }
}

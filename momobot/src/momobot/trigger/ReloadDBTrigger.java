package momobot.trigger;

import ircbot.IrcUser;
import ircbot.ATrigger;
import momobot.Db;
import momobot.MomoBot;
import utils.Utils;

/**
 * @author Administrator
 */
public class ReloadDBTrigger extends ATrigger {
    /**
     * @param trigger
     *            le trigger
     */
    public ReloadDBTrigger(final String trigger) {
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
        try {
            Db.loadMemoDB();
            Db.loadPlayerDB();
            Db.reloadTriggers();
            MomoBot.getInstance().sendMessage(user.getNick(), "DB reloaded");
        } catch (final Exception e) {
            MomoBot.getInstance().sendMessage(user.getNick(),
                    "Erreur " + e.getMessage());
            Utils.logError(getClass(), e);
        }
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

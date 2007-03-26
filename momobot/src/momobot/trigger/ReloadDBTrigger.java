package momobot.trigger;

import ircbot.ATrigger;
import ircbot.IrcUser;
import momobot.Db;
import momobot.MomoBot;
import org.apache.log4j.Logger;

/**
 * @author Administrator
 */
public class ReloadDBTrigger extends ATrigger {
    /**
     * logger.
     */
    private static final Logger LOG = Logger.getLogger(ReloadDBTrigger.class);

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
                    "Erreur : " + e.getMessage());
            if (LOG.isErrorEnabled()) {
                LOG.error(e, e);
            }
        }
    }

    /**
     * @see ircbot.ATrigger#executePublicTrigger(ircbot.IrcUser,
     *      java.lang.String, java.lang.String)
     */
    @Override
    public final void executePublicTrigger(final IrcUser user,
            final String channel, final String message) {
        /* rien */
    }
}

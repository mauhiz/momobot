package momobot.trigger.admin;

import ircbot.IrcUser;
import ircbot.trigger.AbstractTrigger;
import ircbot.trigger.IAdminTrigger;
import ircbot.trigger.IPriveTrigger;

import java.sql.SQLException;

import momobot.MomoBot;
import momobot.SqlUtils;
import momobot.memo.DbMemoUtils;

import org.apache.log4j.Logger;

/**
 * @author mauhiz
 */
public class ReloadDBTrigger extends AbstractTrigger implements IPriveTrigger, IAdminTrigger {
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
    }

    /**
     * @see ircbot.trigger.IPriveTrigger#executePrivateTrigger(IrcUser, String)
     */
    @SuppressWarnings("unused")
    public final void executePrivateTrigger(final IrcUser user, final String message) {
        try {
            DbMemoUtils.loadMemoDB();
            SqlUtils.loadPlayerDB();
            SqlUtils.reloadTriggers();
            MomoBot.getBotInstance().sendMessage(user, "DB reloaded");
        } catch (final SQLException sqle) {
            LOG.error(sqle, sqle);
            MomoBot.getBotInstance().sendMessage(user, "Erreur : " + sqle);
        }
    }
}

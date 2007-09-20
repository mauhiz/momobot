package momobot.trigger.admin;

import ircbot.IrcUser;
import ircbot.trigger.AbstractTrigger;
import ircbot.trigger.IAdminTrigger;
import ircbot.trigger.IPriveTrigger;

import java.util.Map.Entry;

import momobot.MomoBot;
import momobot.SqlUtils;

/**
 * @author mauhiz
 */
public class KnownPlayersTrigger extends AbstractTrigger implements IPriveTrigger, IAdminTrigger {
    /**
     * @param trigger
     *            le trigger
     */
    public KnownPlayersTrigger(final String trigger) {
        super(trigger);
    }

    /**
     * @see ircbot.trigger.IPriveTrigger#executePrivateTrigger(ircbot.IrcUser, java.lang.String)
     */
    public final void executePrivateTrigger(final IrcUser user, final String message) {
        MomoBot.getBotInstance().sendMessage(user, "Je connais " + SqlUtils.countPlayers() + " joueur(s).");
        for (final Entry<String, String> item : SqlUtils.getPlayers()) {
            MomoBot.getBotInstance().sendMessage(user, item.getValue() + " - " + item.getKey());
        }
    }
}

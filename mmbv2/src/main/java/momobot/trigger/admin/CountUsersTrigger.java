package momobot.trigger.admin;

import ircbot.IrcUser;
import ircbot.trigger.AbstractTrigger;
import ircbot.trigger.IAdminTrigger;
import ircbot.trigger.IPriveTrigger;
import momobot.MomoBot;

/**
 * @author mauhiz
 */
public class CountUsersTrigger extends AbstractTrigger implements IAdminTrigger, IPriveTrigger {
    /**
     * @param trigger
     *            le nom du trigger
     */
    public CountUsersTrigger(final String trigger) {
        super(trigger);
    }

    /**
     * @see ircbot.trigger.IPriveTrigger#executePrivateTrigger(IrcUser, String)
     */
    public final void executePrivateTrigger(final IrcUser user, final String message) {
        MomoBot.getBotInstance().sendMessage(user, "Je connais " + IrcUser.countUsers() + " utilisateur(s).");
    }
}

package momobot.automate;

import ircbot.IrcUser;
import ircbot.trigger.AbstractTrigger;
import ircbot.trigger.IPriveTrigger;
import momobot.MomoBot;

/**
 * @author mauhiz
 */
public class AuthTrigger extends AbstractTrigger implements IPriveTrigger {
    /**
     * @param trigger
     *            le trigger
     */
    public AuthTrigger(final String trigger) {
        super(trigger);
    }

    /**
     * @see ircbot.trigger.IPriveTrigger#executePrivateTrigger(IrcUser, String)
     */
    @Override
    public final void executePrivateTrigger(final IrcUser user, final String message) {
        if (!test(message)) {
            return;
        }
        /* pour l'instant je hard code le pw. La sécu attendra ;x */
        if (getArgs(message).equals("boulz")) {
            user.setAdmin(true);
            MomoBot.getBotInstance().sendMessage(user, "Oui, maître");
        }
    }
}

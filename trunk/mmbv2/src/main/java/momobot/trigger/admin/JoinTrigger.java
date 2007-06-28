package momobot.trigger.admin;

import ircbot.IrcUser;
import ircbot.trigger.AbstractTrigger;
import ircbot.trigger.IAdminTrigger;
import ircbot.trigger.IPriveTrigger;
import momobot.MomoBot;

/**
 * @author mauhiz
 */
public class JoinTrigger extends AbstractTrigger implements IPriveTrigger, IAdminTrigger {
    /**
     * @param trigger
     *            le trigger
     */
    public JoinTrigger(final String trigger) {
        super(trigger);
    }

    /**
     * @see ircbot.trigger.IPriveTrigger#executePrivateTrigger(IrcUser, String)
     */
    @Override
    @SuppressWarnings("unused")
    public final void executePrivateTrigger(final IrcUser user, final String message) {
        if (!test(message)) {
            MomoBot.getBotInstance().joinChannel(getArgs(message));
        }
    }
}

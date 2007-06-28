package momobot.trigger.admin;

import ircbot.Channel;
import ircbot.IrcUser;
import ircbot.trigger.AbstractTrigger;
import ircbot.trigger.IAdminTrigger;
import ircbot.trigger.IPriveTrigger;
import momobot.MomoBot;

/**
 * @author mauhiz
 */
public class PartTrigger extends AbstractTrigger implements IPriveTrigger, IAdminTrigger {
    /**
     * @param trigger
     *            le trigger
     */
    public PartTrigger(final String trigger) {
        super(trigger);
    }

    /**
     * @see ircbot.trigger.IPriveTrigger#executePrivateTrigger(ircbot.IrcUser, java.lang.String)
     */
    @Override
    @SuppressWarnings("unused")
    public final void executePrivateTrigger(final IrcUser user, final String message) {
        if (!test(message)) {
            return;
        }
        MomoBot.getBotInstance().partChannel(Channel.getChannel(getArgs(message)));
    }
}

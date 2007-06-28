package momobot.trigger;

import ircbot.IrcUser;
import ircbot.trigger.AbstractTrigger;
import ircbot.trigger.IAdminTrigger;
import ircbot.trigger.IPriveTrigger;
import momobot.MomoBot;

/**
 * @author mauhiz
 */
public class ActTrigger extends AbstractTrigger implements IAdminTrigger, IPriveTrigger {
    /**
     * @param trigger
     *            le trigger
     */
    public ActTrigger(final String trigger) {
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
        final String args = getArgs(message);
        final int index = args.indexOf(' ');
        if (index < 1) {
            MomoBot.getBotInstance().sendMessage(user, "pas assez de paramètres.");
            MomoBot.getBotInstance().sendMessage(user, "syntaxe $" + getTriggerText() + " target msg");
        } else {
            MomoBot.getBotInstance().sendAction(args.substring(0, index), args.substring(index + 1));
        }
    }
}

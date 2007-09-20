package momobot.trigger.admin;

import ircbot.IrcUser;
import ircbot.trigger.AbstractTrigger;
import ircbot.trigger.IAdminTrigger;
import ircbot.trigger.IPriveTrigger;
import momobot.MomoBot;

import org.apache.commons.lang.StringUtils;

/**
 * @author mauhiz
 */
public class QuitTrigger extends AbstractTrigger implements IPriveTrigger, IAdminTrigger {
    /**
     * @param trigger
     *            le trigger
     */
    public QuitTrigger(final String trigger) {
        super(trigger);
    }

    /**
     * @see IPriveTrigger#executePrivateTrigger(IrcUser, String)
     */
    public final void executePrivateTrigger(final IrcUser user, final String message) {
        final String args = getArgs(message);
        if (StringUtils.isEmpty(args)) {
            MomoBot.getBotInstance().quitServer("Requested by " + user);
        } else {
            MomoBot.getBotInstance().quitServer(args);
        }
    }
}

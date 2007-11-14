package momobot.spam;

import ircbot.Channel;
import ircbot.IrcUser;
import ircbot.trigger.AbstractTrigger;
import ircbot.trigger.IPublicTrigger;

/**
 * @author mauhiz
 */
public class SpamTrigger extends AbstractTrigger implements IPublicTrigger {
    /**
     * @param trigger
     */
    public SpamTrigger(final String trigger) {
        super(trigger);
    }

    /**
     * @see ircbot.trigger.IPublicTrigger#executePublicTrigger(ircbot.IrcUser, ircbot.Channel, java.lang.String)
     */
    public void executePublicTrigger(final IrcUser from, final Channel channel, final String message) {
        // TODO
    }
}

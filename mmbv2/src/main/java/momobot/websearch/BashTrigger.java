package momobot.websearch;

import ircbot.Channel;
import ircbot.IrcUser;
import ircbot.trigger.AbstractTrigger;
import ircbot.trigger.IPublicTrigger;

/**
 * @author mauhiz
 */
public class BashTrigger extends AbstractTrigger implements IPublicTrigger {
    /**
     * @param trigger
     */
    public BashTrigger(final String trigger) {
        super(trigger);
    }

    /**
     * ok. TODO http://www.bash.org/?random
     * @see IPublicTrigger#executePublicTrigger(ircbot.IrcUser, ircbot.Channel, java.lang.String)
     */
    @Override
    @SuppressWarnings("unused")
    public void executePublicTrigger(final IrcUser user, final Channel channel, final String message) {
    }
}

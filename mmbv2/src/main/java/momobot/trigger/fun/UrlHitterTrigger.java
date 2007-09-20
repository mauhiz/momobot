package momobot.trigger.fun;

import ircbot.Channel;
import ircbot.IrcUser;
import ircbot.trigger.AbstractTrigger;
import ircbot.trigger.IPublicTrigger;

/**
 * @author mauhiz
 */
public class UrlHitterTrigger extends AbstractTrigger implements IPublicTrigger {
    /**
     * @param trigger
     */
    public UrlHitterTrigger(final String trigger) {
        super(trigger);
    }

    /**
     * TODO ajouter un système URL hitter.
     * 
     * @see IPublicTrigger#executePublicTrigger(IrcUser, Channel, String)
     */
    public void executePublicTrigger(final IrcUser from, final Channel channel, final String message) {
    }
}

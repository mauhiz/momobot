package momobot.trigger;

import ircbot.Channel;
import ircbot.IrcUser;
import ircbot.trigger.AbstractTrigger;
import ircbot.trigger.IPublicTrigger;
import momobot.MomoBot;

import org.apache.commons.lang.StringUtils;

/**
 * En voilà un trigger qu'il est useless.
 * @author mauhiz
 */
public class LengthTrigger extends AbstractTrigger implements IPublicTrigger {
    /**
     * @param trigger
     */
    public LengthTrigger(final String trigger) {
        super(trigger);
    }

    /**
     * @see ircbot.trigger.IPublicTrigger#executePublicTrigger(ircbot.IrcUser, ircbot.Channel, java.lang.String)
     */
    @Override
    @SuppressWarnings("unused")
    public void executePublicTrigger(final IrcUser user, final Channel channel, final String message) {
        if (!test(message)) {
            return;
        }
        final String args = getArgs(message);
        if (null == args || StringUtils.isEmpty(args.trim())) {
            return;
        }
        MomoBot.getBotInstance().sendMessage(channel, "longueur = " + args.length());
    }
}

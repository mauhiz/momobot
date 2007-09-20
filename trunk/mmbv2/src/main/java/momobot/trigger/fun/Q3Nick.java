package momobot.trigger.fun;

import ircbot.Channel;
import ircbot.IrcUser;
import ircbot.trigger.AbstractTrigger;
import ircbot.trigger.IPublicTrigger;
import momobot.MomoBot;

/**
 * @author mauhiz
 */
public class Q3Nick extends AbstractTrigger implements IPublicTrigger {
    /**
     * TODO cette méthode.
     * 
     * @param args
     * @return un nick q3
     */
    private static String createq3nick(final String args) {
        throw new UnsupportedOperationException("TODO");
    }

    /**
     * @param trigger
     */
    public Q3Nick(final String trigger) {
        super(trigger);
    }

    /**
     * @see ircbot.trigger.IPublicTrigger#executePublicTrigger(ircbot.IrcUser, ircbot.Channel, java.lang.String)
     */
    @SuppressWarnings("unused")
    public void executePublicTrigger(final IrcUser from, final Channel channel, final String message) {
        MomoBot.getBotInstance().sendMessage(channel, createq3nick(getArgs(message)));
    }
}

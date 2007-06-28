package momobot.websearch;

import ircbot.Channel;
import ircbot.IrcUser;
import ircbot.trigger.AbstractTrigger;
import ircbot.trigger.IPublicTrigger;
import momobot.MomoBot;

/**
 * @author mauhiz
 */
public class GoogleTrigger extends AbstractTrigger implements IPublicTrigger {
    /**
     * @param trigger
     *            le trigger
     */
    public GoogleTrigger(final String trigger) {
        super(trigger);
    }

    /**
     * @see IPublicTrigger#executePublicTrigger(IrcUser, Channel, String)
     */
    @Override
    @SuppressWarnings("unused")
    public final void executePublicTrigger(final IrcUser user, final Channel channel, final String message) {
        if (!test(message)) {
            return;
        }
        final WebQuery query = new WebQuery("google", getArgs(message));
        for (final String next : query.results()) {
            MomoBot.getBotInstance().sendNotice(user, next);
        }
    }
}

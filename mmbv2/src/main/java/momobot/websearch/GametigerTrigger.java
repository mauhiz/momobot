package momobot.websearch;

import ircbot.Channel;
import ircbot.IrcUser;
import ircbot.trigger.AbstractTrigger;
import ircbot.trigger.IPublicTrigger;

import java.util.List;

import momobot.MomoBot;

/**
 * @author mauhiz
 */
public class GametigerTrigger extends AbstractTrigger implements IPublicTrigger {
    /**
     * @param trigger
     *            le trigger
     */
    public GametigerTrigger(final String trigger) {
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
        final WebQuery query = new WebQuery("gametiger", getArgs(message));
        final List < String > resultats = query.results();
        if (resultats.isEmpty()) {
            MomoBot.getBotInstance().sendNotice(user, "rien trouvé :/");
            return;
        }
        for (final String next : resultats) {
            MomoBot.getBotInstance().sendNotice(user, next);
        }
    }
}

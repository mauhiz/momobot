package momobot.trigger.fun;

import ircbot.Channel;
import ircbot.IrcUser;
import ircbot.trigger.AbstractTrigger;
import ircbot.trigger.IPublicTrigger;
import momobot.MomoBot;

import org.apache.commons.lang.math.RandomUtils;
import org.apache.commons.lang.text.StrBuilder;

/**
 * @author mauhiz
 */
public class MargueriteTrigger extends AbstractTrigger implements IPublicTrigger {
    /**
     * 
     */
    private static final String[] MARGUERITE = { "un peu", "beaucoup", "passionément", "à la folie", "pas du tout", };

    /**
     * @param trigger
     */
    public MargueriteTrigger(final String trigger) {
        super(trigger);
    }

    /**
     * @see IPublicTrigger#executePublicTrigger(IrcUser, Channel, String)
     */
    @SuppressWarnings("unused")
    public void executePublicTrigger(final IrcUser from, final Channel channel, final String message) {
        final String nom = getArgs(message);
        MomoBot.getBotInstance().sendMessage(channel, generateResponse(nom));
    }

    /**
     * @param nom
     * @return un messsage
     */
    protected String generateResponse(final String nom) {
        return new StrBuilder(nom).append(" t'aime ").append(MARGUERITE[RandomUtils.nextInt(5)]).append('.').toString();
    }
}

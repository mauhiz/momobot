package momobot.trigger;

import java.util.Locale;

import ircbot.Channel;
import ircbot.IrcUser;
import ircbot.trigger.AbstractTrigger;
import ircbot.trigger.IJoinTrigger;
import momobot.MomoBot;

/**
 * @author mauhiz
 */
public class WelcomeTrigger extends AbstractTrigger implements IJoinTrigger {
    /**
     * @param trigger
     */
    public WelcomeTrigger(final String trigger) {
        super(trigger);
    }

    /**
     * @see ircbot.trigger.IJoinTrigger#executeJoinTrigger(ircbot.Channel, ircbot.IrcUser)
     */
    @Override
    public void executeJoinTrigger(final Channel channel, final IrcUser user) {
        if (MomoBot.AUTOJOIN.contains(channel.getNom().toLowerCase(Locale.US))) {
            MomoBot.getBotInstance().sendNotice(user, "Bienvenue sur " + channel);
        }
    }
}

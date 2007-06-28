package momobot.event;

import ircbot.Channel;
import ircbot.IrcUser;
import ircbot.trigger.AbstractTrigger;
import ircbot.trigger.IPublicTrigger;

import java.util.Locale;

import momobot.MomoBot;

/**
 * @author mauhiz
 */
public class PenduTrigger extends AbstractTrigger implements IPublicTrigger {
    /**
     * @param trigger
     *            le trigger
     */
    public PenduTrigger(final String trigger) {
        super(trigger);
    }

    /**
     * @see ircbot.trigger.IPublicTrigger#executePublicTrigger(ircbot.IrcUser, ircbot.Channel, java.lang.String)
     */
    @SuppressWarnings("unused")
    public final void executePublicTrigger(final IrcUser user, final Channel channel, final String message) {
        if (test(message)) {
            if (channel.hasRunningEvent()) {
                MomoBot.getBotInstance().sendMessage(channel,
                        "Un " + channel.getEvent().getClass().getSimpleName() + " est déjà lancé sur " + channel);
            } else {
                MomoBot.getBotInstance().sendMessage(channel, "Devinez ce mot: " + new Pendu(channel).getDevinage());
            }
        }
        if (channel.getEvent() instanceof Pendu) {
            final Pendu pendu = (Pendu) channel.getEvent();
            if (message.length() == 1) {
                MomoBot.getBotInstance().sendMessage(channel,
                        pendu.submitLettre(message.toLowerCase(Locale.FRANCE).charAt(0)).toString());
            } else {
                MomoBot.getBotInstance().sendMessage(channel, pendu.submitMot(message).toString());
            }
        }
    }
}

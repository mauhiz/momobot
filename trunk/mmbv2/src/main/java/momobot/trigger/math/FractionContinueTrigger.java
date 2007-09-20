package momobot.trigger.math;

import ircbot.Channel;
import ircbot.IrcUser;
import ircbot.trigger.AbstractTrigger;
import ircbot.trigger.IPublicTrigger;
import momobot.MomoBot;

/**
 * En voilà un trigger qu'il est useless.
 * 
 * @author mauhiz
 */
public class FractionContinueTrigger extends AbstractTrigger implements IPublicTrigger {
    /**
     * @param trigger
     */
    public FractionContinueTrigger(final String trigger) {
        super(trigger);
    }

    /**
     * @see ircbot.trigger.IPublicTrigger#executePublicTrigger(ircbot.IrcUser, ircbot.Channel, java.lang.String)
     */
    @SuppressWarnings("unused")
    public void executePublicTrigger(final IrcUser user, final Channel channel, final String message) {
        final String args = getArgs(message);
        try {
            final double nombre = Double.parseDouble(args);
            final String fraction = computeFractionContinue(nombre);
            MomoBot.getBotInstance().sendMessage(channel, "fraction continue de " + nombre + ": " + fraction);
        } catch (NumberFormatException nfe) {
            MomoBot.getBotInstance().sendMessage(channel, "l'argument doit etre un nombre (reçu : " + args + ")");
        }
    }

    /**
     * TODO .
     * @param nombre
     * @return une fraction continue de type [a0; a1, a2, ...]
     */
    private static String computeFractionContinue(final double nombre) {
        return null;
    }
}

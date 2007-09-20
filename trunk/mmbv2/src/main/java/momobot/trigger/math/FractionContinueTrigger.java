package momobot.trigger.math;

import ircbot.Channel;
import ircbot.IrcUser;
import ircbot.trigger.AbstractTrigger;
import ircbot.trigger.IPublicTrigger;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import momobot.MomoBot;

import org.apache.commons.lang.text.StrBuilder;
import org.apache.log4j.Logger;

/**
 * En voilà un trigger qu'il est useless.
 * 
 * @author mauhiz
 */
public class FractionContinueTrigger extends AbstractTrigger implements IPublicTrigger {
    /**
     * 
     */
    static final int            LIMIT = 10;
    /**
     * 
     */
    static final Logger LOG   = Logger.getLogger(FractionContinueTrigger.class);

    /**
     * TODO : un critère d'arrêt basé sur epsilon.
     * 
     * @param nombre
     * @return une fraction continue de type [a0; a1, a2, ...]
     */
    static String computeFractionContinue(final double nombre) {
        final List < Integer > fraction = new LinkedList < Integer >();
        int floor;
        double work = nombre;
        // LOG.debug("limit = " + LIMIT);
        int count = 0;
        while (count < LIMIT) {
            // LOG.debug("work = " + work);
            floor = (int) Math.floor(work);
            if (floor == Integer.MAX_VALUE) {
                count = LIMIT + 1;
                break;
            }
            // LOG.debug("floor = " + floor);
            fraction.add(Integer.valueOf(floor));
            work = work - floor;
            // LOG.debug("work = " + work);
            if (work == 0) {
                count = LIMIT + 1;
                break;
            }
            work = 1 / work;
            ++count;
        }
        if (count > LIMIT) {
            return displayFraction(fraction, true);
        }
        return displayFraction(fraction, false);
    }

    /**
     * @param fraction
     * @return l'affichage de la fraction continue;
     */
    static String displayFraction(final List < Integer > fraction, final boolean exactMatch) {
        final List < Integer > safeCopy = new ArrayList < Integer >(fraction.size());
        safeCopy.addAll(fraction);
        final StrBuilder retour = new StrBuilder("[");
        if (safeCopy.isEmpty()) {
            retour.append("0]");
        } else {
            retour.append(safeCopy.remove(0));
            if (safeCopy.isEmpty()) {
                retour.append(']');
            } else {
                retour.append("; ");
                for (final Integer fracItem : safeCopy) {
                    retour.append(fracItem);
                    retour.append(", ");
                }
                if (exactMatch) {
                    retour.setLength(retour.length() - 2);
                } else {
                    retour.append("...");
                }
                retour.append(']');
            }
        }
        return retour.toString();
    }

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
}

package net.mauhiz.irc.bot.triggers.math;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import net.mauhiz.irc.base.IIrcControl;
import net.mauhiz.irc.base.msg.Privmsg;
import net.mauhiz.irc.bot.triggers.AbstractTextTrigger;
import net.mauhiz.irc.bot.triggers.IPrivmsgTrigger;

import org.apache.commons.lang.text.StrBuilder;
import org.apache.log4j.Logger;

/**
 * En voilà un trigger qu'il est useless.
 * 
 * @author mauhiz
 */
public class FractionContinueTrigger extends AbstractTextTrigger implements IPrivmsgTrigger {
    /**
     * 
     */
    static final int LIMIT = 10;
    /**
     * 
     */
    static final Logger LOG = Logger.getLogger(FractionContinueTrigger.class);
    
    /**
     * TODO : un critère d'arrêt basé sur epsilon.
     * 
     * @param nombre
     * @return une fraction continue de type [a0; a1, a2, ...]
     */
    static String computeFractionContinue(final double nombre) {
        final List<Integer> fraction = new LinkedList<Integer>();
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
     * @param exactMatch
     * @return l'affichage de la fraction continue;
     */
    static String displayFraction(final List<Integer> fraction, final boolean exactMatch) {
        final List<Integer> safeCopy = new ArrayList<Integer>(fraction.size());
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
     * @see net.mauhiz.irc.bot.triggers.IPrivmsgTrigger#doTrigger(net.mauhiz.irc.base.msg.Privmsg,
     *      net.mauhiz.irc.base.IIrcControl)
     */
    @Override
    public void doTrigger(final Privmsg cme, final IIrcControl control) {
        final String args = getArgs(cme.getMessage());
        Privmsg resp;
        try {
            final double nombre = Double.parseDouble(args);
            final String fraction = computeFractionContinue(nombre);
            resp = Privmsg.buildAnswer(cme, "fraction continue de " + nombre + ": " + fraction);
        } catch (NumberFormatException nfe) {
            resp = Privmsg.buildAnswer(cme, "l'argument doit etre un nombre (reçu : " + args + ")");
        }
        control.sendMsg(resp);
        
    }
}

package net.mauhiz.irc.bot.triggers.fun;

import net.mauhiz.irc.base.IIrcControl;
import net.mauhiz.irc.base.data.IrcUser;
import net.mauhiz.irc.base.msg.Privmsg;
import net.mauhiz.irc.base.trigger.IPrivmsgTrigger;
import net.mauhiz.irc.bot.triggers.AbstractTextTrigger;

import org.apache.commons.lang.math.RandomUtils;

/**
 * Un trigger pour lancer les des !
 * 
 * @author abby
 */
public class RollTheDiceTrigger extends AbstractTextTrigger implements IPrivmsgTrigger {
    /**
     * Constructeur
     * 
     * @param trigger
     */
    public RollTheDiceTrigger(String trigger) {
        super(trigger);
    }

    /**
     * @see net.mauhiz.irc.base.trigger.IPrivmsgTrigger#doTrigger(Privmsg, IIrcControl)
     */
    @Override
    public void doTrigger(Privmsg im, IIrcControl control) {
        String args = getTriggerContent(im);
        boolean engueuler = false;

        // 1000 par defaut;
        int defaultmax = 1000;
        int max;
        try {
            // Pour pas se faire lamer
            if (args.length() > 6) {
                engueuler = true;
            }
            // 
            max = Integer.parseInt(args);
        } catch (NumberFormatException e) {

            max = defaultmax;
        }

        // Fourchette arbitraire
        if (max <= 1 || max > 10000) {
            engueuler = true;
        }

        // Recupere le nom du joueur
        IrcUser user = (IrcUser) im.getFrom();

        // Un parametre est incorrect, on s'arrete la avec un message d'erreur.
        if (engueuler) {
            Privmsg msg = new Privmsg(
                    im,
                    user.getNick()
                            + ", me prend pas pour un con, je suis quand meme le momobot, et je lance les des entre 2 et 10000.");
            control.sendMsg(msg);
            return;
        }

        // random de base
        int diceRoll = RandomUtils.nextInt(max) + 1;

        // Petits commentaires futes
        String commentaire = getCommentaire(diceRoll, max);

        Privmsg msg = new Privmsg(im, commentaire + user.getNick() + " a obtenu un " + diceRoll + " (sur " + max + ')');
        control.sendMsg(msg);
    }

    private String getCommentaire(int diceRoll, int max) {
        if (diceRoll == max) {
            return "Quelle chance ! ";
        } else if (diceRoll == 1) {
            return "C'est vraiment pas son jour : ";
        } else if (diceRoll == 1337) {
            return "OMG leet lance ! ";
        } else if (diceRoll == 666) {
            return "Vade Retro, Satan ! ";
        } else if (diceRoll / (double) max > 0.8) {
            return "Pas trop mal, ";
        } else {
            return "";
        }
    }
}

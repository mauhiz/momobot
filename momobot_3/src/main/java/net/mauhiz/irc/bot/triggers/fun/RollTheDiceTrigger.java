package net.mauhiz.irc.bot.triggers.fun;

import net.mauhiz.irc.base.IIrcControl;
import net.mauhiz.irc.base.data.IrcUser;
import net.mauhiz.irc.base.data.Mask;
import net.mauhiz.irc.base.model.Users;
import net.mauhiz.irc.base.msg.Privmsg;
import net.mauhiz.irc.bot.triggers.AbstractTextTrigger;
import net.mauhiz.irc.bot.triggers.IPrivmsgTrigger;

import org.apache.commons.lang.math.RandomUtils;

/**
 * Un trigger pour lancer les dés !
 * 
 * @author abby
 */
public class RollTheDiceTrigger extends AbstractTextTrigger implements IPrivmsgTrigger {
    /**
     * Constructeur
     * 
     * @param trigger
     */
    public RollTheDiceTrigger(final String trigger) {
        super(trigger);
    }
    
    /**
     * @see net.mauhiz.irc.bot.triggers.IPrivmsgTrigger#doTrigger(net.mauhiz.irc.base.msg.Privmsg,
     *      net.mauhiz.irc.base.IIrcControl)
     */
    @Override
    public void doTrigger(final Privmsg im, final IIrcControl control) {
        // Récupère le nom du joueur
        IrcUser user = Users.getInstance(im.getServer()).findUser(new Mask(im.getFrom()), true);
        boolean engueuler = false;
        
        // 1000 par défaut;
        int defaultmax = 1000;
        int max;
        try {
            // Pour pas se faire lamer
            if (getArgs(im.getMessage()).length() > 6) {
                engueuler = true;
            }
            // 
            max = Integer.parseInt(getArgs(im.getMessage()));
        } catch (NumberFormatException e) {
            
            max = defaultmax;
        }
        
        // Fourchette arbitraire
        if (max <= 1 || max > 10000) {
            engueuler = true;
        }
        
        // Un paramètre est incorrect, on s'arrete là avec un message d'erreur.
        if (engueuler) {
            Privmsg msg = Privmsg
                    .buildAnswer(
                            im,
                            user
                                    + ", me prend pas pour un con, je suis quand meme le momobot, et je lance les dés entre 2 et 10000.");
            control.sendMsg(msg);
            return;
        }
        
        // random de base
        int number = RandomUtils.nextInt(max) + 1;
        
        // Petits commentaires futés
        String commentaire;
        if (number == max) {
            commentaire = "Quelle chance ! ";
        } else if (number == 1) {
            commentaire = "C'est vraiment pas son jour : ";
        } else if (number == 1337) {
            commentaire = "OMG leet lancé ! ";
        } else if (number == 666) {
            commentaire = "Vade Retro, Satan ! ";
        } else if (number / (double) max > 0.8) {
            commentaire = "Pas trop mal, ";
        }

        else {
            commentaire = "";
        }
        Privmsg msg = Privmsg.buildAnswer(im, commentaire + user + " a obtenu un " + number + " (sur " + max + ')');
        control.sendMsg(msg);
        
    }
}

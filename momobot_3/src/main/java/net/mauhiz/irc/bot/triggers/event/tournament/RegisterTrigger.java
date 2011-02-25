package net.mauhiz.irc.bot.triggers.event.tournament;

import java.util.Locale;

import net.mauhiz.irc.base.IIrcControl;
import net.mauhiz.irc.base.data.IrcChannel;
import net.mauhiz.irc.base.data.IrcServer;
import net.mauhiz.irc.base.data.IrcUser;
import net.mauhiz.irc.base.data.Mask;
import net.mauhiz.irc.base.msg.Privmsg;
import net.mauhiz.irc.base.trigger.IPrivmsgTrigger;
import net.mauhiz.irc.bot.event.ChannelEvent;
import net.mauhiz.irc.bot.triggers.AbstractTextTrigger;

import org.apache.commons.lang.ArrayUtils;

/**
 * @author topper
 * 
 */
public class RegisterTrigger extends AbstractTextTrigger implements IPrivmsgTrigger {
    
    /**
     * @param stg
     * @return boolean
     */
    private static Locale getLocale(String stg) {
        for (Locale loc : Locale.getAvailableLocales()) {
            if (loc.getCountry().equalsIgnoreCase(stg)) {
                return loc;
            }
        }
        return null;
    }
    /**
     * @param trigger
     *            le trigger
     */
    public RegisterTrigger(String trigger) {
        super(trigger);
    }
    
    /**
     * @see net.mauhiz.irc.base.trigger.IPrivmsgTrigger#doTrigger(net.mauhiz.irc.base.msg.Privmsg,
     *      net.mauhiz.irc.base.IIrcControl)
     */
    @Override
    public void doTrigger(Privmsg im, IIrcControl control) {
        IrcServer server = im.getServer();
        IrcChannel chan = server.findChannel(im.getTo());
        ChannelEvent event = chan.getEvt();
        if (event == null) {
            Privmsg msg = Privmsg.buildAnswer(im, "Aucun tournois n'est lance.");
            control.sendMsg(msg);
            return;
        }
        
        if (event instanceof Tournament) {
            Tournament tn = (Tournament) event;
            String[] args = getArgs(im.getMessage()).split(" ");
            if (tn.isReady()) {
                if (args.length > 3) {
                    // on match le pays
                    Locale loc = getLocale(args[0]);
                    if (loc != null) {
                        // on match le nom de la team mininum 3 caracteres
                        if (args[1].length() > 2) {
                            String tag = args[1];
                            // on decoupe la liste
                            IrcUser ircuser = im.getServer().findUser(new Mask(im.getFrom()), true);
                            String[] nicks = (String[]) ArrayUtils.subarray(args, 2, args.length);
                            Privmsg msg = Privmsg.buildPrivateAnswer(im, tn.setTeam(ircuser, loc, tag, nicks));
                            
                            // on lance un status
                            Privmsg msg1 = Privmsg.buildAnswer(im, tn.getStatus());
                            control.sendMsg(msg);
                            control.sendMsg(msg1);
                            
                            return;
                        }
                        Privmsg msg = Privmsg.buildAnswer(im, "Erreur : nom de team doit etre > a 3 caracteres.");
                        control.sendMsg(msg);
                        return;
                    }
                    Privmsg msg = Privmsg.buildAnswer(im, "Erreur : Abbreviation du pays inconnu ex: FR");
                    control.sendMsg(msg);
                    return;
                }
                Privmsg msg = Privmsg
                        .buildAnswer(im,
                                "Erreur : parametre(s) insuffisant(s). ex : $tn-register FR eule joueur1 joueur2 joueur3 joueur4 joueur5");
                control.sendMsg(msg);
                return;
            }
            Privmsg msg = Privmsg.buildAnswer(im, "Erreur : Tournois deja complet.");
            control.sendMsg(msg);
            return;
            
        }
    }
}

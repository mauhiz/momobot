package net.mauhiz.irc.bot.triggers.event.tournament;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import net.mauhiz.irc.base.IIrcControl;
import net.mauhiz.irc.base.data.Channel;
import net.mauhiz.irc.base.data.IrcServer;
import net.mauhiz.irc.base.data.IrcUser;
import net.mauhiz.irc.base.data.Mask;
import net.mauhiz.irc.base.model.Channels;
import net.mauhiz.irc.base.model.Users;
import net.mauhiz.irc.base.msg.Privmsg;
import net.mauhiz.irc.bot.event.ChannelEvent;
import net.mauhiz.irc.bot.triggers.AbstractTextTrigger;
import net.mauhiz.irc.bot.triggers.IPrivmsgTrigger;

/**
 * @author topper
 * 
 */
public class RegisterTrigger extends AbstractTextTrigger implements IPrivmsgTrigger {
    /**
     * @param string
     * @return String[] ou a decoupe idTeam et Country
     */
    private static List<String> cutList(final String[] string) {
        if (string.length < 2) {
            return null;
        }
        
        List<String> strReturn = new ArrayList<String>();
        for (int i = 2; i < string.length; i++) {
            strReturn.add(string[i]);
        }
        return strReturn;
    }
    
    /**
     * @param stg
     * @return boolean
     */
    private static Locale getLocale(final String stg) {
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
    public RegisterTrigger(final String trigger) {
        super(trigger);
    }
    
    /**
     * @see net.mauhiz.irc.bot.triggers.IPrivmsgTrigger#doTrigger(net.mauhiz.irc.base.msg.Privmsg,
     *      net.mauhiz.irc.base.IIrcControl)
     */
    @Override
    public void doTrigger(final Privmsg im, final IIrcControl control) {
        IrcServer server = im.getServer();
        Channel chan = Channels.getInstance(server).get(im.getTo());
        ChannelEvent event = chan.getEvt();
        if (event == null) {
            Privmsg msg = Privmsg.buildAnswer(im, "Aucun tournois n'est lance.");
            control.sendMsg(msg);
            return;
        }
        
        if (event instanceof Tournament) {
            String[] args = getArgs(im.getMessage()).split(" ");
            if (((Tournament) event).isReady()) {
                if (args.length > 3) {
                    // on match le pays
                    Locale loc = getLocale(args[0]);
                    if (loc != null) {
                        // on match le nom de la team mininum 3 caractères
                        if (args[1].length() > 2) {
                            String tag = args[1];
                            // on découpe la liste
                            IrcUser ircuser = Users.getInstance(im.getServer()).findUser(new Mask(im.getFrom()), true);
                            Privmsg msg = Privmsg.buildPrivateAnswer(im, ((Tournament) event).setTeam(ircuser, loc,
                                    tag, cutList(args)));
                            
                            // on lance un status
                            Privmsg msg1 = Privmsg.buildAnswer(im, ((Tournament) event).getStatus());
                            control.sendMsg(msg);
                            control.sendMsg(msg1);
                            
                            return;
                        }
                        Privmsg msg = Privmsg.buildAnswer(im, "Erreur : nom de team doit etre > a 3 caracteres.");
                        control.sendMsg(msg);
                        return;
                    }
                    Privmsg msg = Privmsg.buildAnswer(im, "Erreur : Abréviation du pays inconnu ex: FR");
                    control.sendMsg(msg);
                    return;
                }
                Privmsg msg = Privmsg
                        .buildAnswer(im,
                                "Erreur : parametre(s) insuffisant(s). ex : $tn-register FR eule joueur1 joueur2 joueur3 joueur4 joueur5");
                control.sendMsg(msg);
                return;
            }
            Privmsg msg = Privmsg.buildAnswer(im, "Erreur : Tournois déja complet.");
            control.sendMsg(msg);
            return;
            
        }
    }
}

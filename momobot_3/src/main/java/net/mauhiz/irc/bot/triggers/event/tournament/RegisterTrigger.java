package net.mauhiz.irc.bot.triggers.event.tournament;

import net.mauhiz.irc.base.IIrcControl;
import net.mauhiz.irc.base.data.Channel;
import net.mauhiz.irc.base.data.IrcServer;
import net.mauhiz.irc.base.model.Channels;
import net.mauhiz.irc.base.msg.Privmsg;
import net.mauhiz.irc.bot.event.ChannelEvent;
import net.mauhiz.irc.bot.event.Tournament;
import net.mauhiz.irc.bot.triggers.AbstractTextTrigger;
import net.mauhiz.irc.bot.triggers.IPrivmsgTrigger;

public class RegisterTrigger extends AbstractTextTrigger implements IPrivmsgTrigger {
    /**
     * @param trigger
     *            le trigger
     */
    public RegisterTrigger(final String trigger) {
        super(trigger);
    }
    
    /**
     * @param string
     * @return String[] ou a decoupe idTeam et Country
     */
    private String[] cutList(final String[] string) {
        if (string.length < 2) {
            return null;
        }
        String[] strReturn = new String[string.length - 2];
        for (int i = 0; i < strReturn.length; i++) {
            strReturn[i] = string[i + 2];
        }
        return strReturn;
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
            if (args.length > 2) {
                // on match l'id de la team
                if (Integer.parseInt(args[0]) > 0) {
                    int id = Integer.parseInt(args[0]);
                    // on match le pays
                    if (args[1].length() == 2) {
                        String country = args[1];
                        // on match le nom de la team mininum 3 caractères
                        if (args[2].length() > 2) {
                            // on découpe la liste
                            Privmsg msg = Privmsg.buildAnswer(im, ((Tournament) event).setTeam(id, country,
                                    cutList(args)));
                            control.sendMsg(msg);
                        }
                        Privmsg msg = Privmsg.buildAnswer(im, "Erreur : nom de team doit etre > a 3 caracteres.");
                        control.sendMsg(msg);
                        
                    }
                    Privmsg msg = Privmsg.buildAnswer(im,
                            "Erreur : Abréviation du pays doit etre = a 2 caracteres ex: FR");
                    control.sendMsg(msg);
                }
                Privmsg msg = Privmsg.buildAnswer(im,
                        "Erreur : le premier argument doit etre un chiffre correspondant a l'ID de la team.");
                control.sendMsg(msg);
                
            }
            Privmsg msg = Privmsg
                    .buildAnswer(im,
                            "Erreur : parametre(s) insuffisant(s). ex : $tn-register 5 FR eule joueur1 joueur2 joueur3 joueur4 joueur5");
            control.sendMsg(msg);
        }
    }
}

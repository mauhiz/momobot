package net.mauhiz.irc.bot.triggers.event.tournament;

import java.util.ArrayList;

import net.mauhiz.irc.base.IIrcControl;
import net.mauhiz.irc.base.data.Channel;
import net.mauhiz.irc.base.data.IrcServer;
import net.mauhiz.irc.base.model.Channels;
import net.mauhiz.irc.base.msg.Privmsg;
import net.mauhiz.irc.bot.event.ChannelEvent;
import net.mauhiz.irc.bot.tournament.Tournament;
import net.mauhiz.irc.bot.triggers.AbstractTextTrigger;
import net.mauhiz.irc.bot.triggers.IPrivmsgTrigger;

public class ResultTrigger extends AbstractTextTrigger implements IPrivmsgTrigger {
    /**
     * @param trigger
     *            le trigger
     */
    public ResultTrigger(final String trigger) {
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
            if (args.length == 3) {
                if (Integer.parseInt(args[0]) > -1 && Integer.parseInt(args[1]) > -1 && Integer.parseInt(args[2]) > -1) {
                    int id = Integer.parseInt(args[0]);
                    int score1 = Integer.parseInt(args[1]);
                    int score2 = Integer.parseInt(args[2]);
                    ArrayList<String> str = ((Tournament) event).setScore(id, score1, score2);
                    for (String element : str) {
                        Privmsg msg = Privmsg.buildAnswer(im, element);
                        control.sendMsg(msg);
                    }
                    return;
                }
                
            }
            Privmsg msg = Privmsg.buildAnswer(im,
                    "Erreur : Parametre(s) Incorrect(s). ex : $tn-result id_team score1 score2");
            control.sendMsg(msg);
            return;
        }
    }
}

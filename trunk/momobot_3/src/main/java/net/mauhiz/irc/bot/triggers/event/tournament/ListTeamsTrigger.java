package net.mauhiz.irc.bot.triggers.event.tournament;

import java.util.ArrayList;

import net.mauhiz.irc.base.IIrcControl;
import net.mauhiz.irc.base.data.Channel;
import net.mauhiz.irc.base.data.IrcServer;
import net.mauhiz.irc.base.model.Channels;
import net.mauhiz.irc.base.msg.Notice;
import net.mauhiz.irc.base.msg.Privmsg;
import net.mauhiz.irc.bot.event.ChannelEvent;
import net.mauhiz.irc.bot.tournament.Tournament;
import net.mauhiz.irc.bot.triggers.AbstractTextTrigger;
import net.mauhiz.irc.bot.triggers.IPrivmsgTrigger;

public class ListTeamsTrigger extends AbstractTextTrigger implements IPrivmsgTrigger {
    /**
     * @param trigger
     *            le trigger
     */
    public ListTeamsTrigger(final String trigger) {
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
            ArrayList<String> reply = new ArrayList<String>();
            reply = ((Tournament) event).getListTeam();
            if (!reply.isEmpty()) {
                for (String element : reply) {
                    Notice msg = Notice.buildAnswer(im, element);
                    control.sendMsg(msg);
                }
            }
        }
    }
}

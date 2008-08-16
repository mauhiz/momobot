package net.mauhiz.irc.bot.triggers.event;

import net.mauhiz.irc.base.IIrcControl;
import net.mauhiz.irc.base.data.Channel;
import net.mauhiz.irc.base.data.IrcServer;
import net.mauhiz.irc.base.model.Channels;
import net.mauhiz.irc.base.msg.Privmsg;
import net.mauhiz.irc.bot.event.ChannelEvent;
import net.mauhiz.irc.bot.triggers.AbstractTextTrigger;
import net.mauhiz.irc.bot.triggers.IPrivmsgTrigger;

/**
 * @author mauhiz
 */
public class StatusTrigger extends AbstractTextTrigger implements IPrivmsgTrigger {
    /**
     * @param trigger
     *            le trigger
     */
    public StatusTrigger(final String trigger) {
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
        ChannelEvent evt = chan.getEvt();
        String reply;
        if (evt == null) {
            reply = "Il se passe rien ici :x";
        } else {
            reply = evt.toString();
        }
        
        Privmsg msg = Privmsg.buildAnswer(im, reply);
        control.sendMsg(msg);
    }
}

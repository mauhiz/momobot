package net.mauhiz.irc.bot.triggers.event.gather;

import net.mauhiz.irc.base.IIrcControl;
import net.mauhiz.irc.base.data.IrcChannel;
import net.mauhiz.irc.base.msg.Privmsg;
import net.mauhiz.irc.bot.event.ChannelEvent;
import net.mauhiz.irc.bot.event.Gather;
import net.mauhiz.irc.bot.triggers.AbstractTextTrigger;
import net.mauhiz.irc.bot.triggers.IPrivmsgTrigger;

/**
 * @author mauhiz
 */
public class TgSeekTrigger extends AbstractTextTrigger implements IPrivmsgTrigger {
    /**
     * @param trigger
     *            le trigger
     */
    public TgSeekTrigger(final String trigger) {
        super(trigger);
    }
    /**
     * @see net.mauhiz.irc.bot.triggers.IPrivmsgTrigger#doTrigger(net.mauhiz.irc.base.msg.Privmsg,
     *      net.mauhiz.irc.base.IIrcControl)
     */
    @Override
    public void doTrigger(final Privmsg im, final IIrcControl control) {
        IrcChannel chan = im.getServer().findChannel(im.getTo());
        ChannelEvent evt = chan.getEvt();
        String reply = "";
        if (evt == null) {
            reply = "Aucun gather n'est lance.";
        } else {
            if (evt instanceof Gather) {
                if (((Gather) evt).getSeek() != null) {
                    if (((Gather) evt).getSeek().isSeekInProgress()) {
                        reply = "Le seek est en cour.";
                        
                    } else {
                        // on reset le seek
                        ((Gather) evt).setSeekToNull();
                        reply = "ok je remballe.";
                    }
                    
                } else {
                    reply = "Pas de seek en cour.";
                }
                
                Privmsg msg = Privmsg.buildAnswer(im, reply);
                control.sendMsg(msg);
                return;
            }
        }
    }
}

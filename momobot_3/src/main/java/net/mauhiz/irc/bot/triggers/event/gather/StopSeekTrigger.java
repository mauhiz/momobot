package net.mauhiz.irc.bot.triggers.event.gather;

import net.mauhiz.irc.base.IIrcControl;
import net.mauhiz.irc.base.data.IrcChannel;
import net.mauhiz.irc.base.data.IrcServer;
import net.mauhiz.irc.base.msg.Part;
import net.mauhiz.irc.base.msg.Privmsg;
import net.mauhiz.irc.bot.event.ChannelEvent;
import net.mauhiz.irc.bot.event.Gather;
import net.mauhiz.irc.bot.event.SeekWar;
import net.mauhiz.irc.bot.triggers.AbstractTextTrigger;
import net.mauhiz.irc.bot.triggers.IPrivmsgTrigger;

/**
 * @author mauhiz
 */
public class StopSeekTrigger extends AbstractTextTrigger implements IPrivmsgTrigger {
    /**
     * ON LEAVE LES CHANNELS DE SEEK
     * 
     * @param control
     * @param server
     */
    static void leaveSeekChans(final IIrcControl control, final IrcServer server) {
        String[] channelSeek = SeekWar.SEEK_CHANS;
        for (String element : channelSeek) {
            Part leave = new Part(server, element, null);
            control.sendMsg(leave);
        }
    }
    
    /**
     * @param trigger
     *            le trigger
     */
    public StopSeekTrigger(final String trigger) {
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
                    // if (((Gather) evt).getSeek().isSeekInProgress()) {
                    reply = "Seek arrete.";
                    leaveSeekChans(control, im.getServer());
                    ((Gather) evt).setSeekToNull();
                    // }
                } else {
                    reply = "Le seek n'est pas lance.";
                }
                
            } else {
                return;
            }
        }
        Privmsg msg = Privmsg.buildAnswer(im, reply);
        control.sendMsg(msg);
    }
}

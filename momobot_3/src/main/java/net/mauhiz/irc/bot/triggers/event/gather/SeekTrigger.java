package net.mauhiz.irc.bot.triggers.event.gather;

import java.util.List;

import net.mauhiz.irc.base.IIrcControl;
import net.mauhiz.irc.base.data.Channel;
import net.mauhiz.irc.base.data.IrcUser;
import net.mauhiz.irc.base.data.Mask;
import net.mauhiz.irc.base.model.Channels;
import net.mauhiz.irc.base.model.Users;
import net.mauhiz.irc.base.msg.Join;
import net.mauhiz.irc.base.msg.Privmsg;
import net.mauhiz.irc.bot.event.ChannelEvent;
import net.mauhiz.irc.bot.event.Gather;
import net.mauhiz.irc.bot.event.SeekWar;
import net.mauhiz.irc.bot.triggers.AbstractGourmandTrigger;
import net.mauhiz.irc.bot.triggers.IPrivmsgTrigger;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;

/**
 * @author Topper
 */

public class SeekTrigger extends AbstractGourmandTrigger implements IPrivmsgTrigger {
    /**
     * logger.
     */
    private static final Logger LOG = Logger.getLogger(SeekWar.class);
    /**
     * @param trigger
     *            le trigger
     */
    public SeekTrigger(final String trigger) {
        super(trigger);
    }
    
    /**
     * @see net.mauhiz.irc.bot.triggers.IPrivmsgTrigger#doTrigger(net.mauhiz.irc.base.msg.Privmsg,
     *      net.mauhiz.irc.base.IIrcControl)
     */
    @Override
    public void doTrigger(final Privmsg im, final IIrcControl control) {
        Channel chan = Channels.get(im.getServer()).getChannel(im.getTo());
        ChannelEvent evt = chan.getEvt();
        if (isCommandMsg(im.getMessage())) {
            
            if (evt == null) {
                Privmsg resp = Privmsg.buildAnswer(im, "Aucun gather n'est lance.");
                control.sendMsg(resp);
            } else if (evt instanceof Gather) {
                String reply;
                if (((Gather) evt).getSeek().isSeekInProgress()) {
                    reply = "Seek déja en cours.";
                } else {
                    reply = ((Gather) evt).getSeek().start(StringUtils.split(getArgs(im.getMessage())));
                    if (((Gather) evt).getSeek().isSeekInProgress()) {
                        String[] channelSeek = SeekWar.SEEK_CHANS;
                        
                        for (String element : channelSeek) {
                            Join go = new Join(im.getServer(), element);
                            control.sendMsg(go);
                        }
                        
                        // ON ENVOIE LES MSG DE SEEK
                        
                        for (String element : channelSeek) {
                            Privmsg im1 = new Privmsg("momobot3", element, im.getServer(), im.getMessage());
                            Privmsg resp = Privmsg.buildAnswer(im1, ((Gather) evt).getSeek().getMessageForSeeking());
                            control.sendMsg(resp);
                        }
                        
                    }
                }
                Privmsg resp = Privmsg.buildAnswer(im, reply);
                control.sendMsg(resp);
            }
        } else {
            
            Channel chan1 = Channels.get(im.getServer()).getChannel("#tsi.fr");
            ChannelEvent evt1 = chan1.getEvt();
            // if (evt instanceof Gather) {
            if (evt1 instanceof Gather) {
                final Gather gather = (Gather) evt1;
                if (gather.getSeek().isSeekInProgress()) {
                    if (!gather.getSeek().isTimeOut()) {
                        // TJS en vie
                        if (LOG.isDebugEnabled()) {
                            LOG.debug("MSG entrant : " + im.getMessage() + " to : " + im.getTo() + " from : "
                                    + im.getFrom());
                        }
                        IrcUser kikoolol = Users.get(im.getServer()).findUser(new Mask(im.getFrom()), true);
                        List<String> replies = gather.getSeek()
                                .submitSeekMessage(im.getMessage(), im.getTo(), kikoolol);
                        switch (replies.size()) {
                            case 3 :
                                for (int i = 0; i < 2; i++) {
                                    if (!replies.get(i).isEmpty()) {
                                        Privmsg resp = Privmsg.buildPrivateAnswer(im, replies.get(i));
                                        control.sendMsg(resp);
                                    }
                                }
                                Privmsg resp = new Privmsg(null, "#tsi.fr", im.getServer(), replies.get(2));
                                control.sendMsg(resp);
                                break;
                            
                            default :
                                for (String reply2 : replies) {
                                    if (!reply2.isEmpty()) {
                                        Privmsg resp2 = Privmsg.buildPrivateAnswer(im, reply2);
                                        control.sendMsg(resp2);
                                    }
                                    
                                }
                                break;
                            
                        }
                        
                    } else {
                        // !! Time out !!
                        if (LOG.isDebugEnabled()) {
                            LOG.debug("Seek-Time-Out atteint.");
                        }
                        Privmsg resp = Privmsg.buildAnswer(im, gather.getSeek().stopSeek());
                        control.sendMsg(resp);
                        // LEAVE LES CHANNELS
                        StopSeekTrigger.leaveSeekChans(control, im.getServer());
                    }
                }
            }
        }
        /*
         * String reply; if (evt == null) { reply = "Aucun gather n'est lance."; } else { if (evt instanceof Gather) {
         * if (((Gather) evt).isSeekInProgress()) { reply = "Seek déja en cour."; } else { reply = ((Gather)
         * evt).seek(im.getMessage().split(" ")); } } else { return; } } Privmsg msg = Privmsg.buildAnswer(im, reply);
         * control.sendMsg(msg);
         */
    }
}

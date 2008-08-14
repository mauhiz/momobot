package net.mauhiz.irc.bot.triggers.event.gather;

import net.mauhiz.irc.MomoStringUtils;
import net.mauhiz.irc.base.IIrcControl;
import net.mauhiz.irc.base.data.Channel;
import net.mauhiz.irc.base.model.Channels;
import net.mauhiz.irc.base.msg.IrcMessage;
import net.mauhiz.irc.base.msg.Join;
import net.mauhiz.irc.base.msg.Privmsg;
import net.mauhiz.irc.bot.event.ChannelEvent;
import net.mauhiz.irc.bot.event.Gather;
import net.mauhiz.irc.bot.event.SeekWar;
import net.mauhiz.irc.bot.triggers.AbstractTextTrigger;
import net.mauhiz.irc.bot.triggers.IPrivmsgTrigger;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;

/**
 * @author Topper
 */

public class SeekTrigger extends AbstractTextTrigger implements IPrivmsgTrigger {
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
        String reply = "";
        if (isCommandMsg(im.getMessage())) {
            
            if (evt == null) {
                reply = "Aucun gather n'est lance.";
            } else {
                if (evt instanceof Gather) {
                    if (((Gather) evt).getSeek().isSeekInProgress()) {
                        reply = "Seek déja en cour.";
                    } else {
                        reply = ((Gather) evt).getSeek().start(StringUtils.split(getArgs(im.getMessage())));
                        if (((Gather) evt).getSeek().isSeekInProgress()) {
                            String[] channelSeek = SeekWar.channels.split(";");
                            for (String element : channelSeek) {
                                Join go = new Join(im.getServer(), element);
                            }
                            
                            // ON ENVOI LES MSG DE SEEK
                            
                            Privmsg resp = Privmsg.buildAnswer(im, MomoStringUtils.genereSeekMessage(((Gather) evt)
                                    .getSeek().getSeekMessage(), ((Gather) evt).getNumberPlayers(), ((Gather) evt)
                                    .getSeek().getSeekServ(), ((Gather) evt).getSeek().getSeekLevel()));
                            control.sendMsg(resp);
                            
                        }
                    }
                }
            }
            
            if (!reply.isEmpty()) {
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
                            LOG.debug("MSG entrent : " + im.getMessage() + " to : " + im.getTo() + " from : "
                                    + im.getFrom());
                        }
                        String[] replys;
                        replys = gather.getSeek().submitSeekMessage(im.getMessage(), im.getTo(), im.getFrom());
                        switch (replys.length) {
                            case 3 : {
                                for (int i = 0; i < 2; i++) {
                                    if (!replys[i].isEmpty()) {
                                        Privmsg resp = Privmsg.buildPrivateAnswer(im, replys[i]);
                                        control.sendMsg(resp);
                                    }
                                }
                                IrcMessage ircmsg = new IrcMessage(im.getTo(), "#tsi.fr", im.getServer());
                                Privmsg resp = Privmsg.buildAnswer(ircmsg, replys[2]);
                                control.sendMsg(resp);
                                break;
                            }
                                
                            default : {
                                for (String reply2 : replys) {
                                    if (!reply2.isEmpty()) {
                                        Privmsg resp = Privmsg.buildPrivateAnswer(im, reply2);
                                        control.sendMsg(resp);
                                    }
                                    
                                }
                                break;
                            }
                        }
                        
                    } else {
                        // !! Time out !!
                        if (LOG.isDebugEnabled()) {
                            LOG.debug("Seek-Time-Out atteint.");
                        }
                        reply = gather.getSeek().stopSeek();
                        // LEAVE LES CHANNELS
                    }
                    
                }
                
            }
            if (!reply.isEmpty()) {
                Privmsg resp = Privmsg.buildAnswer(im, reply);
                control.sendMsg(resp);
            }
        }
        /**
         * String reply; if (evt == null) { reply = "Aucun gather n'est lance."; } else { if (evt instanceof Gather) {
         * if (((Gather) evt).isSeekInProgress()) { reply = "Seek déja en cour."; } else { reply = ((Gather)
         * evt).seek(im.getMessage().split(" ")); } } else { return; } } Privmsg msg = Privmsg.buildAnswer(im, reply);
         * control.sendMsg(msg);
         */
    }
    /**
     * @see net.mauhiz.irc.bot.triggers.AbstractTextTrigger#isActivatedBy(java.lang.String)
     */
    @Override
    public boolean isActivatedBy(final String msg) {
        return true;
    }
    /**
     * @param msg
     * @return si il s'agit bien du trigger.
     */
    public boolean isCommandMsg(final String msg) {
        return super.isActivatedBy(msg);
    }
    
}

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

/**
 * MEMO: de chose a revoir: 1) charger les valeurs avec le xml 2) détecté une adresse IP + Port pour un msg PV 3) sur le
 * lister tout les gathers en cour et executer l'action correspondant
 * 
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
        if (isCommandMsg(im.getMessage())) {
            Channel chan = Channels.getInstance(im.getServer()).get(im.getTo());
            if (chan == null) {
                /* msg prive */
                return;
            }
            ChannelEvent evt = chan.getEvt();
            if (evt == null) {
                Privmsg resp = Privmsg.buildAnswer(im, "Aucun gather n'est lance.");
                control.sendMsg(resp);
            } else if (evt instanceof Gather) {
                String reply;
                if (((Gather) evt).getSeek().isSeekInProgress()) {
                    reply = "Seek déja en cours.";
                } else {
                    reply = ((Gather) evt).getSeek()
                            .start(StringUtils.split(getArgs(im.getMessage())), chan.toString());
                    if (((Gather) evt).getSeek().isSeekInProgress()) {
                        String[] channelSeek = SeekWar.SEEK_CHANS;
                        
                        for (String element : channelSeek) {
                            Join go = new Join(im.getServer(), element);
                            control.sendMsg(go);
                        }
                        
                        // ON ENVOIE LES MSG DE SEEK
                        
                        for (String element : channelSeek) {
                            Privmsg resp = new Privmsg(null, element, im.getServer(), ((Gather) evt).getSeek()
                                    .getMessageForSeeking());
                            control.sendMsg(resp);
                        }
                        
                    }
                }
                Privmsg resp = Privmsg.buildAnswer(im, reply);
                control.sendMsg(resp);
            }
        } else {
            /* FIXME vilain hardcode */
            Channel chan1 = Channels.getInstance(im.getServer()).get("#tsi.fr");
            ChannelEvent evt1 = chan1.getEvt();
            // if (evt instanceof Gather) {
            if (evt1 instanceof Gather) {
                final Gather gather = (Gather) evt1;
                IrcUser kikoolol = Users.getInstance(im.getServer()).findUser(new Mask(im.getFrom()), true);
                
                if (gather.getSeek().isSeekInProgress()) {
                    if (!gather.getSeek().isTimeOut()) {
                        // TJS en vie
                        if (LOG.isDebugEnabled()) {
                            LOG.debug("MSG entrant : " + im.getMessage() + " to : " + im.getTo() + " from : "
                                    + im.getFrom());
                        }
                        List<Privmsg> replies = gather.getSeek().submitSeekMessage(im);
                        for (Privmsg element : replies) {
                            
                            control.sendMsg(element);
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
                    // Si c'est le winner du seek, je transmet le msg PV
                } else if (gather.getSeek().getSeekWinner().equals(kikoolol.getNick()) && im.getTo().equals("mom0")) {
                    Privmsg reply = new Privmsg(null, gather.getSeek().getChannel(), im.getServer(), kikoolol.getNick()
                            + " : " + im.getMessage());
                    control.sendMsg(reply);
                    // Le seek est réussi, on leave les channels
                } else if (!gather.getSeek().isSeekInProgress() && gather.getSeek().isLunchedAndQuit) {
                    gather.getSeek().isLunchedAndQuit = false;
                    StopSeekTrigger.leaveSeekChans(control, im.getServer());
                }
            }
        }
    }
}

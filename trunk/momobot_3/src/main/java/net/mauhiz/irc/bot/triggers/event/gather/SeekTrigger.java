package net.mauhiz.irc.bot.triggers.event.gather;

import java.util.List;
import java.util.Set;

import net.mauhiz.irc.base.IIrcControl;
import net.mauhiz.irc.base.data.IrcChannel;
import net.mauhiz.irc.base.data.IrcUser;
import net.mauhiz.irc.base.data.Mask;
import net.mauhiz.irc.base.msg.Join;
import net.mauhiz.irc.base.msg.Notice;
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
            IrcChannel chan = im.getServer().findChannel(im.getTo());
            if (chan == null) {
                /* msg prive */
                return;
            }
            ChannelEvent evt = chan.getEvt();
            if (evt == null) {
                Privmsg resp = Privmsg.buildAnswer(im, "Aucun gather n'est lance.");
                control.sendMsg(resp);
            } else if (evt instanceof Gather) {
                String reply = "";
                if (((Gather) evt).getSeek() != null) {
                    if (((Gather) evt).getSeek().isSeekInProgress()) {
                        reply = "Seek déja en cours.";
                    }
                } else {
                    ((Gather) evt).createSeekWar();
                    reply = ((Gather) evt).getSeek().start(StringUtils.split(getArgs(im.getMessage())),
                            chan.toString(), ((Gather) evt).getNumberPlayers());
                    if (((Gather) evt).getSeek().isSeekInProgress()) {
                        // L'initialisation a réussi
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
                        
                    } else {
                        // Le seek a foiré! on notice le noob pour lui donner la syntaxe
                        ((Gather) evt).setSeekToNull();
                        Notice notice;
                        String[] noticeListHelp = {
                                "syntaxe : " + toString() + " [on/off] (sans les [])",
                                "syntaxe : " + toString() + " off [low,mid,skilled,pgm,high,autre level] (sans les [])",
                                "syntaxe : "
                                        + toString()
                                        + " off level \"message de seek entre crochet :: %P=nombre de joueur, %L = level, %S = serv(off ici) \"",
                                "syntaxe : " + toString() + " on \"adresse ip + mdp entre crochet\"",
                                "syntaxe : " + toString()
                                        + " on \"ip+pass\" [low,mid,skilled,pgm,high,autre level] (sans les [])",
                                "syntaxe : "
                                        + toString()
                                        + " on \"ip+pass\" level \"message de seek entre crochet :: %P=nombre de joueur, %L = level, %S = serv(off ici) \""};
                        for (String element : noticeListHelp) {
                            notice = Notice.buildPrivateAnswer(im, element);
                            control.sendMsg(notice);
                        }
                        
                    }
                }
                Privmsg resp = Privmsg.buildAnswer(im, reply);
                control.sendMsg(resp);
                
            }
        } else {
            IrcUser mmb = im.getServer().findUser(im.getServer().getMyNick(), false);
            Set<IrcChannel> myChans = im.getServer().getChannelsForUser(mmb);
            for (IrcChannel element : myChans) {
                
                IrcChannel chan1 = im.getServer().findChannel(element.toString());
                ChannelEvent evt1 = chan1.getEvt();
                // if (evt instanceof Gather) {
                if (evt1 instanceof Gather) {
                    final Gather gather = (Gather) evt1;
                    IrcUser kikoolol = im.getServer().findUser(new Mask(im.getFrom()), true);
                    if (gather.getSeek() != null) {
                        // if (gather.getSeek().isSeekInProgress()) {
                        if (!gather.getSeek().isTimeOut()) {
                            // TJS en vie
                            if (LOG.isDebugEnabled()) {
                                LOG.debug("MSG entrant : " + im.getMessage() + " to : " + im.getTo() + " from : "
                                        + im.getFrom());
                            }
                            // Si c'est le winner du seek, je transmet le msg PV
                            if (gather.getSeek().getSeekWinner().equals(kikoolol.getNick())
                                    && im.getTo().equals(im.getServer().getMyNick())) {
                                Privmsg reply = new Privmsg(null, gather.getSeek().getChannel(), im.getServer(),
                                        kikoolol.getNick() + " : " + im.getMessage());
                                control.sendMsg(reply);
                            } else if (gather.getSeek().isSeekInProgress()) {
                                List<Privmsg> replies = gather.getSeek().submitSeekMessage(im);
                                for (Privmsg element1 : replies) {
                                    
                                    control.sendMsg(element1);
                                }
                            } else if (!gather.getSeek().isSeekInProgress() && gather.getSeek().isLaunchedAndQuit) {
                                gather.getSeek().isLaunchedAndQuit = false;
                                StopSeekTrigger.leaveSeekChans(control, im.getServer());
                            }
                            
                        } else {
                            // !! Time out !!
                            if (LOG.isDebugEnabled()) {
                                LOG.debug("Seek-Time-Out atteint.");
                            }
                            gather.setSeekToNull();
                            Privmsg resp = new Privmsg(null, gather.getSeek().getChannel(), im.getServer(),
                                    "Time out atteint, seek stopper.");
                            control.sendMsg(resp);
                            // LEAVE LES CHANNELS
                            StopSeekTrigger.leaveSeekChans(control, im.getServer());
                        }
                    }
                }
            }
        }
    }
}

package net.mauhiz.irc.bot.triggers.event.gather;

import java.util.List;
import java.util.Set;

import net.mauhiz.irc.base.IIrcControl;
import net.mauhiz.irc.base.data.IrcChannel;
import net.mauhiz.irc.base.data.IrcUser;
import net.mauhiz.irc.base.msg.Join;
import net.mauhiz.irc.base.msg.Notice;
import net.mauhiz.irc.base.msg.Privmsg;
import net.mauhiz.irc.base.trigger.IPrivmsgTrigger;
import net.mauhiz.irc.bot.event.ChannelEvent;
import net.mauhiz.irc.bot.event.Gather;
import net.mauhiz.irc.bot.event.SeekWar;
import net.mauhiz.irc.bot.triggers.AbstractGourmandTrigger;

import org.apache.commons.lang.StringUtils;

/**
 * MEMO: de chose a revoir:<br>
 * 1) charger les valeurs avec le xml<br>
 * 2) detecter une adresse IP + Port pour un msg PV<br>
 * 3) sur le lister tout les gathers en cour et executer l'action correspondant
 * 
 * @author Topper
 */
public class SeekTrigger extends AbstractGourmandTrigger implements IPrivmsgTrigger {
    /**
     * @param trigger
     *            le trigger
     */
    public SeekTrigger(String trigger) {
        super(trigger);
    }

    /**
     * @see net.mauhiz.irc.base.trigger.IPrivmsgTrigger#doTrigger(Privmsg, IIrcControl)
     */
    @Override
    public void doTrigger(Privmsg im, IIrcControl control) {
        if (isCommandMsg(im.getMessage())) {
            IrcChannel chan = (IrcChannel) im.getTo();
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
                Gather gather = (Gather) evt;
                SeekWar seek = gather.getSeek();
                if (seek == null) {
                    seek = gather.createSeekWar();
                    reply = seek.start(StringUtils.split(getArgs(im.getMessage())), chan, gather.getNumberPlayers());
                    if (seek.isSeekInProgress()) {
                        // L'initialisation a reussi
                        String[] channelSeek = SeekWar.SEEK_CHANS;

                        for (String element : channelSeek) {
                            Join go = new Join(im.getServer(), im.getServer().findChannel(element));
                            control.sendMsg(go);
                        }

                        // ON ENVOIE LES MSG DE SEEK

                        for (String element : channelSeek) {
                            Privmsg resp = new Privmsg(null, im.getServer().findChannel(element), im.getServer(),
                                    seek.getMessageForSeeking());
                            control.sendMsg(resp);
                        }

                    } else {
                        // Le seek a foire! on notice le noob pour lui donner la syntaxe
                        gather.setSeekToNull();
                        Notice notice;
                        String[] noticeListHelp = {
                                "syntaxe : " + getTriggerText() + " [on/off] (sans les [])",
                                "syntaxe : " + getTriggerText()
                                        + " off [low,mid,skilled,pgm,high,autre level] (sans les [])",
                                "syntaxe : "
                                        + getTriggerText()
                                        + " off level \"message de seek entre crochet :: %P=nombre de joueur, %L = level, %S = serv(off ici) \"",
                                "syntaxe : " + getTriggerText() + " on \"adresse ip + mdp entre crochet\"",
                                "syntaxe : " + getTriggerText()
                                        + " on \"ip+pass\" [low,mid,skilled,pgm,high,autre level] (sans les [])",
                                "syntaxe : "
                                        + getTriggerText()
                                        + " on \"ip+pass\" level \"message de seek entre crochet :: %P=nombre de joueur, %L = level, %S = serv(off ici) \"" };
                        for (String element : noticeListHelp) {
                            notice = Notice.buildPrivateAnswer(im, element);
                            control.sendMsg(notice);
                        }
                    }
                } else if (seek.isSeekInProgress()) {
                    reply = "Seek deja en cours.";
                } else {
                    reply = "";
                }

                Privmsg resp = Privmsg.buildAnswer(im, reply);
                control.sendMsg(resp);

            }
        } else {
            IrcUser myself = im.getServer().getMyself();
            Set<IrcChannel> myChans = im.getServer().getChannelsForUser(myself);
            for (IrcChannel chan1 : myChans) {
                ChannelEvent evt1 = chan1.getEvt();
                // if (evt instanceof Gather) {
                if (evt1 instanceof Gather) {
                    Gather gather = (Gather) evt1;
                    IrcUser kikoolol = (IrcUser) im.getFrom();
                    SeekWar seek = gather.getSeek();
                    if (seek != null) {
                        // if (seek.isSeekInProgress()) {
                        if (seek.isTimeOut()) {
                            // !! Time out !!
                            LOG.debug("Seek-Time-Out atteint.");
                            gather.setSeekToNull();
                            Privmsg resp = new Privmsg(null, seek.getChannel(), im.getServer(),
                                    "Time out atteint, seek stopper.");
                            control.sendMsg(resp);
                            // LEAVE LES CHANNELS
                            StopSeekTrigger.leaveSeekChans(control, im.getServer());
                        } else {
                            // TJS en vie
                            LOG.debug("MSG entrant : " + im);
                            // Si c'est le winner du seek, je transmet le msg PV
                            if (seek.getSeekWinner().equals(kikoolol.getNick())
                                    && im.getTo().equals(im.getServer().getMyself())) {
                                Privmsg reply = new Privmsg(null, seek.getChannel(), im.getServer(), kikoolol.getNick()
                                        + " : " + im.getMessage());
                                control.sendMsg(reply);
                            } else if (seek.isSeekInProgress()) {
                                List<Privmsg> replies = seek.submitSeekMessage(im);
                                for (Privmsg element1 : replies) {
                                    control.sendMsg(element1);
                                }
                            } else if (!seek.isSeekInProgress() && seek.isLaunchedAndQuit) {
                                seek.isLaunchedAndQuit = false;
                                StopSeekTrigger.leaveSeekChans(control, im.getServer());
                            }

                        }
                    }
                }
            }
        }
    }
}

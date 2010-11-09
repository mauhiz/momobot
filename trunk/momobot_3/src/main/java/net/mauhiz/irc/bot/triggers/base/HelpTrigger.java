package net.mauhiz.irc.bot.triggers.base;

import java.util.SortedSet;
import java.util.TreeSet;

import net.mauhiz.irc.base.IIrcControl;
import net.mauhiz.irc.base.IrcControl;
import net.mauhiz.irc.base.msg.Notice;
import net.mauhiz.irc.base.msg.Privmsg;
import net.mauhiz.irc.bot.MmbTriggerManager;
import net.mauhiz.irc.bot.triggers.AbstractTextTrigger;
import net.mauhiz.irc.bot.triggers.INoticeTrigger;
import net.mauhiz.irc.bot.triggers.IPrivmsgTrigger;
import net.mauhiz.irc.bot.triggers.ITrigger;

/**
 * @author mauhiz
 */
public class HelpTrigger extends AbstractTextTrigger implements IPrivmsgTrigger, INoticeTrigger {
    /**
     * Commandes :.
     */
    private static final String COMMANDES = "Commandes: ";
    
    /**
     * @param trigger
     *            le trigger
     */
    public HelpTrigger(String trigger) {
        super(trigger);
    }
    
    /**
     * @see net.mauhiz.irc.bot.triggers.INoticeTrigger#doTrigger(Notice, IIrcControl)
     */
    @Override
    public void doTrigger(Notice im, IIrcControl control) {
        StringBuilder msg = new StringBuilder(COMMANDES);
        SortedSet<String> cmds = new TreeSet<String>();
        // on fait en 2x pour trier les commandes
        for (ITrigger trig : getTriggers(control)) {
            if (trig instanceof INoticeTrigger) {
                cmds.add(((INoticeTrigger) trig).getTriggerText());
            }
        }
        int maxLen = im.getServer().getLineMaxLength() - 50; // TODO make precise computation of overhead in NOTICE
        for (String trig : cmds) {
            if (msg.length() >= maxLen) { // flush
                Notice resp = Notice.buildPrivateAnswer(im, msg.toString());
                control.sendMsg(resp);
                msg = new StringBuilder(COMMANDES);
            }
            msg.append(trig).append(' ');
        }
        Notice resp = Notice.buildPrivateAnswer(im, msg.toString());
        control.sendMsg(resp);
    }
    
    /**
     * @see net.mauhiz.irc.bot.triggers.IPrivmsgTrigger#doTrigger(Privmsg, IIrcControl)
     */
    @Override
    public void doTrigger(Privmsg im, IIrcControl control) {
        StringBuilder msg = new StringBuilder(COMMANDES);
        SortedSet<String> cmds = new TreeSet<String>();
        // on fait en 2x pour trier les commandes
        for (ITrigger trig : getTriggers(control)) {
            if (trig instanceof IPrivmsgTrigger) {
                cmds.add(((IPrivmsgTrigger) trig).getTriggerText());
            }
        }
        int maxLen = im.getServer().getLineMaxLength(); // TODO make precise computation of overhead in PRIVMSG
        for (String trig : cmds) {
            if (msg.length() >= maxLen) {
                Privmsg resp = Privmsg.buildAnswer(im, msg.toString());
                control.sendMsg(resp);
                msg = new StringBuilder(COMMANDES);
            }
            msg.append(trig).append(' ');
        }
        Privmsg resp = Privmsg.buildAnswer(im, msg.toString());
        control.sendMsg(resp);
    }
    
    /**
     * @param control
     * @return triggers view
     */
    private Iterable<ITrigger> getTriggers(IIrcControl control) {
        IrcControl realControl = (IrcControl) control;
        MmbTriggerManager manager = (MmbTriggerManager) realControl.getManager();
        return manager.getTriggers();
    }
}

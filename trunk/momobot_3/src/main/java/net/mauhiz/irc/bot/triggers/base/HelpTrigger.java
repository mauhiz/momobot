package net.mauhiz.irc.bot.triggers.base;

import java.util.Collections;
import java.util.SortedSet;
import java.util.TreeSet;

import net.mauhiz.irc.base.IIrcControl;
import net.mauhiz.irc.base.IrcClientControl;
import net.mauhiz.irc.base.msg.Notice;
import net.mauhiz.irc.base.msg.Privmsg;
import net.mauhiz.irc.base.trigger.DefaultTriggerManager;
import net.mauhiz.irc.base.trigger.INoticeTrigger;
import net.mauhiz.irc.base.trigger.IPrivmsgTrigger;
import net.mauhiz.irc.base.trigger.ITrigger;
import net.mauhiz.irc.base.trigger.ITriggerManager;
import net.mauhiz.irc.bot.triggers.AbstractTextTrigger;
import net.mauhiz.irc.bot.triggers.ICommand;
import net.mauhiz.util.Messages;

/**
 * @author mauhiz
 */
public class HelpTrigger extends AbstractTextTrigger implements IPrivmsgTrigger, INoticeTrigger {

    /**
     * @param trigger
     *            le trigger
     */
    public HelpTrigger(String trigger) {
        super(trigger);
    }

    /**
     * @see net.mauhiz.irc.base.trigger.INoticeTrigger#doTrigger(Notice, IIrcControl)
     */
    @Override
    public void doTrigger(Notice im, IIrcControl control) {
        StringBuilder msg = new StringBuilder(getHeader());
        SortedSet<String> cmds = new TreeSet<String>();
        // on fait en 2x pour trier les commandes
        for (ITrigger trig : getTriggers(control)) {
            if (trig instanceof ICommand && trig instanceof INoticeTrigger) {
                cmds.add(((ICommand) trig).getTriggerText());
            }
        }
        int maxLen = im.getServerPeer().getNetwork().getLineMaxLength() - 50; // TODO make precise computation of overhead in NOTICE
        for (String trig : cmds) {
            if (msg.length() >= maxLen) { // flush
                Notice resp = Notice.buildPrivateAnswer(im, msg.toString());
                control.sendMsg(resp);
                msg = new StringBuilder(getHeader());
            }
            msg.append(trig).append(' ');
        }
        Notice resp = Notice.buildPrivateAnswer(im, msg.toString());
        control.sendMsg(resp);
    }

    /**
     * @see net.mauhiz.irc.base.trigger.IPrivmsgTrigger#doTrigger(Privmsg, IIrcControl)
     */
    @Override
    public void doTrigger(Privmsg im, IIrcControl control) {
        StringBuilder msg = new StringBuilder(getHeader());
        SortedSet<String> cmds = new TreeSet<String>();
        // on fait en 2x pour trier les commandes
        for (ITrigger trig : getTriggers(control)) {
            if (trig instanceof ICommand && trig instanceof IPrivmsgTrigger) {
                cmds.add(((ICommand) trig).getTriggerText());
            }
        }
        int maxLen = im.getServerPeer().getNetwork().getLineMaxLength(); // TODO make precise computation of overhead in PRIVMSG
        for (String trig : cmds) {
            if (msg.length() >= maxLen) {
                Privmsg resp = Privmsg.buildAnswer(im, msg.toString());
                control.sendMsg(resp);
                msg = new StringBuilder(getHeader());
            }
            msg.append(trig).append(' ');
        }
        Privmsg resp = Privmsg.buildAnswer(im, msg.toString());
        control.sendMsg(resp);
    }

    private String getHeader() {
        return Messages.get(getClass(), "help"); //$NON-NLS-1$
    }

    /**
     * @param control
     * @return triggers view
     */
    private Iterable<ITrigger> getTriggers(IIrcControl control) {
        IrcClientControl realControl = (IrcClientControl) control;
        ITriggerManager[] managers = realControl.getManagers();
        for (ITriggerManager manager : managers) {
            if (manager instanceof DefaultTriggerManager) {
                return ((DefaultTriggerManager) manager).getTriggers();
            }
        }
        return Collections.emptySet();
    }
}

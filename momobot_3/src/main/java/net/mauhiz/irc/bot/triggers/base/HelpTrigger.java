package net.mauhiz.irc.bot.triggers.base;

import java.util.Set;

import net.mauhiz.irc.base.IIrcControl;
import net.mauhiz.irc.base.IrcControl;
import net.mauhiz.irc.base.msg.Notice;
import net.mauhiz.irc.base.msg.Privmsg;
import net.mauhiz.irc.bot.MmbTriggerManager;
import net.mauhiz.irc.bot.triggers.AbstractTextTrigger;
import net.mauhiz.irc.bot.triggers.INoticeTrigger;
import net.mauhiz.irc.bot.triggers.IPrivmsgTrigger;
import net.mauhiz.irc.bot.triggers.ITrigger;

import org.apache.commons.lang.text.StrBuilder;

/**
 * @author mauhiz
 */
public class HelpTrigger extends AbstractTextTrigger implements IPrivmsgTrigger, INoticeTrigger {
    /**
     * Commandes :.
     */
    private static final String COMMANDES = "Commandes :";
    
    /**
     * @param trigger
     *            le trigger
     */
    public HelpTrigger(final String trigger) {
        super(trigger);
    }
    
    /**
     * @see net.mauhiz.irc.bot.triggers.INoticeTrigger#doTrigger(net.mauhiz.irc.base.msg.Notice,
     *      net.mauhiz.irc.base.IIrcControl)
     */
    @Override
    public void doTrigger(final Notice im, final IIrcControl control) {
        StrBuilder msg = new StrBuilder();
        msg.append(COMMANDES);
        for (final ITrigger trig : getTriggers(control)) {
            if (trig instanceof INoticeTrigger) {
                msg.append(trig).append(' ');
            }
        }
        Notice resp = Notice.buildPrivateAnswer(im, msg.toString());
        control.sendMsg(resp);
        
    }
    /**
     * @see net.mauhiz.irc.bot.triggers.IPrivmsgTrigger#doTrigger(net.mauhiz.irc.base.msg.Privmsg,
     *      net.mauhiz.irc.base.IIrcControl)
     */
    @Override
    public void doTrigger(final Privmsg im, final IIrcControl control) {
        StrBuilder msg = new StrBuilder();
        msg.append(COMMANDES);
        for (final ITrigger trig : getTriggers(control)) {
            if (trig instanceof IPrivmsgTrigger) {
                msg.append(trig).append(' ');
            }
        }
        Privmsg resp = Privmsg.buildAnswer(im, msg.toString());
        control.sendMsg(resp);
    }
    
    /**
     * @param control
     * @return triggers view
     */
    private Set<ITrigger> getTriggers(final IIrcControl control) {
        IrcControl realControl = (IrcControl) control;
        MmbTriggerManager manager = (MmbTriggerManager) realControl.getManager();
        return manager.getTriggers();
    }
}

package net.mauhiz.irc.bot.triggers.admin;

import net.mauhiz.irc.base.IIrcControl;
import net.mauhiz.irc.base.msg.MeMsg;
import net.mauhiz.irc.base.msg.Privmsg;
import net.mauhiz.irc.bot.triggers.AbstractTextTrigger;
import net.mauhiz.irc.bot.triggers.IAdminTrigger;
import net.mauhiz.irc.bot.triggers.IPrivmsgTrigger;

/**
 * @author mauhiz
 */
public class SayMeTrigger extends AbstractTextTrigger implements IAdminTrigger, IPrivmsgTrigger {
    /**
     * @param trigger
     *            le trigger
     */
    public SayMeTrigger(final String trigger) {
        super(trigger);
    }
    
    /**
     * @see net.mauhiz.irc.bot.triggers.IPrivmsgTrigger#doTrigger(net.mauhiz.irc.base.msg.Privmsg,
     *      net.mauhiz.irc.base.IIrcControl)
     */
    @Override
    public void doTrigger(final Privmsg pme, final IIrcControl control) {
        final String args = getArgs(pme.getMessage());
        final int index = args.indexOf(' ');
        if (index < 1) {
            MeMsg msg = new MeMsg(null, pme.getTo(), pme.getServer(), "pas assez de paramètres.");
            control.sendMsg(msg);
            msg = new MeMsg(null, pme.getTo(), pme.getServer(), "syntaxe " + this + " target msg");
            control.sendMsg(msg);
        } else {
            /* TODO say cross-server ? */
            MeMsg msg = new MeMsg(null, args.substring(0, index), pme.getServer(), args.substring(index + 1));
            control.sendMsg(msg);
        }
    }
}

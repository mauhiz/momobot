package net.mauhiz.irc.bot.triggers.base;

import net.mauhiz.irc.base.IIrcControl;
import net.mauhiz.irc.base.msg.Action;
import net.mauhiz.irc.base.msg.Privmsg;
import net.mauhiz.irc.bot.triggers.AbstractTextTrigger;
import net.mauhiz.irc.bot.triggers.IAdminTrigger;
import net.mauhiz.irc.bot.triggers.IPrivmsgTrigger;

/**
 * @author mauhiz
 */
public class ActTrigger extends AbstractTextTrigger implements IAdminTrigger, IPrivmsgTrigger {
    /**
     * @param trigger
     *            le trigger
     */
    public ActTrigger(final String trigger) {
        super(trigger);
    }
    
    /**
     * @see net.mauhiz.irc.bot.triggers.IPrivmsgTrigger#doTrigger(net.mauhiz.irc.base.msg.Privmsg,
     *      net.mauhiz.irc.base.IIrcControl)
     */
    @Override
    public void doTrigger(final Privmsg im, final IIrcControl control) {
        final String args = getArgs(im.getMessage());
        final int index = args.indexOf(' ');
        if (index < 1) {
            Privmsg msg = Privmsg.buildPrivateAnswer(im, "pas assez de paramètres.");
            control.sendMsg(msg);
            msg = Privmsg.buildPrivateAnswer(im, "syntaxe $" + this + " target msg");
            control.sendMsg(msg);
        } else {
            /* FIXME cross-server */
            Action msg = new Action(null, args.substring(0, index), im.getServer(), args.substring(index + 1));
            control.sendMsg(msg);
        }
    }
}

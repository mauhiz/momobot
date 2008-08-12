package net.mauhiz.irc.bot.triggers.admin;

import net.mauhiz.irc.base.IIrcControl;
import net.mauhiz.irc.base.msg.Part;
import net.mauhiz.irc.base.msg.Privmsg;
import net.mauhiz.irc.bot.triggers.AbstractTextTrigger;
import net.mauhiz.irc.bot.triggers.IAdminTrigger;
import net.mauhiz.irc.bot.triggers.IPrivmsgTrigger;

/**
 * @author mauhiz
 */
public class PartTrigger extends AbstractTextTrigger implements IPrivmsgTrigger, IAdminTrigger {
    /**
     * @param trigger
     *            le trigger
     */
    public PartTrigger(final String trigger) {
        super(trigger);
    }
    
    /**
     * @see net.mauhiz.irc.bot.triggers.IPrivmsgTrigger#doTrigger(net.mauhiz.irc.base.msg.Privmsg,
     *      net.mauhiz.irc.base.IIrcControl)
     */
    @Override
    public void doTrigger(final Privmsg im, final IIrcControl control) {
        String args = getArgs(im.getMessage());
        int spc = args.indexOf(' ');
        String reason;
        if (spc > 0) {
            reason = args.substring(spc + 1);
            args = args.substring(0, spc);
        } else {
            reason = null;
        }
        /* TODO part cross servers */
        Part leave = new Part(im.getServer(), args, reason);
        control.sendMsg(leave);
    }
}

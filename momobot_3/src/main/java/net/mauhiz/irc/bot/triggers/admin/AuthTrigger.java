package net.mauhiz.irc.bot.triggers.admin;

import net.mauhiz.irc.base.IIrcControl;
import net.mauhiz.irc.base.msg.Privmsg;
import net.mauhiz.irc.bot.triggers.AbstractTextTrigger;
import net.mauhiz.irc.bot.triggers.IPrivmsgTrigger;

/**
 * @author mauhiz
 */
public class AuthTrigger extends AbstractTextTrigger implements IPrivmsgTrigger {
    /**
     * @param trigger
     *            le trigger
     */
    public AuthTrigger(final String trigger) {
        super(trigger);
    }
    
    /**
     * @see net.mauhiz.irc.bot.triggers.IPrivmsgTrigger#doTrigger(net.mauhiz.irc.base.msg.Privmsg,
     *      net.mauhiz.irc.base.IIrcControl)
     */
    @Override
    public void doTrigger(final Privmsg pme, final IIrcControl control) {
        /* pour l'instant je hard code le pw. La s�cu attendra ;x */
        if (getArgs(pme.getMessage()).equals("boulz")) {
            pme.getFrom()/* FIXME .setAdmin(true) */;
            Privmsg msg = Privmsg.buildPrivateAnswer(pme, "Oui, ma�tre");
            control.sendMsg(msg);
        }
    }
}
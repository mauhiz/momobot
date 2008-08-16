package net.mauhiz.irc.bot.triggers.websearch;

import net.mauhiz.irc.base.IIrcControl;
import net.mauhiz.irc.base.msg.IIrcMessage;
import net.mauhiz.irc.base.msg.Privmsg;
import net.mauhiz.irc.bot.triggers.AbstractTextTrigger;
import net.mauhiz.irc.bot.triggers.IPrivmsgTrigger;

/**
 * @author mauhiz
 */
public class GoogleTrigger extends AbstractTextTrigger implements IPrivmsgTrigger {
    /**
     * @param trigger
     *            le trigger
     */
    public GoogleTrigger(final String trigger) {
        super(trigger);
    }
    
    /**
     * @see net.mauhiz.irc.bot.triggers.IPrivmsgTrigger#doTrigger(net.mauhiz.irc.base.msg.Privmsg,
     *      net.mauhiz.irc.base.IIrcControl)
     */
    @Override
    public void doTrigger(final Privmsg cme, final IIrcControl control) {
        final WebQuery query = new WebQuery("google", getArgs(cme.getMessage()));
        for (final String next : query.results()) {
            IIrcMessage notice = Privmsg.buildAnswer(cme, next);
            control.sendMsg(notice);
        }
    }
}

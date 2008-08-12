package net.mauhiz.irc.bot.triggers;

import net.mauhiz.irc.base.IIrcControl;
import net.mauhiz.irc.base.msg.Privmsg;

/**
 * @author mauhiz
 */
public interface IPrivmsgTrigger extends ITextTrigger {
    /**
     * @param im
     * @param control
     */
    void doTrigger(final Privmsg im, final IIrcControl control);
}

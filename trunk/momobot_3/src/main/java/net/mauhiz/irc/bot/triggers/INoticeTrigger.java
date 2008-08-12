package net.mauhiz.irc.bot.triggers;

import net.mauhiz.irc.base.IIrcControl;
import net.mauhiz.irc.base.msg.Notice;

/**
 * @author mauhiz
 */
public interface INoticeTrigger extends ITextTrigger {
    /**
     * @param im
     * @param control
     */
    void doTrigger(final Notice im, final IIrcControl control);
}

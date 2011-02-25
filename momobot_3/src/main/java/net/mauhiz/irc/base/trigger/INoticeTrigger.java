package net.mauhiz.irc.base.trigger;

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
    void doTrigger(Notice im, IIrcControl control);
}

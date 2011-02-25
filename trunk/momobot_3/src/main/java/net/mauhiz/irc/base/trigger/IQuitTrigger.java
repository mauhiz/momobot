package net.mauhiz.irc.base.trigger;

import net.mauhiz.irc.base.IIrcControl;
import net.mauhiz.irc.base.msg.Quit;

/**
 * @author mauhiz
 */
public interface IQuitTrigger extends ITrigger {
    /**
     * @param im
     * @param control
     */
    void doTrigger(Quit im, IIrcControl control);
}

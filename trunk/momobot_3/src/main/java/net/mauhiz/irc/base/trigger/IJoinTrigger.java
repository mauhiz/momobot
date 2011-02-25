package net.mauhiz.irc.base.trigger;

import net.mauhiz.irc.base.IIrcControl;
import net.mauhiz.irc.base.msg.Join;

/**
 * @author mauhiz
 */
public interface IJoinTrigger extends ITrigger {
    /**
     * @param im
     * @param control
     */
    void doTrigger(Join im, IIrcControl control);
}

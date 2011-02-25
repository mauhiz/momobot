package net.mauhiz.irc.base.trigger;

import net.mauhiz.irc.base.IIrcControl;
import net.mauhiz.irc.base.msg.Part;

/**
 * @author mauhiz
 */
public interface IPartTrigger extends ITrigger {
    /**
     * @param im
     * @param control
     */
    void doTrigger(Part im, IIrcControl control);
}

package net.mauhiz.irc.bot.triggers;

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

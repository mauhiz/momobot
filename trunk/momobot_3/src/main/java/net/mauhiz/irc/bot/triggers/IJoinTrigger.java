package net.mauhiz.irc.bot.triggers;

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
    void doTrigger(final Join im, final IIrcControl control);
}

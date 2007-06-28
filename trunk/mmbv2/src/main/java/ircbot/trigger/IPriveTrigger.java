/**
 * 
 */
package ircbot.trigger;

import ircbot.IrcUser;

/**
 * @author mauhiz
 */
public interface IPriveTrigger extends ITrigger {
    /**
     * @param from
     *            le user
     * @param message
     *            le message
     */
    void executePrivateTrigger(final IrcUser from, final String message);
}

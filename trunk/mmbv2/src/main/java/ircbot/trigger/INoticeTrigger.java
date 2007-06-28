/**
 * 
 */
package ircbot.trigger;

import ircbot.IrcUser;

/**
 * @author mauhiz
 */
public interface INoticeTrigger {
    /**
     * @param from
     *            le user
     * @param message
     *            le message
     */
    void executeNoticeTrigger(final IrcUser from, final String message);
}

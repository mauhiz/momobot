package net.mauhiz.irc.base.msg;

import net.mauhiz.irc.base.data.Target;

/**
 * @author mauhiz
 */
public interface IPrivateIrcMessage extends IIrcMessage {
    String getMessage();

    Target getTo();

    /**
     * @return true is this message is directed to a channel
     */
    boolean isToChannel();
}

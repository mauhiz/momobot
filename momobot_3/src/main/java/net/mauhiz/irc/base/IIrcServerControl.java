package net.mauhiz.irc.base;

import net.mauhiz.irc.base.data.IIrcServerPeer;
import net.mauhiz.irc.base.data.IrcUser;
import net.mauhiz.irc.base.msg.IIrcMessage;

/**
 * @author mauhiz
 */
public interface IIrcServerControl extends IIrcControl {

    void greet(IrcUser client);

    void kill(IrcUser client);

    void link(IIrcServerPeer another);

    boolean process(IIrcMessage message);

    void unlink(IIrcServerPeer another);
}

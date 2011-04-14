package net.mauhiz.irc.base;

import net.mauhiz.irc.base.data.IIrcServerPeer;
import net.mauhiz.irc.base.io.IIrcIO;
import net.mauhiz.irc.base.msg.IIrcMessage;

/**
 * @author mauhiz
 */
public interface IIrcClientControl extends IIrcControl {
    /**
     * @param peer
     */
    void connect(IIrcServerPeer peer);

    boolean process(IIrcMessage message, IIrcIO io);

    void quit(IIrcServerPeer peer);
}

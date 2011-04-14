package net.mauhiz.irc.base.data;

import java.net.InetSocketAddress;

/**
 * @author mauhiz
 */
public interface IrcPeer {

    InetSocketAddress getAddress();

    /**
     * @return The IRC network it is connected to (as a client or as a server)
     */
    IrcNetwork getNetwork();
}

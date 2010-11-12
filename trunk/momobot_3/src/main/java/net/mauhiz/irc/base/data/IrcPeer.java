package net.mauhiz.irc.base.data;

import java.net.InetSocketAddress;

import net.mauhiz.irc.base.msg.IIrcMessage;

/**
 * @author mauhiz
 */
public interface IrcPeer {
    
    IIrcMessage buildFromRaw(String raw);
    
    InetSocketAddress getAddress();
    
    int getLineMaxLength();
}

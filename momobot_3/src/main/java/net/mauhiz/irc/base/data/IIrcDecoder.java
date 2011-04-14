package net.mauhiz.irc.base.data;

import net.mauhiz.irc.base.msg.IIrcMessage;

/**
 * @author mauhiz
 */
public interface IIrcDecoder {
    IIrcMessage buildFromRaw(IIrcServerPeer server, String raw);
}

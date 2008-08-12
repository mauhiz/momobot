package net.mauhiz.irc.base;

import java.io.IOException;

import net.mauhiz.irc.base.data.IrcServer;
import net.mauhiz.irc.base.msg.IIrcMessage;

/**
 * @author mauhiz
 */
public interface IIrcControl {
    void connect(IrcServer server);

    void decodeIrcRawMsg(String raw, IIrcIO io);

    void exit() throws IOException;

    void sendMsg(IIrcMessage msg);
}

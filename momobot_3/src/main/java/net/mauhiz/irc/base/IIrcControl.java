package net.mauhiz.irc.base;

import java.io.IOException;

import net.mauhiz.irc.base.data.IrcServer;
import net.mauhiz.irc.base.msg.IIrcMessage;

/**
 * @author mauhiz
 */
public interface IIrcControl {
    /**
     * @param server
     */
    void connect(IrcServer server);
    
    /**
     * @param raw
     *            NOT NULL
     * @param io
     */
    void decodeIrcRawMsg(String raw, IIrcIO io);
    
    /**
     * @throws IOException
     */
    void exit() throws IOException;
    
    void quit(IrcServer server);
    
    /**
     * @param msg
     */
    void sendMsg(IIrcMessage msg);
}

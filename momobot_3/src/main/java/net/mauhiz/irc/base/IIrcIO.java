package net.mauhiz.irc.base;

import java.io.IOException;

import net.mauhiz.irc.base.IrcIO.Status;
import net.mauhiz.irc.base.data.IrcServer;

/**
 * @author mauhiz
 */
public interface IIrcIO {
    void connect(IrcServer server) throws IOException;

    void disconnect() throws IOException;

    Status getStatus();

    void processMsg(String msg);

    void sendMsg(String msg);

    void setStatus(Status status);
}
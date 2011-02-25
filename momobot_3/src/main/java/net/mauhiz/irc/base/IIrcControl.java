package net.mauhiz.irc.base;

import java.io.IOException;

import net.mauhiz.irc.base.data.IrcServer;
import net.mauhiz.irc.base.io.IIrcIO;
import net.mauhiz.irc.base.msg.IIrcMessage;
import net.mauhiz.irc.base.trigger.ITriggerManager;

/**
 * @author mauhiz
 */
public interface IIrcControl {

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

    ITriggerManager getManager();

    /**
     * @param msg
     */
    void sendMsg(IIrcMessage msg);
}

package net.mauhiz.irc.base;

import java.io.IOException;

import net.mauhiz.irc.base.msg.IIrcMessage;

/**
 * @author mauhiz
 */
public interface IIrcControl {

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

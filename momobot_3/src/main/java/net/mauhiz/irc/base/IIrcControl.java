package net.mauhiz.irc.base;

import java.io.IOException;

import net.mauhiz.irc.base.io.IIrcIO;
import net.mauhiz.irc.base.msg.IIrcMessage;
import net.mauhiz.irc.base.trigger.ITriggerManager;

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

    ITriggerManager[] getManagers();

    /**
     * @param msg
     */
    void sendMsg(IIrcMessage msg);
}

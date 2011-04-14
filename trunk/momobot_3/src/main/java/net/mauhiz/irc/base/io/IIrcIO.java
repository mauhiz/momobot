package net.mauhiz.irc.base.io;

import java.io.IOException;

import net.mauhiz.irc.base.data.IIrcServerPeer;

/**
 * @author mauhiz
 */
public interface IIrcIO {
    /**
     * @exception RuntimeException
     *                if {@link IOException}
     */
    void disconnect();

    /**
     * @return server
     */
    IIrcServerPeer getServerPeer();

    /**
     * @return status
     */
    IOStatus getStatus();

    /**
     * @param msg
     */
    void processMsg(String msg);

    /**
     * @param msg
     */
    void sendMsg(String msg);

    /**
     * @param status
     */
    void setStatus(IOStatus status);
}

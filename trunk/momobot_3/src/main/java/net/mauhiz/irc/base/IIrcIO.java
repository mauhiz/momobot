package net.mauhiz.irc.base;

import java.io.IOException;

import net.mauhiz.irc.base.data.IrcPeer;

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
    IrcPeer getPeer();
    
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

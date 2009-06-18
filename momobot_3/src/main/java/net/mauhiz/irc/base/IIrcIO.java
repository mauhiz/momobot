package net.mauhiz.irc.base;

import java.io.IOException;

import net.mauhiz.irc.base.IrcIO.Status;
import net.mauhiz.irc.base.data.IrcServer;

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
    IrcServer getServer();
    
    /**
     * @return status
     */
    Status getStatus();
    
    /**
     * @param msg
     */
    void processMsg(String msg);
    
    /**
     * @throws IOException
     */
    void reconnect() throws IOException;
    
    /**
     * @param msg
     */
    void sendMsg(String msg);
    
    /**
     * @param status
     */
    void setStatus(Status status);
    
    void waitForConnection();
    
}

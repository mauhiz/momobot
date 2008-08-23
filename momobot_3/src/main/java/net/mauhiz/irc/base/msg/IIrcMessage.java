package net.mauhiz.irc.base.msg;

import net.mauhiz.irc.base.data.IrcServer;

/**
 * @author mauhiz
 */
public interface IIrcMessage {
    /**
     * @return from
     */
    String getFrom();
    
    /**
     * @return server
     */
    IrcServer getServer();
    
    /**
     * @return to
     */
    String getTo();
}

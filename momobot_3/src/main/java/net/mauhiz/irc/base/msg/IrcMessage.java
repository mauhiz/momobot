package net.mauhiz.irc.base.msg;

import net.mauhiz.irc.base.data.IrcServer;

/**
 * Generic IRC Message
 * 
 * @author mauhiz
 */
public class IrcMessage implements IIrcMessage {
    String from;
    IrcServer server;
    String to;
    
    public IrcMessage(final String from1, final String to1, final IrcServer server1) {
        from = from1;
        to = to1;
        server = server1;
    }
    
    /**
     * @see net.mauhiz.irc.base.msg.IIrcMessage#getFrom()
     */
    public String getFrom() {
        return from;
    }
    
    /**
     * @see net.mauhiz.irc.base.msg.IIrcMessage#getServer()
     */
    public IrcServer getServer() {
        return server;
    }
    
    /**
     * @see net.mauhiz.irc.base.msg.IIrcMessage#getTo()
     */
    public String getTo() {
        return to;
    }
}

package net.mauhiz.irc.base.msg;

import net.mauhiz.irc.base.data.IrcServer;

import org.apache.commons.lang.builder.ToStringBuilder;

/**
 * Generic IRC Message
 * 
 * @author mauhiz
 */
public abstract class AbstractIrcMessage implements IIrcMessage {
    protected String from;
    protected IrcServer server;
    protected String to;
    
    /**
     * @param from1
     * @param to1
     * @param server1
     */
    public AbstractIrcMessage(String from1, String to1, IrcServer server1) {
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
    
    /**
     * debug
     * 
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }
}

package net.mauhiz.irc.base.msg;

import net.mauhiz.irc.base.data.IrcChannel;
import net.mauhiz.irc.base.data.IrcServer;
import net.mauhiz.irc.base.data.Target;

import org.apache.commons.lang.builder.ToStringBuilder;

/**
 * Generic IRC Message
 * 
 * @author mauhiz
 */
public abstract class AbstractIrcMessage implements IIrcMessage {
    protected Target from;
    protected IrcServer server;
    protected Target to;

    /**
     * @param from1
     * @param to1
     * @param server1
     */
    public AbstractIrcMessage(Target from1, Target to1, IrcServer server1) {
        from = from1;
        to = to1;
        server = server1;
    }

    /**
     * @see net.mauhiz.irc.base.msg.IIrcMessage#getFrom()
     */
    public Target getFrom() {
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
    public Target getTo() {
        return to;
    }

    public boolean isToChannel() {
        return to instanceof IrcChannel;
    }

    protected String niceFromDisplay() {
        if (from == null) {
            return server.getMyself().toString();
        }
        return from.toString();
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

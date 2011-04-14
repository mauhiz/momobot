package net.mauhiz.irc.base.msg;

import net.mauhiz.irc.base.data.IIrcServerPeer;
import net.mauhiz.irc.base.data.IrcChannel;
import net.mauhiz.irc.base.data.Target;

import org.apache.commons.lang.builder.ToStringBuilder;

/**
 * Generic IRC Message
 * 
 * @author mauhiz
 */
public abstract class AbstractIrcMessage implements IIrcMessage {
    protected final Target from;
    protected final IIrcServerPeer server;
    protected final Target to;

    public AbstractIrcMessage(Target from, Target to, IIrcServerPeer server) {
        this.from = from;
        this.to = to;
        this.server = server;
    }

    /**
     * @see net.mauhiz.irc.base.msg.IIrcMessage#getFrom()
     */
    public Target getFrom() {
        return from;
    }

    /**
     * @see net.mauhiz.irc.base.msg.IIrcMessage#getServerPeer()
     */
    public IIrcServerPeer getServerPeer() {
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

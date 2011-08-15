package net.mauhiz.irc.base.msg;

import net.mauhiz.irc.base.data.IIrcServerPeer;
import net.mauhiz.irc.base.data.IrcCommands;
import net.mauhiz.irc.base.data.Target;

import org.apache.commons.lang3.builder.ToStringBuilder;

/**
 * Generic IRC Message
 * 
 * @author mauhiz
 */
public abstract class AbstractIrcMessage implements IIrcMessage {
    protected final Target from;
    protected final IIrcServerPeer server;

    public AbstractIrcMessage(IIrcServerPeer server, Target from) {
        this.from = from;
        this.server = server;
    }

    /**
     * @see net.mauhiz.irc.base.msg.IIrcMessage#getFrom()
     */
    public Target getFrom() {
        return from;
    }

    public abstract IrcCommands getIrcCommand();

    public String getIrcForm() {
        return ircFromDisplay() + getIrcCommand();
    }

    /**
     * @see net.mauhiz.irc.base.msg.IIrcMessage#getServerPeer()
     */
    public IIrcServerPeer getServerPeer() {
        return server;
    }

    protected String ircFromDisplay() {
        return from == null ? "" : ':' + from.getIrcForm() + ' ';
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

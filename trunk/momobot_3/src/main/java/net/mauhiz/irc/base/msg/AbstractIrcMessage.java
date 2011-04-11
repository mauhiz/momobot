package net.mauhiz.irc.base.msg;

import net.mauhiz.irc.MomoStringUtils;
import net.mauhiz.irc.base.data.IrcServer;
import net.mauhiz.irc.base.data.IrcUser;
import net.mauhiz.irc.base.data.Mask;

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

    public boolean isToChannel() {
        return MomoStringUtils.isChannelName(to);
    }

    protected String niceFromDisplay() {
        assert from != null; // calling method must play by the rules!
        Mask fromMask = new Mask(from);
        if (fromMask.getNick() == null) {
            return server.getAlias();
        }
        IrcUser fromUser = server.findUser(fromMask, false);
        if (fromUser == null) {
            return from; // should seldom happen
        }
        return fromUser.toString();
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

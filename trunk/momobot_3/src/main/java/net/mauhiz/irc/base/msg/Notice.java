package net.mauhiz.irc.base.msg;

import net.mauhiz.irc.base.data.IrcServer;
import net.mauhiz.irc.base.model.Channels;

import org.apache.commons.lang.StringUtils;

/**
 * @author mauhiz
 */
public class Notice extends IrcMessage {
    /**
     * @param toReply
     * @param msg
     * @return
     */
    public static Notice buildAnswer(final IrcMessage toReply, final String msg) {
        String oldDest = toReply.getTo();
        if (Channels.isChannelName(oldDest)) {
            return new Notice(null, oldDest, toReply.getServer(), msg);
        }
        return buildPrivateAnswer(toReply, msg);
    }
    
    /**
     * @param toReply
     * @param msg
     * @return
     */
    public static Notice buildPrivateAnswer(final IrcMessage toReply, final String msg) {
        return new Notice(null, toReply.getFrom(), toReply.getServer(), msg);
    }
    
    private final String message;
    
    /**
     * @param from1
     * @param to1
     * @param ircServer
     * @param msg1
     */
    public Notice(final String from1, final String to1, final IrcServer ircServer, final String msg1) {
        super(from1, to1, ircServer);
        message = msg1;
    }
    
    /**
     * @return the message
     */
    public String getMessage() {
        return message;
    }
    
    /**
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        if (StringUtils.isEmpty(message)) {
            return null;
        }
        StringBuilder sb = new StringBuilder();
        if (super.from != null) {
            sb.append(':');
            sb.append(super.from);
            sb.append(' ');
        }
        sb.append("NOTICE ");
        if (super.to != null) {
            sb.append(super.to);
            sb.append(' ');
        }
        sb.append(':');
        sb.append(message);
        return sb.toString();
    }
}

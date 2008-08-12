package net.mauhiz.irc.base.msg;

import net.mauhiz.irc.base.data.IrcServer;

/**
 * @author mauhiz
 */
public class Invite extends IrcMessage {
    String message;
    
    /**
     * @param ircServer
     * @param msg
     */
    public Invite(final IrcServer ircServer, final String msg) {
        this(null, null, ircServer, msg);
    }
    
    /**
     * @param from1
     * @param to1
     * @param ircServer
     * @param msg1
     */
    public Invite(final String from1, final String to1, final IrcServer ircServer, final String msg1) {
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
        StringBuilder sb = new StringBuilder();
        if (super.from != null) {
            sb.append(':');
            sb.append(super.from);
            sb.append(' ');
        }
        sb.append("INVITE ");
        if (super.to != null) {
            sb.append(super.to);
            sb.append(' ');
        }
        sb.append(message);
        return sb.toString();
    }
}

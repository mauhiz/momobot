package net.mauhiz.irc.base.msg;

import net.mauhiz.irc.base.data.IrcServer;

/**
 * @author mauhiz
 */
public class Part extends IrcMessage {
    String chan;
    String reason;
    
    /**
     * @param ircServer
     * @param msg
     * @param reason1
     */
    public Part(final IrcServer ircServer, final String msg, final String reason1) {
        this(ircServer, null, null, msg, reason1);
    }
    
    /**
     * @param from1
     * @param to1
     * @param ircServer
     * @param msg1
     * @param reason1
     */
    public Part(final IrcServer ircServer, final String from1, final String to1, final String msg1, final String reason1) {
        super(from1, to1, ircServer);
        chan = msg1;
        reason = reason1;
    }
    
    /**
     * @return the message
     */
    public String getChan() {
        return chan;
    }
    
    /**
     * @return {@link #reason}
     */
    public String getReason() {
        return reason;
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
        sb.append("PART ");
        if (super.to != null) {
            sb.append(super.to);
            sb.append(' ');
        }
        sb.append(chan);
        if (reason != null) {
            sb.append(" :");
            sb.append(reason);
        }
        return sb.toString();
    }
}

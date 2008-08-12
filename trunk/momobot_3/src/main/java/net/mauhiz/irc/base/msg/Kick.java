package net.mauhiz.irc.base.msg;

import net.mauhiz.irc.base.data.IrcServer;

/**
 * @author mauhiz
 */
public class Kick extends IrcMessage {
    String chan;
    String reason;
    String target;
    
    /**
     * @param from1
     * @param to1
     * @param ircServer
     * @param chan1
     * @param target1
     * @param reason1
     */
    public Kick(final IrcServer ircServer, final String from1, final String to1, final String chan1,
            final String target1, final String reason1) {
        super(from1, to1, ircServer);
        chan = chan1;
        target = target1;
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
     * @return {@link #target}
     */
    public String getTarget() {
        return target;
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
        sb.append("KICK ");
        if (super.to != null) {
            sb.append(super.to);
            sb.append(' ');
        }
        sb.append(chan);
        sb.append(' ');
        sb.append(target);
        if (reason != null) {
            sb.append(' ');
            sb.append(reason);
        }
        return sb.toString();
    }
}

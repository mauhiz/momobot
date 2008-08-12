package net.mauhiz.irc.base.msg;

import net.mauhiz.irc.base.data.IrcServer;

/**
 * @author mauhiz
 */
public class Mode extends IrcMessage {
    String message;

    /**
     * @param from1
     * @param to1
     * @param server1
     */
    public Mode(final String from1, final String to1, final IrcServer server1) {
        super(from1, to1, server1);
    }

    /**
     * @param group
     * @param object
     * @param ircServer
     * @param group2
     */
    public Mode(final String group, final String object, final IrcServer ircServer, final String group2) {
        this(group, object, ircServer);
        this.message = group2;
    }

    /**
     * @return the message
     */
    public String getMessage() {
        return this.message;
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
        sb.append("MODE ");
        if (super.to != null) {
            sb.append(super.to);
            sb.append(' ');
        }
        sb.append(this.message);
        return sb.toString();
    }
}

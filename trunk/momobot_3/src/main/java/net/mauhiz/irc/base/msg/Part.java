package net.mauhiz.irc.base.msg;

import net.mauhiz.irc.base.data.IrcChannel;
import net.mauhiz.irc.base.data.IrcCommands;
import net.mauhiz.irc.base.data.IIrcServerPeer;
import net.mauhiz.irc.base.data.IrcUser;

import org.apache.commons.lang.StringUtils;

/**
 * @author mauhiz
 */
public class Part extends AbstractIrcMessage {
    private final String reason;

    public Part(IIrcServerPeer server, IrcChannel fromChannel, String reason) {
        this(server, null, fromChannel, reason);
    }

    public Part(IIrcServerPeer server, IrcUser partingUser, IrcChannel fromChannel, String reason) {
        super(partingUser, fromChannel, server);
        this.reason = reason;
    }

    @Override
    public Part copy() {
        return new Part(server, (IrcUser) from, (IrcChannel) to, reason);
    }

    @Override
    public String getIrcForm() {
        StringBuilder sb = new StringBuilder();
        if (super.from != null) {
            sb.append(':');
            sb.append(super.from);
            sb.append(' ');
        }
        sb.append(IrcCommands.PART).append(' ');
        sb.append(super.to);
        if (reason != null) {
            sb.append(" :");
            sb.append(reason);
        }
        return sb.toString();
    }

    /**
     * @return {@link #reason}
     */
    public String getReason() {
        return reason;
    }

    @Override
    public IrcChannel getTo() {
        return (IrcChannel) super.getTo();
    }

    @Override
    public String toString() {
        if (from == null) {
            return "* Leaving " + getTo() + (StringUtils.isEmpty(reason) ? "" : " (" + reason + ")");
        }
        return "* " + niceFromDisplay() + " has left " + getTo()
                + (StringUtils.isEmpty(reason) ? "" : " (" + reason + ")");
    }
}

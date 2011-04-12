package net.mauhiz.irc.base.msg;

import net.mauhiz.irc.base.IIrcControl;
import net.mauhiz.irc.base.data.IrcChannel;
import net.mauhiz.irc.base.data.IrcServer;
import net.mauhiz.irc.base.data.IrcUser;
import net.mauhiz.irc.base.data.Target;

import org.apache.commons.lang.StringUtils;

/**
 * @author mauhiz
 */
public class Part extends AbstractIrcMessage {
    private final String reason;

    /**
     * @param ircServer
     * @param msg
     * @param reason1
     */
    public Part(IrcServer ircServer, Target to, String reason1) {
        this(ircServer, null, to, reason1);
    }

    /**
     * @param from1
     * @param to1
     * @param ircServer
     * @param msg1
     * @param reason1
     */
    public Part(IrcServer ircServer, Target from1, Target to1, String reason1) {
        super(from1, to1, ircServer);
        reason = reason1;
    }

    @Override
    public String getIrcForm() {
        StringBuilder sb = new StringBuilder();
        if (super.from != null) {
            sb.append(':');
            sb.append(super.from);
            sb.append(' ');
        }
        sb.append("PART ");
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
    public void process(IIrcControl control) {
        IrcChannel fromChan = getTo();
        if (fromChan != null) {
            IrcUser leaver = (IrcUser) from;
            fromChan.remove(leaver);
        }
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

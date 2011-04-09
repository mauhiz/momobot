package net.mauhiz.irc.base.msg;

import net.mauhiz.irc.base.IIrcControl;
import net.mauhiz.irc.base.data.IrcChannel;
import net.mauhiz.irc.base.data.IrcServer;
import net.mauhiz.irc.base.data.IrcUser;
import net.mauhiz.irc.base.data.Mask;

/**
 * @author mauhiz
 */
public class Part extends AbstractIrcMessage {
    private final String chan;
    private final String reason;

    /**
     * @param ircServer
     * @param msg
     * @param reason1
     */
    public Part(IrcServer ircServer, String msg, String reason1) {
        this(ircServer, null, null, msg, reason1);
    }

    /**
     * @param from1
     * @param to1
     * @param ircServer
     * @param msg1
     * @param reason1
     */
    public Part(IrcServer ircServer, String from1, String to1, String msg1, String reason1) {
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

    @Override
    public String getIrcForm() {
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

    /**
     * @return {@link #reason}
     */
    public String getReason() {
        return reason;
    }

    @Override
    public void process(IIrcControl control) {
        IrcChannel fromChan = server.findChannel(chan);
        if (fromChan != null) {
            Mask mask = new Mask(from);
            IrcUser leaver = server.findUser(mask, true);
            fromChan.remove(leaver);
        }
    }

    @Override
    public String toString() {
        return "* Parts: " + chan + (reason == null ? "" : "(" + reason + ")");
    }
}

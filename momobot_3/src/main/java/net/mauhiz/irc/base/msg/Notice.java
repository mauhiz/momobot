package net.mauhiz.irc.base.msg;

import net.mauhiz.irc.base.IIrcControl;
import net.mauhiz.irc.base.data.IrcServer;
import net.mauhiz.irc.base.data.IrcUser;
import net.mauhiz.irc.base.data.Mask;

import org.apache.commons.lang.StringUtils;

/**
 * @author mauhiz
 */
public class Notice extends AbstractIrcMessage {
    /**
     * @param toReply
     * @param msg
     * @return answer
     */
    public static Notice buildAnswer(IIrcMessage toReply, String msg) {
        if (toReply.isToChannel()) {
            return new Notice(null, toReply.getTo(), toReply.getServer(), msg);
        }
        return buildPrivateAnswer(toReply, msg);
    }

    /**
     * @param toReply
     * @param msg
     * @return private answer
     */
    public static Notice buildPrivateAnswer(IIrcMessage toReply, String msg) {
        return new Notice(null, toReply.getFrom(), toReply.getServer(), msg);
    }

    private final String message;

    /**
     * @param from1
     * @param to1
     * @param ircServer
     * @param msg1
     */
    public Notice(String from1, String to1, IrcServer ircServer, String msg1) {
        super(from1, to1, ircServer);
        message = msg1;
    }

    @Override
    public String getIrcForm() {
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
            if (super.from == null && !isToChannel()) {
                try {
                    Mask m = new Mask(super.to);
                    IrcUser dest = super.server.findUser(m, true);
                    sb.append(dest.getNick());
                } catch (IllegalArgumentException iae) {
                    sb.append(super.to);
                }
            } else {
                sb.append(super.to);
            }
            sb.append(' ');
        }
        sb.append(':');
        sb.append(message);
        return sb.toString();
    }

    /**
     * @return the message
     */
    public String getMessage() {
        return message;
    }

    @Override
    public void process(IIrcControl control) {
        // nothing to do here
    }

    @Override
    public String toString() {
        if (from == null) {
            return "Noticing: " + message;
        }
        return "-" + niceFromDisplay() + "- " + message;
    }
}

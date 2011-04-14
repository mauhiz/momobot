package net.mauhiz.irc.base.msg;

import net.mauhiz.irc.base.data.IrcCommands;
import net.mauhiz.irc.base.data.IIrcServerPeer;
import net.mauhiz.irc.base.data.Target;

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
            return new Notice(null, toReply.getTo(), toReply.getServerPeer(), msg);
        }
        return buildPrivateAnswer(toReply, msg);
    }

    /**
     * @param toReply
     * @param msg
     * @return private answer
     */
    public static Notice buildPrivateAnswer(IIrcMessage toReply, String msg) {
        return new Notice(null, toReply.getFrom(), toReply.getServerPeer(), msg);
    }

    private final String message;

    /**
     * @param from
     * @param to
     * @param server
     * @param msg
     */
    public Notice(Target from, Target to, IIrcServerPeer server, String msg) {
        super(from, to, server);
        message = msg;
    }

    @Override
    public Notice copy() {
        return new Notice(from, to, server, message);
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
        sb.append(IrcCommands.NOTICE).append(' ');
        sb.append(super.to);
        sb.append(" :");
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
    public String toString() {
        if (from == null) {
            return "Noticing: " + message;
        }
        return "-" + niceFromDisplay() + "- " + message;
    }
}

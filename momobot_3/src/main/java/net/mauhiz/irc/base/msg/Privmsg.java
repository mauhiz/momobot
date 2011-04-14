package net.mauhiz.irc.base.msg;

import net.mauhiz.irc.base.data.IrcCommands;
import net.mauhiz.irc.base.data.IIrcServerPeer;
import net.mauhiz.irc.base.data.Target;

import org.apache.commons.lang.StringUtils;

/**
 * @author mauhiz
 */
public class Privmsg extends AbstractIrcMessage {
    /**
     * @param toReply
     * @param msg
     * @return new msg
     */
    public static Privmsg buildAnswer(IIrcMessage toReply, String msg) {
        if (toReply.isToChannel()) {
            return new Privmsg(null, toReply.getTo(), toReply.getServerPeer(), msg);
        }
        return buildPrivateAnswer(toReply, msg);
    }

    /**
     * @param toReply
     * @param msg
     * @return new msg
     */
    public static Privmsg buildPrivateAnswer(IIrcMessage toReply, String msg) {
        return new Privmsg(null, toReply.getFrom(), toReply.getServerPeer(), msg);
    }

    /**
     * message
     */
    private final String message;

    /**
     * TODO constr private ?
     */
    public Privmsg(Target from, Target to, IIrcServerPeer server, String message) {
        super(from, to, server);
        this.message = message;
    }

    @Override
    public Privmsg copy() {
        return new Privmsg(from, to, server, message);
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
        sb.append(IrcCommands.PRIVMSG).append(' ');
        sb.append(super.to);
        sb.append(" :");
        sb.append(getMessage()); // important pour les sous classes (CTCP, ACTION...)
        return sb.toString();
    }

    /**
     * @return le message
     */
    public String getMessage() {
        return message;
    }

    @Override
    public String toString() {
        if (from == null) { // self
            return "Saying to " + to + " : " + message;
        }
        return "<" + niceFromDisplay() + "> " + message;
    }
}

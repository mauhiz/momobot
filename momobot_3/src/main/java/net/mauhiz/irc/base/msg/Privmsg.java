package net.mauhiz.irc.base.msg;

import net.mauhiz.irc.MomoStringUtils;
import net.mauhiz.irc.base.IIrcControl;
import net.mauhiz.irc.base.data.IrcServer;
import net.mauhiz.irc.base.data.IrcUser;
import net.mauhiz.irc.base.data.Mask;

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
        String oldDest = toReply.getTo();
        if (MomoStringUtils.isChannelName(oldDest)) {
            return new Privmsg(null, oldDest, toReply.getServer(), msg);
        }
        return buildPrivateAnswer(toReply, msg);
    }
    
    /**
     * @param toReply
     * @param msg
     * @return new msg
     */
    public static Privmsg buildPrivateAnswer(IIrcMessage toReply, String msg) {
        String from = toReply.getFrom();
        Mask mask = new Mask(from);
        return new Privmsg(null, mask.getNick(), toReply.getServer(), msg);
    }
    
    /**
     * message
     */
    private final String message;
    
    /**
     * TODO constr private
     * 
     * @param from1
     * @param to1
     * @param server1
     * @param message1
     */
    public Privmsg(String from1, String to1, IrcServer server1, String message1) {
        super(from1, to1, server1);
        message = message1;
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
        sb.append("PRIVMSG ");
        if (super.to != null) {
            if (super.from == null && !MomoStringUtils.isChannelName(super.to)) {
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
    public void process(IIrcControl control) {
        // nothing to do
    }
    
    @Override
    public String toString() {
        return "<" + from + "> " + message;
    }
}

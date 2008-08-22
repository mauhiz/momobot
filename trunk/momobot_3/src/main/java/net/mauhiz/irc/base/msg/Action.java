package net.mauhiz.irc.base.msg;

import net.mauhiz.irc.base.data.IrcServer;
import net.mauhiz.irc.base.data.IrcUser;
import net.mauhiz.irc.base.data.Mask;
import net.mauhiz.irc.base.model.Channels;
import net.mauhiz.irc.base.model.Users;

import org.apache.commons.lang.StringUtils;

/**
 * @author mauhiz
 */
public class Action extends Privmsg {
    
    /**
     * @param toReply
     * @param msg
     * @return MeMsg
     */
    public static Action buildAnswer(final IrcMessage toReply, final String msg) {
        String oldDest = toReply.getTo();
        if (Channels.isChannelName(oldDest)) {
            return new Action(null, oldDest, toReply.getServer(), msg);
        }
        return buildPrivateAnswer(toReply, msg);
    }
    
    public static Action buildPrivateAnswer(final IrcMessage toReply, final String msg) {
        String from = toReply.getFrom();
        Mask mask = new Mask(from);
        return new Action(null, mask.getNick(), toReply.getServer(), msg);
    }
    
    /**
     * TODO constr private
     * 
     * @param from1
     * @param to1
     * @param server1
     * @param message1
     */
    public Action(final String from1, final String to1, final IrcServer server1, final String message1) {
        super(from1, to1, server1, message1);
    }
    
    /**
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
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
            if (super.from == null && !Channels.isChannelName(super.to)) {
                try {
                    Mask m = new Mask(super.to);
                    IrcUser dest = Users.getInstance(super.server).findUser(m, true);
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
        sb.append((char) 01 + "ACTION ");
        sb.append(message);
        sb.append((char) 01);
        return sb.toString();
    }
}

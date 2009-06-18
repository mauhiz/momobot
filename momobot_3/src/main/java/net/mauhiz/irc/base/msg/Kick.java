package net.mauhiz.irc.base.msg;

import net.mauhiz.irc.base.IIrcControl;
import net.mauhiz.irc.base.data.IrcChannel;
import net.mauhiz.irc.base.data.IrcServer;
import net.mauhiz.irc.base.data.IrcUser;

/**
 * @author mauhiz
 */
public class Kick extends AbstractIrcMessage {
    private final String chan;
    private final String reason;
    private final String target;
    
    /**
     * @param from1
     * @param to1
     * @param ircServer
     * @param chan1
     * @param target1
     * @param reason1
     */
    public Kick(IrcServer ircServer, String from1, String to1, String chan1, String target1, String reason1) {
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
    
    @Override
    public String getIrcForm() {
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
    
    /**
     * @return {@link #target}
     */
    public String getTarget() {
        return target;
    }
    
    @Override
    public void process(IIrcControl control) {
        IrcChannel fromChan = server.findChannel(chan);
        if (fromChan != null) {
            IrcUser kicked = server.findUser(target, true);
            fromChan.remove(kicked);
        }
    }
}

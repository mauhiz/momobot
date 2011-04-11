package net.mauhiz.irc.base.msg;

import net.mauhiz.irc.base.IIrcControl;
import net.mauhiz.irc.base.data.IrcServer;

/**
 * @author mauhiz
 */
public class Invite extends AbstractIrcMessage {
    private final String message;

    /**
     * @param ircServer
     * @param msg
     */
    public Invite(IrcServer ircServer, String msg) {
        this(null, null, ircServer, msg);
    }

    /**
     * @param from1
     * @param to1
     * @param ircServer
     * @param msg1
     */
    public Invite(String from1, String to1, IrcServer ircServer, String msg1) {
        super(from1, to1, ircServer);
        message = msg1;
    }

    @Override
    public String getIrcForm() {
        StringBuilder sb = new StringBuilder();
        if (super.from != null) {
            sb.append(':');
            sb.append(super.from);
            sb.append(' ');
        }
        sb.append("INVITE ");
        if (super.to != null) {
            sb.append(super.to);
            sb.append(' ');
        }
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
        // nothing to do
    }

    @Override
    public String toString() {
        // TODO Auto-generated method stub
        return super.toString();
    }
}

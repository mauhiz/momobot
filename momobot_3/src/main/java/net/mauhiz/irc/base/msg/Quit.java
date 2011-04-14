package net.mauhiz.irc.base.msg;

import net.mauhiz.irc.base.data.IrcCommands;
import net.mauhiz.irc.base.data.IIrcServerPeer;
import net.mauhiz.irc.base.data.Target;

/**
 * @author mauhiz
 */
public class Quit extends AbstractIrcMessage {
    private final String message;

    /**
     * @param server
     * @param msg
     */
    public Quit(IIrcServerPeer server, String msg) {
        this(null, server, msg);
    }

    public Quit(Target from, IIrcServerPeer server, String reason) {
        super(from, null, server);
        message = reason;
    }

    @Override
    public Quit copy() {
        return new Quit(from, server, message);
    }

    @Override
    public String getIrcForm() {
        StringBuilder sb = new StringBuilder();
        if (super.from != null) {
            sb.append(':');
            sb.append(super.from);
            sb.append(' ');
        }
        sb.append(IrcCommands.QUIT);
        if (message != null) {
            sb.append(" :").append(message);
        }

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
        return "* Quits: " + niceFromDisplay() + " (" + message + ")";
    }
}

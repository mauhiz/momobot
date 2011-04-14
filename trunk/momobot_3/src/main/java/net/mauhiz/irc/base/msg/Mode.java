package net.mauhiz.irc.base.msg;

import net.mauhiz.irc.base.data.IrcCommands;
import net.mauhiz.irc.base.data.IIrcServerPeer;
import net.mauhiz.irc.base.data.Target;

/**
 * @author mauhiz
 */
public class Mode extends AbstractIrcMessage {

    public static boolean isModifier(char c) {
        return c == '+' || c == '-';
    }

    private final String modeQuery;

    public Mode(Target from, Target modifiedObject, IIrcServerPeer server) {
        this(from, modifiedObject, server, null);
    }

    /**
     * TODO refine modeQuery?
     */
    public Mode(Target from, Target modifiedObject, IIrcServerPeer server, String modeQuery) {
        super(from, modifiedObject, server);
        this.modeQuery = modeQuery;
    }

    @Override
    public Mode copy() {
        return new Mode(from, to, server, modeQuery);
    }

    @Override
    public String getIrcForm() {
        StringBuilder sb = new StringBuilder();
        if (super.from != null) {
            sb.append(':');
            sb.append(super.from);
            sb.append(' ');
        }
        sb.append(IrcCommands.MODE).append(' ');
        sb.append(super.to).append(' ');
        sb.append(modeQuery);
        return sb.toString();
    }

    /**
     * @return the message
     */
    public String getMessage() {
        return modeQuery;
    }

    /**
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        return "* " + niceFromDisplay() + " sets mode: " + modeQuery + " " + to;
    }
}

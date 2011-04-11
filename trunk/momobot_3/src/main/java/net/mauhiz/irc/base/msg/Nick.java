package net.mauhiz.irc.base.msg;

import net.mauhiz.irc.base.IIrcControl;
import net.mauhiz.irc.base.data.IrcServer;
import net.mauhiz.irc.base.data.IrcUser;
import net.mauhiz.irc.base.data.Mask;

/**
 * @author mauhiz
 */
public class Nick extends AbstractIrcMessage {
    /**
     * le nouveau nick
     */
    private final String newNick;

    /**
     * @param server1
     */
    public Nick(IrcServer server1) {
        this(server1, null, server1.getMyself().getNick());
    }

    /**
     * @param server1
     * @param from1
     * @param newNick1
     */
    public Nick(IrcServer server1, String from1, String newNick1) {
        super(from1, null, server1);
        newNick = newNick1;
    }

    @Override
    public String getIrcForm() {
        StringBuilder sb = new StringBuilder();
        if (super.from != null) {
            sb.append(':');
            sb.append(super.from);
            sb.append(' ');
        }
        sb.append("NICK ");
        sb.append(newNick);
        return sb.toString();
    }

    /**
     * @return {@link #newNick}
     */
    public String getNewNick() {
        return newNick;
    }

    @Override
    public void process(IIrcControl control) {
        Mask fromMask = new Mask(from);
        IrcUser target = server.findUser(fromMask, true);
        server.updateNick(target, newNick);
    }

    /**
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        if (from == null) {
            return "* Setting new nickname " + newNick;
        }
        return "* " + niceFromDisplay() + " is now known as " + newNick;
    }
}

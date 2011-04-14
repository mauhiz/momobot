package net.mauhiz.irc.base.msg;

import net.mauhiz.irc.base.data.IrcCommands;
import net.mauhiz.irc.base.data.IIrcServerPeer;
import net.mauhiz.irc.base.data.Target;

/**
 * @author mauhiz
 */
public class Nick extends AbstractIrcMessage {
    /**
     * le nouveau nick
     */
    private final String newNick;

    public Nick(IIrcServerPeer server) {
        this(server, null, server.getMyself().getNick());
    }

    public Nick(IIrcServerPeer server, Target from, String newNick) {
        super(from, null, server);
        this.newNick = newNick;
    }

    @Override
    public Nick copy() {
        return new Nick(server, from, newNick);
    }

    @Override
    public String getIrcForm() {
        StringBuilder sb = new StringBuilder();
        if (super.from != null) {
            sb.append(':');
            sb.append(super.from);
            sb.append(' ');
        }
        sb.append(IrcCommands.NICK).append(' ').append(newNick);
        return sb.toString();
    }

    /**
     * @return {@link #newNick}
     */
    public String getNewNick() {
        return newNick;
    }

    @Override
    public String toString() {
        if (from == null) {
            return "* Setting new nickname " + newNick;
        }
        return "* " + niceFromDisplay() + " is now known as " + newNick;
    }
}

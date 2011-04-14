package net.mauhiz.irc.base.msg;

import net.mauhiz.irc.base.data.IrcCommands;
import net.mauhiz.irc.base.data.IIrcServerPeer;

/**
 * @author mauhiz
 */
public class Pong extends AbstractIrcMessage {
    private final String pingId;

    public Pong(IIrcServerPeer server, String pingId) {
        super(null, null, server);
        this.pingId = pingId;
    }

    @Override
    public Pong copy() {
        return new Pong(server, pingId);
    }

    @Override
    public String getIrcForm() {
        return IrcCommands.PONG + " " + pingId;
    }

    @Override
    public String toString() {
        return "Answering PING: " + pingId;
    }
}

package net.mauhiz.irc.base.msg;

import net.mauhiz.irc.base.data.IrcCommands;
import net.mauhiz.irc.base.data.IIrcServerPeer;
import net.mauhiz.irc.base.data.Target;

/**
 * @author mauhiz
 */
public class Ping extends AbstractIrcMessage {
    private final String pingId;

    public Ping(Target from, IIrcServerPeer server, String pingId) {
        super(from, null, server);
        this.pingId = pingId;
    }

    @Override
    public Ping copy() {
        return new Ping(from, server, pingId);
    }

    @Override
    public String getIrcForm() {
        return IrcCommands.PING + " " + pingId;
    }

    /**
     * @return the pingId
     */
    public String getPingId() {
        return pingId;
    }

    /**
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        return "Answering PING: " + pingId;
    }
}

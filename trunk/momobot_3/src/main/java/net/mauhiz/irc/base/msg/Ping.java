package net.mauhiz.irc.base.msg;

import net.mauhiz.irc.base.IIrcControl;
import net.mauhiz.irc.base.data.IrcServer;

/**
 * @author mauhiz
 */
public class Ping extends AbstractIrcMessage {
    private final String pingId;

    /**
     * @param from1
     * @param to1
     * @param server1
     * @param pingId1
     */
    public Ping(String from1, String to1, IrcServer server1, String pingId1) {
        super(from1, to1, server1);
        pingId = pingId1;
    }

    @Override
    public String getIrcForm() {
        return "PING " + pingId;
    }

    /**
     * @return the pingId
     */
    public String getPingId() {
        return pingId;
    }

    @Override
    public void process(IIrcControl control) {
        control.sendMsg(new Pong(server, pingId));
    }

    /**
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        return "Answering PING: " + pingId;
    }
}

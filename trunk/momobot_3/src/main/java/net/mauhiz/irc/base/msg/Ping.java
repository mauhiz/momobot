package net.mauhiz.irc.base.msg;

import net.mauhiz.irc.base.data.IrcServer;

/**
 * @author mauhiz
 */
public class Ping extends IrcMessage {
    private final String pingId;
    
    /**
     * @param from1
     * @param to1
     * @param server1
     * @param pingId1
     */
    public Ping(final String from1, final String to1, final IrcServer server1, final String pingId1) {
        super(from1, to1, server1);
        pingId = pingId1;
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
        return "PING " + getPingId();
    }
}

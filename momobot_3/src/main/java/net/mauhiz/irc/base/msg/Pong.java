package net.mauhiz.irc.base.msg;

import net.mauhiz.irc.base.data.IrcServer;

/**
 * @author mauhiz
 */
public class Pong extends IrcMessage {
    String pingId;
    
    /**
     * @param server1
     * @param pingId1
     */
    public Pong(final IrcServer server1, final String pingId1) {
        super(null, null, server1);
        pingId = pingId1;
    }
    
    /**
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        return "PONG " + pingId;
    }
}

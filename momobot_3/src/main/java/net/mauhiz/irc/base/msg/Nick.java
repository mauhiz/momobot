package net.mauhiz.irc.base.msg;

import net.mauhiz.irc.base.data.IrcServer;

/**
 * @author mauhiz
 */
public class Nick extends IrcMessage {
    String nick;
    
    /**
     * @param server1
     */
    public Nick(final IrcServer server1) {
        this(server1, server1.getMyNick());
    }
    
    /**
     * @param server1
     * @param newNick
     */
    public Nick(final IrcServer server1, final String newNick) {
        super(null, null, server1);
        nick = newNick;
    }
    
    /**
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        return "NICK " + nick;
    }
}

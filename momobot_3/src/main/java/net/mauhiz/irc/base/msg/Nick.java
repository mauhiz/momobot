package net.mauhiz.irc.base.msg;

import net.mauhiz.irc.base.data.IrcServer;

/**
 * @author mauhiz
 */
public class Nick extends IrcMessage {
    /**
     * le nouveau nick
     */
    private final String newNick;
    
    /**
     * @param server1
     */
    public Nick(final IrcServer server1) {
        this(server1, null, server1.getMyNick());
    }
    
    /**
     * @param server1
     * @param from1
     * @param newNick1
     */
    public Nick(final IrcServer server1, final String from1, final String newNick1) {
        super(from1, null, server1);
        newNick = newNick1;
    }
    
    /**
     * @return {@link #newNick}
     */
    public String getNewNick() {
        return newNick;
    }
    
    /**
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
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
}

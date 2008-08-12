package net.mauhiz.irc.base.msg;

import net.mauhiz.irc.base.data.IrcServer;

/**
 * @author mauhiz
 */
public class Whois extends IrcMessage {
    
    private String target;
    
    /**
     * @param from1
     * @param to1
     * @param server1
     * @param target1
     */
    public Whois(final String from1, final String to1, final IrcServer server1, final String target1) {
        super(from1, to1, server1);
        target = target1;
    }
    
    /**
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        return "WHOIS " + target + ' ';
    }
    
}

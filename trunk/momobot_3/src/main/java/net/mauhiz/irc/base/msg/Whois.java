package net.mauhiz.irc.base.msg;

import net.mauhiz.irc.base.IIrcControl;
import net.mauhiz.irc.base.data.IrcServer;

/**
 * @author mauhiz
 */
public class Whois extends AbstractIrcMessage {
    
    private final String target;
    
    /**
     * @param from1
     * @param to1
     * @param server1
     * @param target1
     */
    public Whois(String from1, String to1, IrcServer server1, String target1) {
        super(from1, to1, server1);
        target = target1;
    }
    
    @Override
    public String getIrcForm() {
        return "WHOIS " + target + ' ';
    }
    
    @Override
    public void process(IIrcControl control) {
        throw new UnsupportedOperationException("I should not receive WHOIS msg");
    }
}

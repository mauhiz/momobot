package net.mauhiz.irc.base.msg;

import net.mauhiz.irc.base.IIrcControl;
import net.mauhiz.irc.base.data.IrcServer;

/**
 * @author mauhiz
 */
public class Pong extends AbstractIrcMessage {
    private final String pingId;
    
    /**
     * @param server1
     * @param pingId1
     */
    public Pong(IrcServer server1, String pingId1) {
        super(null, null, server1);
        pingId = pingId1;
    }
    
    @Override
    public String getIrcForm() {
        return "PONG " + pingId;
    }
    
    @Override
    public void process(IIrcControl control) {
        throw new UnsupportedOperationException("I should not received PONG msg");
    }
}

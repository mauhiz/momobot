package net.mauhiz.irc.base;

import net.mauhiz.irc.base.data.IrcServer;

/**
 * @author mauhiz
 */
public interface IIrcClientControl extends IIrcControl {
    /**
     * @param server
     */
    void connect(IrcServer server);
    
    void quit(IrcServer server);
}

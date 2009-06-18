package net.mauhiz.irc.base.msg;

import net.mauhiz.irc.base.IIrcControl;
import net.mauhiz.irc.base.data.IrcServer;

/**
 * @author mauhiz
 */
public interface IIrcMessage {
    /**
     * @return from
     */
    String getFrom();
    
    /**
     * @return message prêt à être utilise sur IRC
     */
    String getIrcForm();
    
    /**
     * @return server
     */
    IrcServer getServer();
    
    /**
     * @return to
     */
    String getTo();
    
    /**
     * @param control
     */
    void process(IIrcControl control);
}

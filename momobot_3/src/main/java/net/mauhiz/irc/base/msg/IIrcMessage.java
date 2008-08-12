package net.mauhiz.irc.base.msg;

import net.mauhiz.irc.base.data.IrcServer;

/**
 * @author mauhiz
 */
public interface IIrcMessage {
    String getFrom();

    /**
     * @return
     */
    IrcServer getServer();

    String getTo();
}

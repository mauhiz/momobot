package net.mauhiz.irc.base.msg;

import net.mauhiz.irc.base.IIrcControl;
import net.mauhiz.irc.base.data.IrcServer;
import net.mauhiz.irc.base.data.Target;

/**
 * @author mauhiz
 */
public interface IIrcMessage {
    /**
     * @return from
     */
    Target getFrom();

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
    Target getTo();

    boolean isToChannel();

    /**
     * @param control
     */
    void process(IIrcControl control);

    /**
     * @return message lisible par l'utilisateur
     */
    @Override
    String toString();
}

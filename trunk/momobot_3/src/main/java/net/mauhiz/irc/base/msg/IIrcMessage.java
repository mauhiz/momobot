package net.mauhiz.irc.base.msg;

import net.mauhiz.irc.base.data.IIrcServerPeer;
import net.mauhiz.irc.base.data.Target;

/**
 * @author mauhiz
 */
public interface IIrcMessage {

    /**
     * @return a duplicate of this message
     */
    IIrcMessage copy();

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
    IIrcServerPeer getServerPeer();

    /**
     * @return message lisible par l'utilisateur
     */
    @Override
    String toString();
}

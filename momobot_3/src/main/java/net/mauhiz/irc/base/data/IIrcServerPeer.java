package net.mauhiz.irc.base.data;

/**
 * Represents a server to connect to
 * @author mauhiz
 */
public interface IIrcServerPeer extends IrcPeer, Target {

    IrcUser getMyself();

    IrcUser introduceMyself(String nick, String user, String fullName);

    void setIrcForm(String ircForm);
}

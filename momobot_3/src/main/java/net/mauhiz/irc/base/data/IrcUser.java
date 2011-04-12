package net.mauhiz.irc.base.data;

/**
 * @author mauhiz
 */
public interface IrcUser extends Comparable<IrcUser>, Target {
    String getFullName();

    HostMask getMask();

    String getNick();

    UserProperties getProperties();

    /**
     * @return si je suis un service du reseau
     */
    boolean isService();

    void setFullName(String fullName);

    void setMask(HostMask mask);

    void setNick(String newNick);
}

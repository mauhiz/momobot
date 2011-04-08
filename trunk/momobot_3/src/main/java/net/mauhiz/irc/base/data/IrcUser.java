package net.mauhiz.irc.base.data;

/**
 * @author mauhiz
 */
public interface IrcUser extends Comparable<IrcUser> {
    String getFullName();

    String getHost();

    String getNick();

    UserProperties getProperties();

    String getUser();

    /**
     * @return si je suis un service du reseau
     */
    boolean isService();

    void setFullName(String fullName);

    void setHost(String string);

    void setNick(String newNick);

    void setUser(String string);

    void updateWithMask(Mask mask);
}

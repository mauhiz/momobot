package net.mauhiz.irc.base.data;

import java.net.URI;
import java.util.Collection;
import java.util.Set;

/**
 * @author mauhiz
 * 
 */
public interface IrcNetwork {

    int countUsers();

    /**
     * @param chanName
     * @return
     */
    IrcChannel findChannel(String chanName);

    /**
     * @param chanName
     * @param addIfNotFound
     * @return
     */
    IrcChannel findChannel(String chanName, boolean addIfNotFound);

    /**
     * @param mask
     * @param addIfNotFound
     * @return
     */
    IrcUser findUser(HostMask mask, boolean addIfNotFound);

    /**
     * @param target
     * @param addIfNotFound
     * @return
     */
    IrcUser findUser(String target, boolean addIfNotFound);

    String getAlias();

    Iterable<IrcChannel> getChannels();

    /**
     * @param smith
     * @return
     */
    Set<IrcChannel> getChannelsForUser(IrcUser smith);

    int getLineMaxLength();

    Collection<String> getServiceNicks();

    IrcChannel newChannel(String chanLowerCase);

    IIrcServerPeer newServerPeer();

    IIrcServerPeer newServerPeer(URI uri);

    IrcUser newUser(String nick);

    /**
     * @param channel
     */
    void remove(IrcChannel channel);

    /**
     * @param quitter
     */
    void remove(IrcUser quitter);

    void setDefaultUri(URI defaultUri);

    /**
     * @param target
     * @param newNick
     */
    void updateNick(IrcUser target, String newNick);
}

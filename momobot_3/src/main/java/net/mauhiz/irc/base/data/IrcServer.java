package net.mauhiz.irc.base.data;

import java.util.Collection;
import java.util.Set;

/**
 * @author mauhiz
 * 
 */
public interface IrcServer extends IrcPeer {
    
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
    IrcUser findUser(Mask mask, boolean addIfNotFound);
    
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
    
    IrcUser getMyself();
    
    Collection<String> getServiceNicks();
    
    IrcChannel newChannel(String chanLowerCase);
    
    IrcUser newUser(String nick);
    
    /**
     * @param channel
     */
    void remove(IrcChannel channel);
    
    /**
     * @param quitter
     */
    void remove(IrcUser quitter);
    
    /**
     * @param alias
     */
    void setAlias(String alias);
    
    /**
     * @param me
     */
    void setMyself(IrcUser me);
    
    /**
     * @param target
     * @param newNick
     */
    void updateNick(IrcUser target, String newNick);
}

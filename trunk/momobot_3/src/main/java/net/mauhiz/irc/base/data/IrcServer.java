package net.mauhiz.irc.base.data;

import java.net.InetSocketAddress;
import java.util.Set;

import net.mauhiz.irc.base.msg.IIrcMessage;

/**
 * @author mauhiz
 * 
 */
public interface IrcServer {
    
    /**
     * @param test
     * @return
     */
    IIrcMessage buildFromRaw(String test);
    
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
    
    InetSocketAddress getAddress();
    
    String getAlias();
    
    Iterable<IrcChannel> getChannels();
    
    /**
     * @param smith
     * @return
     */
    Set<IrcChannel> getChannelsForUser(IrcUser smith);
    
    IrcUser getMyself();
    
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
    
    public abstract IrcChannel newChannel(String chanLowerCase);
    
    public abstract IrcUser newUser(String nick);
}

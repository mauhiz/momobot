package net.mauhiz.irc.base.data;

import java.net.InetSocketAddress;
import java.util.Set;

import net.mauhiz.irc.base.msg.IIrcMessage;

/**
 * @author mauhiz
 * 
 */
public interface IrcServer extends IHookable<IrcServer> {
    
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
     * @param b
     * @return
     */
    IrcChannel findChannel(String chanName, boolean b);
    
    /**
     * @param mask
     * @param b
     * @return
     */
    IrcUser findUser(Mask mask, boolean b);
    
    /**
     * @param target
     * @param b
     * @return
     */
    IrcUser findUser(String target, boolean b);
    
    InetSocketAddress getAddress();
    
    String getAlias();
    
    Iterable<IrcChannel> getChannels();
    
    /**
     * @param smith
     * @return
     */
    Set<IrcChannel> getChannelsForUser(IrcUser smith);
    
    String getMyFullName();
    
    String getMyLogin();
    
    String getMyNick();
    
    /**
     * @param channel
     */
    void remove(final IrcChannel channel);
    /**
     * @param quitter
     */
    void remove(IrcUser quitter);
    
    /**
     * @param alias
     */
    void setAlias(String alias);
    
    /**
     * @param fullName
     */
    void setMyFullName(String fullName);
    
    /**
     * @param login
     */
    void setMyLogin(String login);
    
    /**
     * @param username
     */
    void setMyNick(String username);
    
    /**
     * @param target
     * @param newNick
     */
    void updateNick(IrcUser target, String newNick);
}

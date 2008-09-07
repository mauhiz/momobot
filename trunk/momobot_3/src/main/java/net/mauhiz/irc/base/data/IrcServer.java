package net.mauhiz.irc.base.data;

import java.net.InetSocketAddress;
import java.util.Set;

import net.mauhiz.irc.base.msg.IIrcMessage;

/**
 * @author mauhiz
 * 
 */
public interface IrcServer extends IHookable<IrcServer> {
    
    IIrcMessage buildFromRaw(String test);
    
    int countUsers();
    
    IrcChannel findChannel(String chanName);
    
    IrcChannel findChannel(String chanName, boolean b);
    
    IrcUser findUser(Mask mask, boolean b);
    
    IrcUser findUser(String target, boolean b);
    
    InetSocketAddress getAddress();
    
    String getAlias();
    
    Iterable<IrcChannel> getChannels();
    
    Set<IrcChannel> getChannelsForUser(IrcUser peon2);
    
    String getMyFullName();
    
    String getMyLogin();
    
    String getMyNick();
    
    void remove(IrcUser quitter);
    
    void setAlias(String string);
    
    void setMyFullName(String string);
    
    void setMyLogin(String username);
    
    void setMyNick(String username);
    
    void updateNick(IrcUser target, String newNick);
}

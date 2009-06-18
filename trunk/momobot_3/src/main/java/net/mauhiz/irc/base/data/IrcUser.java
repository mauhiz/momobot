package net.mauhiz.irc.base.data;

import java.io.Serializable;

/**
 * @author mauhiz
 */
public interface IrcUser extends Serializable, Comparable<IrcUser> {
    String getFullName();
    
    String getHost();
    
    String getNick();
    
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

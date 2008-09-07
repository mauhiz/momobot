package net.mauhiz.irc.base.data;

/**
 * @author mauhiz
 * 
 */
public interface IrcUser extends IHookable<IrcUser> {
    
    String getHost();
    
    String getNick();
    
    String getUser();
    
    /**
     * @return si je suis un service du reseau
     */
    boolean isService();
    
    void setNick(String newNick);
    
    void updateWithMask(Mask mask);
    
}

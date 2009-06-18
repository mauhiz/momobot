package net.mauhiz.irc.base.data;

import org.apache.commons.lang.NullArgumentException;

/**
 * @author mauhiz
 */
public abstract class AbstractIrcUser implements IrcUser {
    private String fullName;
    private String host;
    private String nick;
    protected UserProperties props;
    private String user;
    
    /**
     * @param nick1
     */
    protected AbstractIrcUser(String nick1) {
        nick = nick1;
    }
    
    @Override
    public int compareTo(IrcUser o) {
        return nick.compareTo(o.getNick());
    }
    
    /**
     * @see java.lang.Object#equals(java.lang.Object)
     */
    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        } else if (!(obj instanceof IrcUser)) {
            return false;
        }
        return nick.equals(((IrcUser) obj).getNick());
    }
    
    /**
     * @see net.mauhiz.irc.base.data.IrcUser#getFullName()
     */
    public String getFullName() {
        return fullName;
    }
    
    /**
     * @see net.mauhiz.irc.base.data.IrcUser#getHost()
     */
    public String getHost() {
        return host;
    }
    
    /**
     * @see net.mauhiz.irc.base.data.IrcUser#getNick()
     */
    public String getNick() {
        return nick;
    }
    
    /**
     * @return {@link #props}
     */
    public UserProperties getProps() {
        return props;
    }
    
    /**
     * @see net.mauhiz.irc.base.data.IrcUser#getUser()
     */
    public String getUser() {
        return user;
    }
    
    /**
     * @see java.lang.Object#hashCode()
     */
    @Override
    public int hashCode() {
        return nick.hashCode();
    }
    
    /**
     * Par defaut : pas de service
     * 
     * @see net.mauhiz.irc.base.data.IrcUser#isService()
     */
    public boolean isService() {
        return false;
    }
    
    /**
     * @see net.mauhiz.irc.base.data.IrcUser#setFullName(java.lang.String)
     */
    public void setFullName(String fullName) {
        this.fullName = fullName;
    }
    
    /**
     * @see net.mauhiz.irc.base.data.IrcUser#setHost(java.lang.String)
     */
    public void setHost(String host1) {
        host = host1;
    }
    
    /**
     * @see net.mauhiz.irc.base.data.IrcUser#setNick(java.lang.String)
     */
    public void setNick(String nick1) {
        if (nick == null) {
            throw new NullArgumentException("nick");
        }
        
        nick = nick1;
    }
    
    /**
     * @see net.mauhiz.irc.base.data.IrcUser#setUser(java.lang.String)
     */
    public void setUser(String user1) {
        user = user1;
    }
    
    @Override
    public String toString() {
        return nick;
    }
    
    /**
     * @param hostmask
     */
    public void updateWithMask(Mask hostmask) {
        host = hostmask.getHost();
        if ("*".equals(host)) {
            host = null;
        }
        user = hostmask.getUser();
        if ("*".equals(user)) {
            user = null;
        }
    }
}

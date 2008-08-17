package net.mauhiz.irc.base.data;

/**
 * @author mauhiz
 */
public class IrcUser {
    private String host;
    private String nick;
    private final UserProperties props = new UserProperties();
    private String user;
    
    /**
     * @param nick1
     */
    public IrcUser(final String nick1) {
        nick = nick1;
    }
    
    /**
     * @see java.lang.Object#equals(java.lang.Object)
     */
    @Override
    public boolean equals(final Object obj) {
        if (this == obj) {
            return true;
        } else if (!(obj instanceof IrcUser)) {
            return false;
        }
        return nick.equals(((IrcUser) obj).nick);
    }
    
    /**
     * @return {@link #host}
     */
    public String getHost() {
        return host;
    }
    
    /**
     * @return user nick
     */
    public String getNick() {
        return nick;
    }
    
    /**
     * @return {@link #user}
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
     * @param host1
     */
    public void setHost(final String host1) {
        host = host1;
    }
    
    /**
     * @param nick1
     */
    public void setNick(final String nick1) {
        nick = nick1;
    }
    
    /**
     * @param user1
     */
    public void setUser(final String user1) {
        user = user1;
    }
    
    /**
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        return getNick();
    }
    
    /**
     * @param hostmask
     */
    public final void updateWithMask(final Mask hostmask) {
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

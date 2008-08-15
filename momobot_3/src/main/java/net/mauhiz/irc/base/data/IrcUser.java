package net.mauhiz.irc.base.data;

/**
 * @author mauhiz
 */
public class IrcUser {
    private final Mask hostmask;
    private final UserProperties props = new UserProperties();
    
    /**
     * @param hostmask1
     */
    public IrcUser(final Mask hostmask1) {
        hostmask = hostmask1;
    }
    
    /**
     * @param nick
     */
    public IrcUser(final String nick) {
        hostmask = new Mask(nick + "!*@*");
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
        return hostmask.equals(((IrcUser) obj).hostmask);
    }
    
    /**
     * @return {@link #hostmask}
     */
    public Mask getHostmask() {
        return hostmask;
    }
    
    /**
     * @return user nick
     */
    public String getNick() {
        return hostmask.getNick();
    }
    
    /**
     * @see java.lang.Object#hashCode()
     */
    @Override
    public int hashCode() {
        return hostmask.hashCode();
    }
    
    /**
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        return getNick();
    }
}

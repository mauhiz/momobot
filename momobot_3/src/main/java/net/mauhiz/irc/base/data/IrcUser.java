package net.mauhiz.irc.base.data;

/**
 * @author mauhiz
 */
public class IrcUser {
    private Mask hostmask;
    private UserProperties props = new UserProperties();
    
    public IrcUser(final Mask hostmask1) {
        hostmask = hostmask1;
    }
    
    public IrcUser(final String nick) {
        hostmask = new Mask(nick + "!*@*");
    }
    
    public String getNick() {
        return hostmask.nick;
    }
    
    /**
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        return getNick();
    }
}

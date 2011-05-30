package net.mauhiz.irc.base.data;

/**
 * @author mauhiz
 */
public abstract class AbstractIrcUser implements IrcUser {
    private String fullName;
    private HostMask mask;
    private String nick;
    protected UserProperties props;

    /**
     * @param nick1
     */
    protected AbstractIrcUser(String nick1) {
        nick = nick1;
    }

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

    public String getIrcForm() {
        if (mask == null) {
            return nick;
        }
        return mask.getIrcForm();
    }

    public HostMask getMask() {
        if (mask == null) {
            mask = HostMask.getInstance(nick + "!*@*");
        }
        return mask;
    }

    /**
     * @see net.mauhiz.irc.base.data.IrcUser#getNick()
     */
    public String getNick() {
        return nick;
    }

    public UserProperties getProperties() {
        return props;
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

    public void setMask(HostMask mask) {
        this.mask = mask;
        setNick(mask.getNick());
    }

    /**
     * @see net.mauhiz.irc.base.data.IrcUser#setNick(java.lang.String)
     */
    public void setNick(String nick) {
        assert nick != null;
        this.nick = nick;
    }

    @Override
    public String toString() {
        return nick;
    }
}

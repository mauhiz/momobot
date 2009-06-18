package net.mauhiz.irc.base.data.qnet;

import net.mauhiz.irc.base.data.AbstractIrcUser;

/**
 * @author mauhiz
 */
public class QnetUser extends AbstractIrcUser {
    private String auth;
    
    /**
     * @param nick1
     */
    protected QnetUser(String nick1) {
        super(nick1);
        props = new QnetUserProperties();
    }
    
    /**
     * @return the auth
     */
    public String getAuth() {
        return auth;
    }
    
    /**
     * @see net.mauhiz.irc.base.data.IrcUser#isService()
     */
    @Override
    public boolean isService() {
        return getNick().length() == 1;
    }
    
    /**
     * @param auth1
     *            the auth to set
     */
    public void setAuth(String auth1) {
        auth = auth1;
    }
}

package net.mauhiz.irc.base.data.qnet;

import net.mauhiz.irc.base.data.IrcUser;

/**
 * @author mauhiz
 */
public class QnetUser extends IrcUser {
    private String auth;
    
    /**
     * @param nick1
     */
    public QnetUser(final String nick1) {
        super(nick1);
    }
    
    /**
     * @return the auth
     */
    public String getAuth() {
        return auth;
    }
    
    /**
     * @return si je suis un service Qnet
     */
    public boolean isService() {
        return getNick().length() == 1;
    }
    
    /**
     * @param auth1
     *            the auth to set
     */
    public void setAuth(final String auth1) {
        auth = auth1;
    }
}

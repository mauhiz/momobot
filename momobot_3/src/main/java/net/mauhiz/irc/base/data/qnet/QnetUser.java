package net.mauhiz.irc.base.data.qnet;

import net.mauhiz.irc.base.data.IrcUser;
import net.mauhiz.irc.base.data.Mask;

/**
 * @author mauhiz
 */
public class QnetUser extends IrcUser {
    private String auth;
    
    /**
     * @param hostmask1
     */
    public QnetUser(final Mask hostmask1) {
        super(hostmask1);
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

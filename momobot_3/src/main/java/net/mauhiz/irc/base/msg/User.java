package net.mauhiz.irc.base.msg;

import java.util.Locale;

import net.mauhiz.irc.base.data.IrcServer;

/**
 * @author mauhiz
 */
public class User extends IrcMessage {
    String fullName;
    String login;
    
    /**
     * @param server1
     */
    public User(final IrcServer server1) {
        super(null, null, server1);
        login = server.getMyLogin();
        fullName = server.getMyFullName();
    }
    
    /**
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        return "USER " + login.toLowerCase(Locale.US) + " \"neuf.fr\" \"irc.quakenet.org\" :" + fullName;
    }
}

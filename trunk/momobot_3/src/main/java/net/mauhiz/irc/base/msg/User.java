package net.mauhiz.irc.base.msg;

import java.util.Locale;

import net.mauhiz.irc.base.IIrcControl;
import net.mauhiz.irc.base.data.IrcServer;

/**
 * @author mauhiz
 */
public class User extends AbstractIrcMessage {
    private final String fullName;
    private final String login;
    
    /**
     * @param server1
     */
    public User(IrcServer server1) {
        super(null, null, server1);
        login = server.getMyself().getUser();
        fullName = server.getMyself().getFullName();
    }
    
    @Override
    public String getIrcForm() {
        return "USER " + login.toLowerCase(Locale.US) + " \"neuf.fr\" \"irc.quakenet.org\" :" + fullName;
    }
    
    @Override
    public void process(IIrcControl control) {
        throw new UnsupportedOperationException("I should not receive USER msg");
    }
}

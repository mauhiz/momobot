package net.mauhiz.irc.base.msg;

import java.util.Locale;

import net.mauhiz.irc.base.data.IrcCommands;
import net.mauhiz.irc.base.data.IIrcServerPeer;

/**
 * @author mauhiz
 */
public class User extends AbstractIrcMessage {
    private final String fullName;
    private final String login;

    /**
     * @param server
     */
    public User(IIrcServerPeer server) {
        super(null, null, server);
        login = server.getMyself().getMask().getUser();
        fullName = server.getMyself().getFullName();
    }

    @Override
    public User copy() {
        return new User(server);
    }

    @Override
    public String getIrcForm() {
        return IrcCommands.USER + " " + login.toLowerCase(Locale.US) + " \"neuf.fr\" \"irc.quakenet.org\" :" + fullName;
    }
}

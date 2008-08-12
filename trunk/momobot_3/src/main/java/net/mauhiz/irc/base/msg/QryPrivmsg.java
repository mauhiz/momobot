package net.mauhiz.irc.base.msg;

import net.mauhiz.irc.base.data.IrcServer;
import net.mauhiz.irc.base.data.IrcUser;

/**
 * @author mauhiz
 */
public class QryPrivmsg extends Privmsg {
    
    /**
     * @param from1
     * @param toUser
     * @param server1
     * @param message1
     */
    public QryPrivmsg(final String from1, final IrcUser toUser, final IrcServer server1, final String message1) {
        super(from1, toUser.toString(), server1, message1);
    }
}

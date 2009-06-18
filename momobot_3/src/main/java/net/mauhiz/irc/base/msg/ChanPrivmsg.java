package net.mauhiz.irc.base.msg;

import net.mauhiz.irc.base.data.IrcChannel;
import net.mauhiz.irc.base.data.IrcServer;

/**
 * @author mauhiz
 */
public class ChanPrivmsg extends Privmsg {
    /**
     * @param from1
     * @param toChan
     * @param server1
     * @param message1
     */
    public ChanPrivmsg(String from1, IrcChannel toChan, IrcServer server1, String message1) {
        super(from1, toChan.fullName(), server1, message1);
    }
}

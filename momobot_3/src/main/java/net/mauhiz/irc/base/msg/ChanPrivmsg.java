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
    public ChanPrivmsg(final String from1, final IrcChannel toChan, final IrcServer server1, final String message1) {
        super(from1, toChan.toString(), server1, message1);
    }
}

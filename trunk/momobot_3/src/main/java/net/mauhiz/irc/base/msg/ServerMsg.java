package net.mauhiz.irc.base.msg;

import net.mauhiz.irc.base.data.IrcServer;

/**
 * @author mauhiz
 */
public class ServerMsg extends IrcMessage {
    int    code;
    String msg;

    /**
     * @param from1
     * @param to1
     * @param server1
     */
    public ServerMsg(final String from1, final String to1, final IrcServer server1) {
        super(from1, to1, server1);
    }

    /**
     * @param object
     * @param object2
     * @param ircServer
     * @param group
     * @param group2
     */
    public ServerMsg(final String object, final String object2, final IrcServer ircServer, final String group,
            final String group2) {
        this(object, object2, ircServer);
        this.code = Integer.parseInt(group);
        this.msg = group2;
    }

    /**
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        return "server msg: code=" + this.code + " msg=" + this.msg;
    }
}

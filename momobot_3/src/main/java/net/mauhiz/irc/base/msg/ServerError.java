package net.mauhiz.irc.base.msg;

import net.mauhiz.irc.base.data.IrcCommands;
import net.mauhiz.irc.base.data.IIrcServerPeer;

/**
 * @author mauhiz
 */
public class ServerError extends AbstractIrcMessage {
    /**
     * contenu de l erreur
     */
    private final String msg;

    public ServerError(IIrcServerPeer server, String msg) {
        super(null, null, server);
        this.msg = msg;
    }

    @Override
    public ServerError copy() {
        return new ServerError(server, msg);
    }

    @Override
    public String getIrcForm() {
        return IrcCommands.ERROR + " :" + msg;
    }

    /**
     * @return {@link #msg}
     */
    public String getMsg() {
        return msg;
    }

    @Override
    public String toString() {
        return getIrcForm();
    }
}

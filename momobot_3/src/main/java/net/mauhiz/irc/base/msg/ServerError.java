package net.mauhiz.irc.base.msg;

import net.mauhiz.irc.base.data.IrcServer;

/**
 * @author mauhiz
 */
public class ServerError extends IrcMessage {
    /**
     * contenu de l erreur
     */
    private String msg;
    
    /**
     * @param server1
     * @param msg1
     */
    public ServerError(final IrcServer server1, final String msg1) {
        super(null, null, server1);
        msg = msg1;
    }
    
    /**
     * @return {@link #msg}
     */
    public String getMsg() {
        return msg;
    }
    
    /**
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        return "ERROR :" + msg;
    }
}

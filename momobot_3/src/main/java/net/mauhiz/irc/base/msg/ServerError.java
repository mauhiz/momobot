package net.mauhiz.irc.base.msg;

import net.mauhiz.irc.base.IIrcControl;
import net.mauhiz.irc.base.data.IrcServer;

/**
 * @author mauhiz
 */
public class ServerError extends AbstractIrcMessage {
    /**
     * contenu de l erreur
     */
    private final String msg;
    
    /**
     * @param server1
     * @param msg1
     */
    public ServerError(IrcServer server1, String msg1) {
        super(null, null, server1);
        msg = msg1;
    }
    
    @Override
    public String getIrcForm() {
        return "ERROR :" + msg;
    }
    
    /**
     * @return {@link #msg}
     */
    public String getMsg() {
        return msg;
    }
    
    @Override
    public void process(IIrcControl control) {
        control.quit(server);
    }
    
    @Override
    public String toString() {
        return getIrcForm();
    }
}

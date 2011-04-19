package net.mauhiz.irc.base.msg;

import net.mauhiz.irc.base.data.IIrcServerPeer;
import net.mauhiz.irc.base.data.Target;

/**
 * @author mauhiz
 */
public class Action extends Ctcp {

    public static final String COMMAND = "ACTION";

    /**
     * @deprecated
     */
    @Deprecated
    public Action(IIrcServerPeer server, Target from, IIrcServerPeer to, String msg) {
        super(server, from, to, msg);
    }

    public Action(IIrcServerPeer server, Target from, Target to, String message) {
        super(server, from, to, message);
    }

    public Action(IPrivateIrcMessage replyTo, String respMsg, boolean priv) {
        super(replyTo, respMsg, priv);
    }

    @Override
    protected String getCommand() {
        return COMMAND;
    }
}

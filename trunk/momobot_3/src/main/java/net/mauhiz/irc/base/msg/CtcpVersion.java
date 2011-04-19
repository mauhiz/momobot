package net.mauhiz.irc.base.msg;

import net.mauhiz.irc.base.data.IIrcServerPeer;
import net.mauhiz.irc.base.data.Target;

/**
 * @author mauhiz
 */
public class CtcpVersion extends Ctcp {

    public static final String COMMAND = "VERSION";

    public CtcpVersion(IIrcServerPeer server, Target from, Target to, String message) {
        super(server, from, to, message);
    }

    @Override
    protected String getCommand() {
        return COMMAND;
    }
}

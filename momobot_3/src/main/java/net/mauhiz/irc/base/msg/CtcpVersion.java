package net.mauhiz.irc.base.msg;

import net.mauhiz.irc.base.data.IIrcServerPeer;
import net.mauhiz.irc.base.data.Target;

/**
 * @author mauhiz
 */
public class CtcpVersion extends Ctcp {

    public static final String COMMAND = "VERSION";

    /**
     * @param toReply
     * @param msg
     * @return new msg
     */
    public static CtcpVersion buildAnswer(IIrcMessage toReply, String msg) {
        if (toReply.isToChannel()) {
            return new CtcpVersion(null, toReply.getTo(), toReply.getServerPeer(), msg);
        }
        return buildPrivateAnswer(toReply, msg);
    }

    /**
     * @param toReply
     * @param msg
     * @return new msg
     */
    public static CtcpVersion buildPrivateAnswer(IIrcMessage toReply, String msg) {
        return new CtcpVersion(null, toReply.getFrom(), toReply.getServerPeer(), msg);
    }

    /**
     * @param from1
     * @param to1
     * @param server1
     * @param message1
     */
    public CtcpVersion(Target from1, Target to1, IIrcServerPeer server1, String message1) {
        super(from1, to1, server1, message1);
    }

    @Override
    protected String getCommand() {
        return COMMAND;
    }
}

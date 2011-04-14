package net.mauhiz.irc.base.msg;

import net.mauhiz.irc.base.data.IIrcServerPeer;
import net.mauhiz.irc.base.data.Target;

/**
 * @author mauhiz
 */
public class Action extends Ctcp {

    public static final String COMMAND = "ACTION";

    /**
     * @param toReply
     * @param msg
     * @return new msg
     */
    public static Action buildAnswer(IIrcMessage toReply, String msg) {
        if (toReply.isToChannel()) {
            return new Action(null, toReply.getTo(), toReply.getServerPeer(), msg);
        }
        return buildPrivateAnswer(toReply, msg);
    }

    /**
     * @param toReply
     * @param msg
     * @return new msg
     */
    public static Action buildPrivateAnswer(IIrcMessage toReply, String msg) {
        return new Action(null, toReply.getFrom(), toReply.getServerPeer(), msg);
    }

    public Action(Target from, Target to, IIrcServerPeer server, String message) {
        super(from, to, server, message);
    }

    @Override
    protected String getCommand() {
        return COMMAND;
    }
}

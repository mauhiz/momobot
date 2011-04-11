package net.mauhiz.irc.base.msg;

import net.mauhiz.irc.base.data.IrcServer;
import net.mauhiz.irc.base.data.Mask;

/**
 * @author mauhiz
 */
public class Version extends Ctcp {

    public static final String COMMAND = "VERSION";

    /**
     * @param toReply
     * @param msg
     * @return new msg
     */
    public static Version buildAnswer(IIrcMessage toReply, String msg) {
        if (toReply.isToChannel()) {
            return new Version(null, toReply.getTo(), toReply.getServer(), msg);
        }
        return buildPrivateAnswer(toReply, msg);
    }

    /**
     * @param toReply
     * @param msg
     * @return new msg
     */
    public static Version buildPrivateAnswer(IIrcMessage toReply, String msg) {
        Mask from = new Mask(toReply.getFrom());
        return new Version(null, from.getNick(), toReply.getServer(), msg);
    }

    /**
     * @param from1
     * @param to1
     * @param server1
     * @param message1
     */
    public Version(String from1, String to1, IrcServer server1, String message1) {
        super(from1, to1, server1, message1);
    }

    @Override
    protected String getCommand() {
        return COMMAND;
    }
}

package net.mauhiz.irc.base.msg;

import net.mauhiz.irc.base.IrcSpecialChars;
import net.mauhiz.irc.base.data.IrcServer;

import org.apache.commons.lang.StringUtils;

/**
 * @author mauhiz
 */
public abstract class Ctcp extends Privmsg implements IrcSpecialChars {
    protected static final String ACTION = "ACTION";
    protected static final String DCC = "DCC";

    public static Ctcp decode(String from, String to, IrcServer server, String msg) {
        String command = StringUtils.substringBefore(msg, " ");
        String ctcpContent = StringUtils.substringAfter(msg, " ");

        if (ACTION.equals(command)) {
            return new Action(from, to, server, ctcpContent);

        } else if (DCC.equals(command)) {
            if (ctcpContent.startsWith("CHAT CHAT ")) {
                return new DccChatRequest(from, to, server, ctcpContent);
            }
        }

        return null;
    }

    /**
     * @param from1
     * @param to1
     * @param server1
     * @param message1
     */
    public Ctcp(String from1, String to1, IrcServer server1, String message1) {
        super(from1, to1, server1, message1);
    }

    protected abstract String getCommand();

    protected String getCtcpContent() {
        return super.getMessage();
    }

    /**
     * @see net.mauhiz.irc.base.msg.Privmsg#getMessage()
     */
    @Override
    public String getMessage() {
        return QUOTE_STX + getCommand() + " " + getCtcpContent() + QUOTE_STX;
    }
}

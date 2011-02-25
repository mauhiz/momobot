package net.mauhiz.irc.base.msg;

import net.mauhiz.irc.base.IrcSpecialChars;
import net.mauhiz.irc.base.data.IrcServer;

/**
 * @author mauhiz
 */
public abstract class Ctcp extends Privmsg implements IrcSpecialChars {

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

    public String getCtcpContent() {
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

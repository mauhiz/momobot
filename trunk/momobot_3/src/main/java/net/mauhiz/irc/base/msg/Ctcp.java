package net.mauhiz.irc.base.msg;

import net.mauhiz.irc.base.IrcSpecialChars;
import net.mauhiz.irc.base.data.IIrcServerPeer;
import net.mauhiz.irc.base.data.Target;

/**
 * @author mauhiz
 */
public abstract class Ctcp extends Privmsg implements IrcSpecialChars {

    protected Ctcp(Target from, Target to, IIrcServerPeer server, String message) {
        super(from, to, server, message);
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

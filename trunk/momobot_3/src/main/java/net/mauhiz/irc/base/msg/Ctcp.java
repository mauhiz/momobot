package net.mauhiz.irc.base.msg;

import net.mauhiz.irc.base.IrcSpecialChars;
import net.mauhiz.irc.base.data.IIrcServerPeer;
import net.mauhiz.irc.base.data.Target;

/**
 * @author mauhiz
 */
public abstract class Ctcp extends Privmsg implements IrcSpecialChars {

    protected Ctcp(IIrcServerPeer server, Target from, Target to, String message) {
        super(server, from, to, message);
    }

    public Ctcp(IPrivateIrcMessage replyTo, String respMsg, boolean priv) {
        super(replyTo, respMsg, priv);
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

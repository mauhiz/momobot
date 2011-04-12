package net.mauhiz.irc.base.msg;

import net.mauhiz.irc.base.IIrcControl;
import net.mauhiz.irc.base.data.IrcChannel;
import net.mauhiz.irc.base.data.IrcServer;
import net.mauhiz.irc.base.data.Target;

/**
 * @author mauhiz
 */
public class Invite extends AbstractIrcMessage {

    private final IrcChannel chan;

    /**
     * @param from1
     * @param to1
     * @param ircServer
     * @param msg1
     */
    public Invite(Target from, Target to, IrcServer ircServer, IrcChannel chan) {
        super(from, to, ircServer);
        this.chan = chan;
    }

    /**
     * @return the message
     */
    public IrcChannel getChan() {
        return chan;
    }

    @Override
    public String getIrcForm() {
        StringBuilder sb = new StringBuilder();
        if (super.from != null) {
            sb.append(':');
            sb.append(super.from);
            sb.append(' ');
        }
        sb.append("INVITE ");
        sb.append(super.to);
        sb.append(' ');

        sb.append(chan);
        return sb.toString();
    }

    @Override
    public void process(IIrcControl control) {
        // nothing to do
    }

    @Override
    public String toString() {
        return " * " + niceFromDisplay() + " invited me to " + chan;
    }
}

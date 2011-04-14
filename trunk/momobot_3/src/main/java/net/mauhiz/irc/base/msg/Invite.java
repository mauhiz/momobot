package net.mauhiz.irc.base.msg;

import net.mauhiz.irc.base.data.IrcChannel;
import net.mauhiz.irc.base.data.IrcCommands;
import net.mauhiz.irc.base.data.IIrcServerPeer;
import net.mauhiz.irc.base.data.IrcUser;
import net.mauhiz.irc.base.data.Target;

/**
 * @author mauhiz
 */
public class Invite extends AbstractIrcMessage {

    private final IrcChannel chan;

    public Invite(Target from, IrcUser invitedUser, IIrcServerPeer server, IrcChannel chan) {
        super(from, invitedUser, server);
        this.chan = chan;
    }

    @Override
    public Invite copy() {
        return new Invite(chan, (IrcUser) to, server, chan);
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
        sb.append(IrcCommands.INVITE).append(' ');
        sb.append(super.to);
        sb.append(' ');

        sb.append(chan);
        return sb.toString();
    }

    @Override
    public String toString() {
        return " * " + niceFromDisplay() + " invited me to " + chan;
    }
}

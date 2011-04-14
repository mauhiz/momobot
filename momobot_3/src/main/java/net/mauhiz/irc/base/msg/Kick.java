package net.mauhiz.irc.base.msg;

import net.mauhiz.irc.base.data.IrcChannel;
import net.mauhiz.irc.base.data.IrcCommands;
import net.mauhiz.irc.base.data.IIrcServerPeer;
import net.mauhiz.irc.base.data.IrcUser;
import net.mauhiz.irc.base.data.Target;

/**
 * @author mauhiz
 */
public class Kick extends AbstractIrcMessage {
    private final String reason;
    private final IrcUser target;

    public Kick(IIrcServerPeer server, Target kicker, IrcChannel chan, IrcUser target, String reason) {
        super(kicker, chan, server);
        this.target = target;
        this.reason = reason;
    }

    @Override
    public Kick copy() {
        return new Kick(server, from, (IrcChannel) to, target, reason);
    }

    @Override
    public String getIrcForm() {
        StringBuilder sb = new StringBuilder();
        if (super.from != null) {
            sb.append(':');
            sb.append(super.from);
            sb.append(' ');
        }
        sb.append(IrcCommands.KICK).append(' ');
        sb.append(to);
        sb.append(' ');
        sb.append(target);
        if (reason != null) {
            sb.append(" :");
            sb.append(reason);
        }
        return sb.toString();
    }

    /**
     * @return {@link #reason}
     */
    public String getReason() {
        return reason;
    }

    /**
     * @return {@link #target}
     */
    public IrcUser getTarget() {
        return target;
    }

    @Override
    public String toString() {
        if (server.getMyself().equals(target)) {
            // I have been kicked
            return "* Kicked from " + to + " by " + niceFromDisplay() + ": " + reason;
        } else if (from == null) { // I am kicking
            return "* Kicking " + target + " from " + to + ": " + reason;
        } else { // Someone kicks someone else
            return "* " + target + " was kicked from " + to + " by " + niceFromDisplay() + ": " + reason;
        }
    }
}

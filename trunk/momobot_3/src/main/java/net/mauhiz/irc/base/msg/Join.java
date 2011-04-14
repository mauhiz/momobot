package net.mauhiz.irc.base.msg;

import net.mauhiz.irc.base.data.IrcChannel;
import net.mauhiz.irc.base.data.IrcCommands;
import net.mauhiz.irc.base.data.IIrcServerPeer;
import net.mauhiz.irc.base.data.Target;

/**
 * @author mauhiz
 */
public class Join extends AbstractIrcMessage {
    private final String key;

    public Join(IIrcServerPeer server, IrcChannel channel) {
        this(null, server, channel);
    }

    public Join(Target from, IIrcServerPeer server, IrcChannel chan) {
        this(from, server, chan, null);
    }

    /**
     * TODO join multiple chans with different keys
     */
    public Join(Target from, IIrcServerPeer server, IrcChannel chan, String key) {
        super(from, chan, server);
        this.key = key;
    }

    @Override
    public Join copy() {
        return new Join(from, server, (IrcChannel) to, key);
    }

    @Override
    public String getIrcForm() {
        StringBuilder sb = new StringBuilder();
        if (super.from != null) {
            sb.append(':');
            sb.append(super.from);
            sb.append(' ');
        }
        sb.append(IrcCommands.JOIN).append(' ');
        sb.append(super.to);

        if (key != null) {
            sb.append(' ');
            sb.append(key);
        }
        return sb.toString();
    }

    /**
     * @return the message
     */
    public String getKey() {
        return key;
    }

    /**
     * The channel this JOIN joins
     */
    @Override
    public IrcChannel getTo() {
        return (IrcChannel) super.getTo();
    }

    /**
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        if (from == null) { // self
            return "Joining " + to;
        }
        return "* Joins " + to + " : " + niceFromDisplay();
    }
}

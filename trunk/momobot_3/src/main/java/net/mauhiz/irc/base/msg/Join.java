package net.mauhiz.irc.base.msg;

import net.mauhiz.irc.base.IIrcControl;
import net.mauhiz.irc.base.data.IrcChannel;
import net.mauhiz.irc.base.data.IrcServer;
import net.mauhiz.irc.base.data.IrcUser;
import net.mauhiz.irc.base.data.Target;

/**
 * @author mauhiz
 */
public class Join extends AbstractIrcMessage {
    private final String key;

    /**
     * @param ircServer
     * @param channel
     */
    public Join(IrcServer ircServer, IrcChannel channel) {
        this(null, ircServer, channel);
    }

    /**
     * @param from
     * @param ircServer
     * @param chan1
     */
    public Join(Target from, IrcServer ircServer, IrcChannel chan1) {
        this(from, ircServer, chan1, null);
    }

    /**
     * @param from
     * @param ircServer
     * @param chan1
     * @param key1
     */
    public Join(Target from, IrcServer ircServer, IrcChannel chan1, String key1) {
        super(from, chan1, ircServer);
        key = key1;
    }

    @Override
    public String getIrcForm() {
        StringBuilder sb = new StringBuilder();
        if (super.from != null) {
            sb.append(':');
            sb.append(super.from);
            sb.append(' ');
        }
        sb.append("JOIN ");
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

    @Override
    public void process(IIrcControl control) {
        IrcChannel joined = (IrcChannel) to;
        IrcUser joiner = (IrcUser) from;
        joined.add(joiner);
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

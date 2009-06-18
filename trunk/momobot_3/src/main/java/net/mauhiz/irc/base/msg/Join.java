package net.mauhiz.irc.base.msg;

import net.mauhiz.irc.base.IIrcControl;
import net.mauhiz.irc.base.data.IrcChannel;
import net.mauhiz.irc.base.data.IrcServer;
import net.mauhiz.irc.base.data.IrcUser;
import net.mauhiz.irc.base.data.Mask;

/**
 * @author mauhiz
 */
public class Join extends AbstractIrcMessage {
    private final String chan;
    private final String key;
    
    /**
     * @param ircServer
     * @param channel
     */
    public Join(IrcServer ircServer, String channel) {
        this(null, ircServer, channel);
    }
    
    /**
     * @param from1
     * @param ircServer
     * @param chan1
     */
    public Join(String from1, IrcServer ircServer, String chan1) {
        this(from1, ircServer, chan1, null);
    }
    
    /**
     * @param from1
     * @param ircServer
     * @param chan1
     * @param key1
     */
    public Join(String from1, IrcServer ircServer, String chan1, String key1) {
        super(from1, null, ircServer);
        chan = chan1;
        key = key1;
        
    }
    
    /**
     * @return {@link #chan}
     */
    public String getChan() {
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
        sb.append("JOIN ");
        if (super.to != null) {
            sb.append(super.to);
            sb.append(' ');
        }
        sb.append(chan);
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
    
    @Override
    public void process(IIrcControl control) {
        IrcChannel joined = server.findChannel(chan);
        IrcUser joiner = server.findUser(new Mask(from), true);
        joined.add(joiner);
    }
    
    /**
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        return "* Joins: " + from;
    }
}

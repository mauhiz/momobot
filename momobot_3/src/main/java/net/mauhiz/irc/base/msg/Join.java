package net.mauhiz.irc.base.msg;

import net.mauhiz.irc.base.data.IrcServer;

/**
 * @author mauhiz
 */
public class Join extends IrcMessage {
    String chan;
    String key;
    
    /**
     * @param ircServer
     * @param channel
     */
    public Join(final IrcServer ircServer, final String channel) {
        this(null, ircServer, channel);
    }
    
    /**
     * @param from1
     * @param ircServer
     * @param chan1
     */
    public Join(final String from1, final IrcServer ircServer, final String chan1) {
        this(null, ircServer, chan1, null);
    }
    
    /**
     * @param from1
     * @param ircServer
     * @param chan1
     * @param key1
     */
    public Join(final String from1, final IrcServer ircServer, final String chan1, final String key1) {
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
    
    /**
     * @return the message
     */
    public String getKey() {
        return key;
    }
    
    /**
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
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
}

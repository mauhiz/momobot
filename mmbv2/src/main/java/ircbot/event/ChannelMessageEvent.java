package ircbot.event;

import ircbot.Channel;
import ircbot.IrcUser;

/**
 * @author mauhiz
 */
public class ChannelMessageEvent extends AbstracIrcEvent {
    /**
     * 
     */
    private final IrcUser from;
    /**
     * 
     */
    private final String  msg;
    /**
     * 
     */
    private final Channel to;

    /**
     * @param from1
     * @param to1
     * @param msg1
     */
    public ChannelMessageEvent(final IrcUser from1, final Channel to1, final String msg1) {
        super();
        this.from = from1;
        this.msg = msg1;
        this.to = to1;
    }

    /**
     * @return the from
     */
    public IrcUser getFrom() {
        return this.from;
    }

    /**
     * @return the msg
     */
    public String getMsg() {
        return this.msg;
    }

    /**
     * @return the to
     */
    public Channel getTo() {
        return this.to;
    }
}

package ircbot.event;

import ircbot.IrcUser;

/**
 * @author mauhiz
 */
public class PrivateMessageEvent extends AbstracIrcEvent {
    /**
     * 
     */
    private final IrcUser from;
    /**
     * 
     */
    private final String  msg;

    /**
     * @param from1
     * @param msg1
     */
    public PrivateMessageEvent(final IrcUser from1, final String msg1) {
        super();
        this.from = from1;
        this.msg = msg1;
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
}

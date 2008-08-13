package net.mauhiz.irc.base.msg;

import net.mauhiz.irc.base.data.IrcServer;

/**
 * @author mauhiz
 */
public class ServerMsg extends IrcMessage implements NumericReplies {
    /**
     * code numerique
     */
    private int code;
    /**
     * suite du message
     */
    private String msg;
    
    /**
     * @param from1
     * @param to1
     * @param server1
     */
    public ServerMsg(final String from1, final String to1, final IrcServer server1) {
        super(from1, to1, server1);
    }
    
    /**
     * @param from1
     * @param to1
     * @param ircServer
     * @param codeStr
     * @param group2
     */
    public ServerMsg(final String from1, final String to1, final IrcServer ircServer, final String codeStr,
            final String group2) {
        this(from1, to1, ircServer);
        code = Integer.parseInt(codeStr);
        msg = group2;
    }
    
    /**
     * @return {@link #code}
     */
    public int getCode() {
        return code;
    }
    
    /**
     * @return {@link #msg}
     */
    public String getMsg() {
        return msg;
    }
    
    /**
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        return "server msg: code=" + code + " msg=" + msg;
    }
}

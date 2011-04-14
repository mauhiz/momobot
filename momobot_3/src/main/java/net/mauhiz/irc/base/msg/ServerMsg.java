package net.mauhiz.irc.base.msg;

import net.mauhiz.irc.base.data.IIrcServerPeer;
import net.mauhiz.irc.base.data.Target;

/**
 * @author mauhiz
 */
public class ServerMsg extends AbstractIrcMessage {
    /**
     * code numerique
     */
    private final int code;
    /**
     * suite du message
     */
    private final String msg;

    public ServerMsg(Target from, Target to, IIrcServerPeer server, String codeStr, String msg) {
        super(from, to, server);
        code = Integer.parseInt(codeStr);
        this.msg = msg;
    }

    @Override
    public IIrcMessage copy() {
        return new ServerMsg(from, to, server, Integer.toString(code), msg);
    }

    /**
     * @return {@link #code}
     */
    public int getCode() {
        return code;
    }

    @Override
    public String getIrcForm() {
        return ":" + from.getIrcForm() + " " + code + " " + to + " :" + msg;
    }

    /**
     * @return {@link #msg}
     */
    public String getMsg() {
        return msg;
    }

    /**
     * @see net.mauhiz.irc.base.msg.AbstractIrcMessage#toString()
     */
    @Override
    public String toString() {
        return "code=" + code + " msg=" + msg;
    }
}

package net.mauhiz.irc.bouncer;

import net.mauhiz.irc.base.data.IIrcServerPeer;
import net.mauhiz.irc.base.data.Target;
import net.mauhiz.irc.base.msg.IIrcMessage;

public class NoticeAuth implements IIrcMessage {
    private final String msg;
    private final IIrcServerPeer server;

    public NoticeAuth(IIrcServerPeer server, String msg) {
        this.server = server;
        this.msg = msg;
    }

    @Override
    public NoticeAuth copy() {
        return new NoticeAuth(server, msg);
    }

    @Override
    public Target getFrom() {
        return null;
    }

    @Override
    public String getIrcForm() {
        return "NOTICE AUTH :" + msg;
    }

    @Override
    public IIrcServerPeer getServerPeer() {
        return server;
    }

    @Override
    public Target getTo() {
        return null;
    }

    @Override
    public boolean isToChannel() {
        return false;
    }
}

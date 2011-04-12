package net.mauhiz.irc.bouncer;

import net.mauhiz.irc.base.IIrcControl;
import net.mauhiz.irc.base.data.IrcServer;
import net.mauhiz.irc.base.data.Target;
import net.mauhiz.irc.base.msg.IIrcMessage;

public class NoticeAuth implements IIrcMessage {
    private final String msg;
    private final IrcServer server;

    public NoticeAuth(IrcServer server, String msg) {
        this.server = server;
        this.msg = msg;
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
    public IrcServer getServer() {
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

    @Override
    public void process(IIrcControl control) {
        // so what
    }
}

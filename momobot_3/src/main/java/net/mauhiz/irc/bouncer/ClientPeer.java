package net.mauhiz.irc.bouncer;

import java.net.InetSocketAddress;
import java.net.Socket;

import net.mauhiz.irc.base.data.AbstractPeer;
import net.mauhiz.irc.base.data.IrcNetwork;
import net.mauhiz.irc.base.data.Target;

import org.apache.commons.lang.NotImplementedException;

public class ClientPeer extends AbstractPeer implements Target {
    private final BncServer server;

    public ClientPeer(Socket so, BncServer server) {
        super(new InetSocketAddress(so.getInetAddress(), so.getPort()));
        this.server = server;
    }

    public String getIrcForm() {
        throw new NotImplementedException("TODO");
    }

    public IrcNetwork getNetwork() {
        return server;
    }
}

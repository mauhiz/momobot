package net.mauhiz.irc.bouncer;

import java.net.InetSocketAddress;
import java.net.Socket;

import net.mauhiz.irc.base.data.IrcDecoder;
import net.mauhiz.irc.base.data.IrcServer;

public class ClientPeer extends IrcDecoder {
    private final BncServer server;
    
    public ClientPeer(Socket so, BncServer server) {
        super();
        this.server = server;
        hostPort = new InetSocketAddress(so.getInetAddress(), so.getPort());
    }
    
    @Override
    public int getLineMaxLength() {
        return 512;
    }
    
    @Override
    protected IrcServer getServer() {
        return server;
    }
}

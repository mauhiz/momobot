package net.mauhiz.irc.base.msg;

import java.net.InetAddress;

import net.mauhiz.irc.base.data.IrcServer;
import net.mauhiz.util.NetUtils;

/**
 * @author mauhiz
 */
public class DccChatRequest extends Ctcp {

    private final InetAddress address;
    private final int port;

    /**
     */
    public DccChatRequest(String from1, String to1, IrcServer server1, InetAddress address, int port) {
        super(from1, to1, server1, "CHAT " + NetUtils.iaToLong(address) + " " + port);
        this.address = address;
        this.port = port;
    }

    public InetAddress getAddress() {
        return address;
    }

    @Override
    protected String getCommand() {
        return "DCC CHAT";
    }

    public int getPort() {
        return port;
    }

    @Override
    public String toString() {
        if (from == null) { // self
            return "Starting DCC Chat with " + to;
        }
        return "DCC Chat request from " + to;
    }
}

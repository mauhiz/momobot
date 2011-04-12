package net.mauhiz.irc.base.msg;

import java.net.InetAddress;

import net.mauhiz.irc.base.data.IrcServer;
import net.mauhiz.irc.base.data.Target;
import net.mauhiz.util.NetUtils;

import org.apache.commons.lang.StringUtils;

/**
 * @author mauhiz
 */
public class DccChatRequest extends Ctcp {
    public static final String COMMAND = "DCC";
    private final InetAddress address;
    private final int port;

    public DccChatRequest(Target from, Target to, IrcServer server1, InetAddress address, int port) {
        super(from, to, server1, "CHAT CHAT " + NetUtils.iaToLong(address) + " " + port);
        this.address = address;
        this.port = port;
    }

    public DccChatRequest(Target from, Target to, IrcServer server, String ctcpContent) {
        super(from, to, server, ctcpContent);
        String[] tokens = StringUtils.split(ctcpContent, ' ');
        long addrLong = Long.parseLong(tokens[2]);
        address = NetUtils.longToIa(addrLong);
        port = Integer.parseInt(tokens[3]);
    }

    public InetAddress getAddress() {
        return address;
    }

    @Override
    protected String getCommand() {
        return COMMAND;
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

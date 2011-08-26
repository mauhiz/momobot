package net.mauhiz.irc.base.msg;

import java.net.InetAddress;
import java.net.UnknownHostException;

import net.mauhiz.irc.base.data.IIrcServerPeer;
import net.mauhiz.irc.base.data.Target;
import net.mauhiz.util.NetUtils;

import org.apache.commons.lang3.StringUtils;

/**
 * @author mauhiz
 */
public class DccChatRequest extends Ctcp {
    public static final String COMMAND = "DCC";

    private static InetAddress decodeAddress(String addrLong) {
        try {
            return NetUtils.longToIa(Long.parseLong(addrLong));
        } catch (UnknownHostException uhe) {
            return null;
        } catch (IllegalArgumentException iae) {
            return null;
        }
    }

    private final InetAddress address;

    private final int port;

    public DccChatRequest(IIrcServerPeer server, Target from, Target to, InetAddress address, int port) {
        super(server, from, to, "CHAT CHAT " + NetUtils.iaToLong(address) + " " + port);
        this.address = address;
        this.port = port;
    }

    public DccChatRequest(IIrcServerPeer server, Target from, Target to, String ctcpContent) {
        super(server, from, to, ctcpContent);
        String[] tokens = StringUtils.split(ctcpContent, ' ');
        address = decodeAddress(tokens[2]);
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

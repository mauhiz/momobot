package net.mauhiz.irc.base.io;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Socket;

import net.mauhiz.irc.base.IrcClientControl;
import net.mauhiz.irc.base.data.IIrcServerPeer;
import net.mauhiz.irc.base.data.IrcPeer;
import net.mauhiz.irc.base.msg.Nick;
import net.mauhiz.irc.base.msg.User;
import net.mauhiz.util.ThreadUtils;

import org.apache.commons.net.SocketClient;
import org.apache.log4j.Logger;

/**
 * @author mauhiz
 */
public class IrcClientIO extends AbstractIrcIO {
    static class IrcClient extends SocketClient {
        Socket getSocket() {
            return _socket_;
        }
    }

    private static final Logger LOG = Logger.getLogger(IrcClientIO.class);
    private IIrcInput input;
    private final IrcClient sclient = new IrcClient();

    public IrcClientIO(IrcClientControl control, IrcPeer server) {
        super(control, server);
    }

    /**
     * @throws IOException
     */
    public void connect() throws IOException {
        InetSocketAddress address = peer.getAddress();
        sclient.connect(address.getAddress(), address.getPort());
        if (sclient.getSocket() == null) {
            LOG.error("could not connect to " + address);
            return;
        }
        output = new IrcOutput(sclient.getSocket());
        output.start();
        input = new IrcInput(this, sclient.getSocket());
        input.start();
        IIrcServerPeer server = getServerPeer();
        sendMsg(new Nick(server).getIrcForm());
        sendMsg(new User(server).getIrcForm());
    }

    /**
     * @see org.apache.commons.net.SocketClient#disconnect()
     */
    public void disconnect() {
        status = IOStatus.DISCONNECTING;
        if (output != null) {
            output.stop();
        }
        if (input != null) {
            input.stop();
        }
        ThreadUtils.safeSleep(2000);
        if (sclient.isConnected()) {
            try {
                sclient.disconnect();
            } catch (IOException ioe) {
                LOG.error(ioe, ioe);
            }
        }
        status = IOStatus.DISCONNECTED;
    }

    public IIrcServerPeer getServerPeer() {
        return (IIrcServerPeer) peer; // server is my partner.
    }

    public void reconnect() throws IOException {
        disconnect();
        connect();
    }

    public void waitForConnection() {
        while (true) {
            if (status == IOStatus.CONNECTED) {
                return;
            }
            ThreadUtils.safeSleep(100);
        }
    }
}

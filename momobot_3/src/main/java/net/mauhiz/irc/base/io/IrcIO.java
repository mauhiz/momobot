package net.mauhiz.irc.base.io;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Socket;

import net.mauhiz.irc.base.IrcControl;
import net.mauhiz.irc.base.data.IrcServer;
import net.mauhiz.irc.base.msg.Nick;
import net.mauhiz.irc.base.msg.User;
import net.mauhiz.util.AbstractRunnable;

import org.apache.commons.net.SocketClient;
import org.apache.log4j.Logger;

/**
 * @author mauhiz
 */
public class IrcIO extends AbstractIrcIO {
    static class IrcClient extends SocketClient {
        Socket getSocket() {
            return _socket_;
        }
    }

    private static final Logger LOG = Logger.getLogger(IrcIO.class);
    private final IrcClient sclient = new IrcClient();

    /**
     * @param ircControl
     * @param server1
     */
    public IrcIO(IrcControl ircControl, IrcServer server1) {
        super(ircControl, server1);
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
        IIrcInput input = new IrcInput(this, sclient.getSocket());
        input.start();
        sendMsg(new Nick(getPeer()).getIrcForm());
        sendMsg(new User(getPeer()).getIrcForm());
    }

    /**
     * @see org.apache.commons.net.SocketClient#disconnect()
     */
    @Override
    public void disconnect() {
        status = IOStatus.DISCONNECTING;
        if (output != null) {
            output.stop();
            AbstractRunnable.sleep(2000);
        }
        if (sclient.isConnected()) {
            try {
                sclient.disconnect();
            } catch (IOException ioe) {
                LOG.error(ioe, ioe);
            }
        }
        status = IOStatus.DISCONNECTED;
    }

    @Override
    public IrcServer getPeer() {
        return (IrcServer) peer;
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
            AbstractRunnable.sleep(100);
        }
    }
}

package net.mauhiz.irc.base.io;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.channels.AsynchronousSocketChannel;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import net.mauhiz.irc.base.IrcClientControl;
import net.mauhiz.irc.base.data.IIrcServerPeer;
import net.mauhiz.irc.base.data.IrcPeer;
import net.mauhiz.irc.base.msg.Nick;
import net.mauhiz.irc.base.msg.User;
import net.mauhiz.util.ThreadUtils;

import org.apache.log4j.Logger;

/**
 * @author mauhiz
 */
public class IrcClientIO extends AbstractIrcIO {
    private static final Logger LOG = Logger.getLogger(IrcClientIO.class);
    private IIrcInput input;
    private AsynchronousSocketChannel sclient;

    public IrcClientIO(IrcClientControl control, IrcPeer server) {
        super(control, server);
    }

    public void connect() {
        status = IOStatus.CONNECTING;
        InetSocketAddress address = peer.getAddress();
        try {
            sclient = AsynchronousSocketChannel.open();
        } catch (IOException ioe) {
            LOG.error("could not create socket to " + address, ioe);
            status = IOStatus.DISCONNECTED;
            return;
        }

        Future<Void> f = sclient.connect(address);
        try {
            f.get(1, TimeUnit.MINUTES);
        } catch (InterruptedException e) {
            ThreadUtils.handleInterruption(e);
        } catch (TimeoutException e) {
            LOG.error("could not connect to " + address + " within given time");
            status = IOStatus.DISCONNECTED;
            return;
        } catch (ExecutionException e) {
            LOG.error("could not connect to " + address, e.getCause());
            status = IOStatus.DISCONNECTED;
            return;
        }
        output = new IrcOutput(sclient);
        output.tstart();
        input = new IrcInput(this, sclient);
        input.tstart();
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
            output.tstop();
        }
        if (input != null) {
            input.tstop();
        }
        ThreadUtils.safeSleep(2_000);
        try {
            sclient.close();
        } catch (IOException ioe) {
            LOG.error(ioe, ioe);
        }

        status = IOStatus.DISCONNECTED;
    }

    public IIrcServerPeer getServerPeer() {
        return (IIrcServerPeer) peer; // server is my partner.
    }

    public void reconnect() {
        disconnect();
        connect();
    }

    public IOStatus waitForConnection() {
        while (true) {
            if (status == IOStatus.CONNECTED || status == IOStatus.DISCONNECTED) {
                return status;
            }
            ThreadUtils.safeSleep(100);
        }
    }
}

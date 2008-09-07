package net.mauhiz.irc.base;

import java.io.IOException;
import java.net.InetSocketAddress;

import net.mauhiz.irc.AbstractRunnable;
import net.mauhiz.irc.base.data.IrcServer;
import net.mauhiz.irc.base.msg.Nick;
import net.mauhiz.irc.base.msg.User;

import org.apache.commons.net.SocketClient;
import org.apache.log4j.Logger;

/**
 * @author mauhiz
 */
public class IrcIO extends SocketClient implements IIrcIO {
    /**
     * @author mauhiz
     */
    enum Status {
        CONNECTED, CONNECTING, DISCONNECTED, DISCONNECTING;
    }
    /**
     * logger
     */
    private static final Logger LOG = Logger.getLogger(IrcIO.class);
    private final IIrcControl control;
    private IIrcOutput output;
    private final IrcServer server;
    private Status status = Status.DISCONNECTED;
    
    /**
     * @param ircControl
     * @param server1
     * @throws IOException
     */
    IrcIO(final IrcControl ircControl, final IrcServer server1) throws IOException {
        control = ircControl;
        status = Status.CONNECTING;
        server = server1;
        connect();
    }
    
    /**
     * @throws IOException
     */
    private void connect() throws IOException {
        InetSocketAddress address = server.getAddress();
        super.connect(address.getAddress(), address.getPort());
        IIrcInput input = new IrcInput(this, super._socket_);
        input.start();
        output = new IrcOutput(super._socket_);
        output.start();
        output.sendRawMsg(new Nick(server).toString());
        output.sendRawMsg(new User(server).toString());
    }
    /**
     * @see org.apache.commons.net.SocketClient#disconnect()
     */
    @Override
    public final void disconnect() {
        status = Status.DISCONNECTING;
        if (output != null) {
            output.stop();
            AbstractRunnable.sleep(2000);
        }
        if (isConnected()) {
            try {
                super.disconnect();
            } catch (IOException ioe) {
                LOG.error(ioe, ioe);
            }
        }
        status = Status.DISCONNECTED;
    }
    
    /**
     * @see net.mauhiz.irc.base.IIrcIO#getServer()
     */
    @Override
    public IrcServer getServer() {
        return server;
    }
    
    /**
     * @see net.mauhiz.irc.base.IIrcIO#getStatus()
     */
    public Status getStatus() {
        return status;
    }
    
    /**
     * @see net.mauhiz.irc.base.IIrcIO#processMsg(java.lang.String)
     */
    public void processMsg(final String msg) {
        if (msg == null) {
            return;
        }
        control.decodeIrcRawMsg(msg, this);
    }
    
    /**
     * @see net.mauhiz.irc.base.IIrcIO#reconnect()
     */
    public final void reconnect() throws IOException {
        disconnect();
        connect();
    }
    
    /**
     * @see net.mauhiz.irc.base.IIrcIO#sendMsg(java.lang.String)
     */
    public void sendMsg(final String msg) {
        output.sendRawMsg(msg);
    }
    
    /**
     * @see net.mauhiz.irc.base.IIrcIO#setStatus(net.mauhiz.irc.base.IrcIO.Status)
     */
    public void setStatus(final Status status1) {
        status = status1;
    }
}

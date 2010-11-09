package net.mauhiz.irc.base;

import java.io.IOException;
import java.net.InetSocketAddress;

import net.mauhiz.irc.base.data.IrcServer;
import net.mauhiz.irc.base.msg.Nick;
import net.mauhiz.irc.base.msg.User;
import net.mauhiz.util.AbstractRunnable;

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
    IrcIO(IrcControl ircControl, IrcServer server1) throws IOException {
        super();
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
        if (super._socket_ == null) {
            LOG.error("could not connect to " + address);
            return;
        }
        output = new IrcOutput(super._socket_);
        output.start();
        IIrcInput input = new IrcInput(this, super._socket_);
        input.start();
        sendMsg(new Nick(server).getIrcForm());
        sendMsg(new User(server).getIrcForm());
    }
    /**
     * @see org.apache.commons.net.SocketClient#disconnect()
     */
    @Override
    public void disconnect() {
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
    public void processMsg(String msg) {
        if (msg == null) {
            return;
        }
        control.decodeIrcRawMsg(msg, this);
    }
    
    /**
     * @see net.mauhiz.irc.base.IIrcIO#reconnect()
     */
    public void reconnect() throws IOException {
        disconnect();
        connect();
    }
    
    /**
     * @see net.mauhiz.irc.base.IIrcIO#sendMsg(String)
     */
    public void sendMsg(String msg) {
        int maxLen = server.getLineMaxLength();
        String trimmedMsg = msg.length() > maxLen ? msg.substring(0, maxLen) : msg;
        
        output.sendRawMsg(trimmedMsg);
    }
    
    /**
     * @see net.mauhiz.irc.base.IIrcIO#setStatus(net.mauhiz.irc.base.IrcIO.Status)
     */
    public void setStatus(Status status1) {
        status = status1;
    }
    
    @Override
    public void waitForConnection() {
        while (true) {
            if (status == Status.CONNECTED) {
                return;
            }
            AbstractRunnable.sleep(100);
        }
    }
}

package net.mauhiz.irc.base;

import java.io.IOException;

import net.mauhiz.irc.base.data.IrcServer;
import net.mauhiz.irc.base.msg.Nick;
import net.mauhiz.irc.base.msg.User;

import org.apache.commons.lang.builder.CompareToBuilder;
import org.apache.commons.net.SocketClient;

/**
 * @author mauhiz
 */
public class IrcIO extends SocketClient implements IIrcIO, Comparable<IrcIO> {
    /**
     * @author mauhiz
     */
    enum Status {
        CONNECTED, CONNECTING, DISCONNECTED;
    }
    IIrcControl control;
    IIrcOutput output;
    Status status = Status.DISCONNECTED;
    
    /**
     * @param ircControl
     */
    public IrcIO(final IrcControl ircControl) {
        control = ircControl;
    }
    
    /**
     * @see java.lang.Comparable#compareTo(java.lang.Object)
     */
    public int compareTo(final IrcIO o) {
        return CompareToBuilder.reflectionCompare(this, o);
    }
    
    /**
     * @see net.mauhiz.irc.base.IIrcIO#connect(net.mauhiz.irc.base.data.IrcServer)
     */
    public void connect(final IrcServer server) throws IOException {
        status = Status.CONNECTING;
        super.connect(server.getAddress().getAddress(), server.getAddress().getPort());
        IrcInput input = new IrcInput(this);
        input.connect(super._socket_);
        new Thread(input).start();
        output = new IrcOutput(super._socket_);
        new Thread(output).start();
        output.sendRawMsg(new Nick(server).toString());
        output.sendRawMsg(new User(server).toString());
    }
    
    /**
     * @see org.apache.commons.net.SocketClient#disconnect()
     */
    @Override
    public void disconnect() throws IOException {
        super.disconnect();
        status = Status.DISCONNECTED;
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
        control.decodeIrcRawMsg(msg, this);
    }
    
    /**
     * @see net.mauhiz.irc.base.IIrcIO#sendMsg(java.lang.String)
     */
    public void sendMsg(final String msg) {
        output.sendRawMsg(msg);
    }
    
    public void setStatus(final Status status1) {
        status = status1;
    }
}

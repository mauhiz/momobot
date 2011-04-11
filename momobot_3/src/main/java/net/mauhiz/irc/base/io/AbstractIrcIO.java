package net.mauhiz.irc.base.io;

import net.mauhiz.irc.base.IIrcControl;
import net.mauhiz.irc.base.data.IrcPeer;

import org.apache.log4j.Logger;

/**
 * @author mauhiz
 */
public abstract class AbstractIrcIO implements IIrcIO {
    private static final Logger LOG = Logger.getLogger(AbstractIrcIO.class);
    protected final IIrcControl control;
    protected IIrcOutput output;
    protected final IrcPeer peer;

    protected IOStatus status = IOStatus.DISCONNECTED;

    /**
     * @param ircControl
     * @param server1
     */
    protected AbstractIrcIO(IIrcControl ircControl, IrcPeer server1) {
        super();
        control = ircControl;
        status = IOStatus.CONNECTING;
        peer = server1;
    }

    @Override
    public IrcPeer getPeer() {
        return peer;
    }

    /**
     * @see net.mauhiz.irc.base.io.IIrcIO#getStatus()
     */
    public IOStatus getStatus() {
        return status;
    }

    /**
     * @see net.mauhiz.irc.base.io.IIrcIO#processMsg(java.lang.String)
     */
    public void processMsg(String msg) {
        if (msg == null) {
            return;
        }
        try {
            control.decodeIrcRawMsg(msg, this);
        } catch (RuntimeException rex) {
            LOG.error(rex, rex); // dont let it crash
        }
    }

    /**
     * @see net.mauhiz.irc.base.io.IIrcIO#sendMsg(String)
     */
    public final void sendMsg(String msg) {
        int maxLen = peer.getLineMaxLength();
        String trimmedMsg = msg.length() > maxLen ? msg.substring(0, maxLen) : msg;

        output.sendRawMsg(trimmedMsg);
    }

    /**
     * @see net.mauhiz.irc.base.io.IIrcIO#setStatus(IOStatus)
     */
    public void setStatus(IOStatus status1) {
        status = status1;
    }
}

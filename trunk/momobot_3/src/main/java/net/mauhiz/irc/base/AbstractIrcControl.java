package net.mauhiz.irc.base;

import net.mauhiz.irc.base.data.IrcDecoder;
import net.mauhiz.irc.base.data.IIrcServerPeer;
import net.mauhiz.irc.base.io.IIrcIO;
import net.mauhiz.irc.base.msg.IIrcMessage;
import net.mauhiz.irc.base.trigger.ITriggerManager;

import org.apache.commons.lang.NotImplementedException;

/**
 * @author mauhiz
 */
public abstract class AbstractIrcControl implements IIrcControl {
    private final ITriggerManager[] managers;

    public AbstractIrcControl(ITriggerManager... managers) {
        this.managers = managers;
    }

    /**
     * @see net.mauhiz.irc.base.IIrcControl#decodeIrcRawMsg(java.lang.String, net.mauhiz.irc.base.io.IIrcIO)
     */
    public void decodeIrcRawMsg(String raw, IIrcIO io) {
        IIrcServerPeer peer = io.getServerPeer();
        assert peer != null : "io had no associated decoder";

        IIrcMessage msg = IrcDecoder.getInstance().buildFromRaw(peer, raw);

        if (msg == null) {
            throw new NotImplementedException("Unknown msg: " + raw);
        }

        // FIXME implement basic processing as a trigger manager
        if (process(msg, io)) {
            return;
        } // common actions

        for (ITriggerManager itm : managers) {
            itm.processMsg(msg, this); // specific client actions
        }
    }

    /**
     * @return trigger manager
     */
    public ITriggerManager[] getManagers() {
        return managers;
    }

    protected abstract boolean process(IIrcMessage message, IIrcIO io);
}

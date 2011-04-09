package net.mauhiz.irc.base;

import net.mauhiz.irc.base.data.IrcPeer;
import net.mauhiz.irc.base.io.IIrcIO;
import net.mauhiz.irc.base.io.IOStatus;
import net.mauhiz.irc.base.msg.IIrcMessage;
import net.mauhiz.irc.base.msg.Notice;
import net.mauhiz.irc.base.trigger.ITriggerManager;

import org.apache.log4j.Logger;

/**
 * @author mauhiz
 */
public abstract class AbstractIrcControl implements IIrcControl {
    /**
     * logger.
     */
    private static final Logger LOG = Logger.getLogger(AbstractIrcControl.class);
    private final ITriggerManager manager;

    /**
     * @param mtm
     */
    public AbstractIrcControl(ITriggerManager mtm) {
        manager = mtm;
    }

    /**
     * @see net.mauhiz.irc.base.IIrcControl#decodeIrcRawMsg(java.lang.String, net.mauhiz.irc.base.io.IIrcIO)
     */
    public void decodeIrcRawMsg(String raw, IIrcIO io) {
        IrcPeer peer = io.getPeer();
        assert peer != null : "io had no associated server";

        IIrcMessage msg = peer.buildFromRaw(raw);
        assert msg != null;

        if (msg instanceof Notice && io.getStatus() == IOStatus.CONNECTING) {
            Notice notice = (Notice) msg;
            if (notice.getFrom() != null) {
                io.setStatus(IOStatus.CONNECTED);
                LOG.info("connected to " + msg.getServer().getAlias());
            }
            /* dont let it be processed */
            return;
        }
        try {
            msg.process(this); // common actions
        } catch (RuntimeException rex) {
            LOG.error(rex, rex); // dont let it crash
        }
        getManager().processMsg(msg, this); // specific client actions
    }

    /**
     * @return trigger manager
     */
    public ITriggerManager getManager() {
        return manager;
    }
}

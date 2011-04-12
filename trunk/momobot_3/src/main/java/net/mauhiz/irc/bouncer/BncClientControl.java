package net.mauhiz.irc.bouncer;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import net.mauhiz.irc.base.AbstractIrcControl;
import net.mauhiz.irc.base.data.IrcServer;
import net.mauhiz.irc.base.data.Target;
import net.mauhiz.irc.base.io.IIrcIO;
import net.mauhiz.irc.base.msg.IIrcMessage;
import net.mauhiz.irc.base.msg.Notice;
import net.mauhiz.irc.base.msg.Privmsg;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;

/**
 * @author mauhiz
 */
public class BncClientControl extends AbstractIrcControl {
    /**
     * logger.
     */
    private static final Logger LOG = Logger.getLogger(BncClientControl.class);

    private final Map<ClientPeer, BncClientIO> clientToIo = new HashMap<ClientPeer, BncClientIO>();

    public BncClientControl() {
        super(new BouncerTriggerManager());
    }

    @Override
    public void connect(IrcServer server) {
        throw new UnsupportedOperationException("This is a client contol, so it cannot connect to servers");
    }

    /**
     * @see net.mauhiz.irc.base.IIrcControl#exit()
     */
    @Override
    public void exit() {
        Collection<BncClientIO> ios = clientToIo.values();
        for (IIrcIO io : ios) {
            io.disconnect();
        }
    }

    /**
     * @see net.mauhiz.irc.base.IIrcControl#sendMsg(net.mauhiz.irc.base.msg.IIrcMessage)
     */
    @Override
    public void sendMsg(IIrcMessage msg) {
        if (msg instanceof Privmsg) {
            if (StringUtils.isEmpty(((Privmsg) msg).getMessage())) {
                LOG.info("Not sending empty msg", new IllegalArgumentException());
            }
        } else if (msg instanceof Notice) {
            if (StringUtils.isEmpty(((Notice) msg).getMessage())) {
                LOG.info("Not sending empty msg", new IllegalArgumentException());
            }
        }
        LOG.info(msg);

        // FIXME this is wrong
        Target from = msg.getFrom();
        IIrcIO io = clientToIo.get(from);
        io.sendMsg(msg.getIrcForm());
    }
}
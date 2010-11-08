package net.mauhiz.irc.base;

import java.io.IOException;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import net.mauhiz.irc.base.IrcIO.Status;
import net.mauhiz.irc.base.data.IrcServer;
import net.mauhiz.irc.base.ident.IdentServer;
import net.mauhiz.irc.base.msg.IIrcMessage;
import net.mauhiz.irc.base.msg.Notice;
import net.mauhiz.irc.base.msg.Privmsg;
import net.mauhiz.irc.base.msg.Quit;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;

/**
 * @author mauhiz
 */
public class IrcControl implements IIrcControl {
    /**
     * logger.
     */
    private static final Logger LOG = Logger.getLogger(IrcControl.class);
    private final ITriggerManager manager;
    /**
     * key = {@link IrcServer}, value = {@link IIrcIO}.
     */
    private final Map<IrcServer, IIrcIO> serverToIo = new HashMap<IrcServer, IIrcIO>();
    
    /**
     * @param mtm
     */
    public IrcControl(ITriggerManager mtm) {
        manager = mtm;
    }
    
    /**
     * @see net.mauhiz.irc.base.IIrcControl#connect(net.mauhiz.irc.base.data.IrcServer)
     */
    public void connect(IrcServer server) {
        if (serverToIo.containsKey(server)) {
            /* already connected */
            return;
        }
        
        try {
            new IdentServer(server.getMyself()).start();
            IIrcIO ircio = new IrcIO(this, server);
            serverToIo.put(server, ircio);
            ircio.waitForConnection();
        } catch (IOException e) {
            LOG.error(e, e); // cannot connect
        }
    }
    
    /**
     * @see net.mauhiz.irc.base.IIrcControl#decodeIrcRawMsg(java.lang.String, net.mauhiz.irc.base.IIrcIO)
     */
    public void decodeIrcRawMsg(String raw, IIrcIO io) {
        IrcServer server = io.getServer();
        if (server == null) {
            throw new IllegalStateException("io had no associated server");
        }
        IIrcMessage msg = server.buildFromRaw(raw);
        if (msg instanceof Notice && io.getStatus() == Status.CONNECTING) {
            Notice notice = (Notice) msg;
            if (notice.getFrom() != null) {
                io.setStatus(Status.CONNECTED);
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
     * @see net.mauhiz.irc.base.IIrcControl#exit()
     */
    public void exit() {
        Collection<IIrcIO> ios = serverToIo.values();
        for (IIrcIO io : ios) {
            io.disconnect();
        }
    }
    
    /**
     * @return trigger manager
     */
    public ITriggerManager getManager() {
        return manager;
    }
    
    public void quit(IrcServer server) {
        IIrcIO io = serverToIo.get(server);
        if (io != null) {
            io.disconnect();
            serverToIo.remove(server);
        }
    }
    
    /**
     * @see net.mauhiz.irc.base.IIrcControl#sendMsg(net.mauhiz.irc.base.msg.IIrcMessage)
     */
    public void sendMsg(IIrcMessage msg) {
        IrcServer server = msg.getServer();
        IIrcIO io = serverToIo.get(server);
        if (io == null) {
            throw new IllegalArgumentException("not connected to " + server.getAlias());
        }
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
        io.sendMsg(msg.getIrcForm());
        if (msg instanceof Quit) {
            quit(server);
        }
    }
}

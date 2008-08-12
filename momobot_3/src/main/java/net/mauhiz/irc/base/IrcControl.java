package net.mauhiz.irc.base;

import java.io.IOException;
import java.util.Collection;

import net.mauhiz.irc.base.IrcIO.Status;
import net.mauhiz.irc.base.data.IrcServer;
import net.mauhiz.irc.base.msg.IIrcMessage;
import net.mauhiz.irc.base.msg.Notice;
import net.mauhiz.irc.base.msg.Ping;
import net.mauhiz.irc.base.msg.Pong;

import org.apache.commons.collections.BidiMap;
import org.apache.commons.collections.bidimap.TreeBidiMap;
import org.apache.log4j.Logger;

/**
 * @author mauhiz
 */
public class IrcControl implements IIrcControl {
    /**
     * key = {@link IrcServer}, value = {@link IIrcIO}.
     */
    BidiMap ioMap = new TreeBidiMap();
    ITriggerManager manager;
    
    /**
     * @param mtm
     */
    public IrcControl(final ITriggerManager mtm) {
        manager = mtm;
    }
    
    /**
     * @see net.mauhiz.irc.base.IIrcControl#connect(net.mauhiz.irc.base.data.IrcServer)
     */
    public void connect(final IrcServer server) {
        if (ioMap.containsKey(server)) {
            /* already connected */
            return;
        }
        IIrcIO io = new IrcIO(this);
        ioMap.put(server, io);
        try {
            // new IdentServer().start(server.getMyLogin());
            io.connect(server);
            while (io.getStatus() != Status.CONNECTED) {
                Thread.yield();
            }
        } catch (IOException e) {
            Logger.getLogger(IrcControl.class).error(e);
        }
    }
    
    /**
     * @see net.mauhiz.irc.base.IIrcControl#decodeIrcRawMsg(java.lang.String,
     *      net.mauhiz.irc.base.IIrcIO)
     */
    public void decodeIrcRawMsg(final String raw, final IIrcIO io) {
        IrcServer server = (IrcServer) ioMap.getKey(io);
        if (server == null) {
            throw new IllegalStateException("io had no associated server");
        }
        IIrcMessage msg = server.buildFromRaw(raw);
        if (msg instanceof Ping) {
            sendMsg(new Pong(server, ((Ping) msg).getPingId()));
        } else {
            if (msg instanceof Notice && io.getStatus() == Status.CONNECTING) {
                Notice notice = (Notice) msg;
                if (notice.getFrom() != null) {
                    io.setStatus(Status.CONNECTED);
                }
            }
            manager.processMsg(msg, this);
        }
    }
    
    /**
     * @see net.mauhiz.irc.base.IIrcControl#exit()
     */
    public void exit() throws IOException {
        Collection<IIrcIO> ios = ioMap.values();
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
    
    /**
     * @see net.mauhiz.irc.base.IIrcControl#sendMsg(net.mauhiz.irc.base.msg.IIrcMessage)
     */
    public void sendMsg(final IIrcMessage msg) {
        IrcServer server = msg.getServer();
        IIrcIO io = (IIrcIO) ioMap.get(server);
        io.sendMsg(msg.toString());
    }
}

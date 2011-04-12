package net.mauhiz.irc.base;

import java.io.IOException;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import net.mauhiz.irc.base.data.IrcServer;
import net.mauhiz.irc.base.data.IrcUser;
import net.mauhiz.irc.base.ident.IdentServer;
import net.mauhiz.irc.base.io.IIrcIO;
import net.mauhiz.irc.base.io.IrcIO;
import net.mauhiz.irc.base.msg.IIrcMessage;
import net.mauhiz.irc.base.msg.Notice;
import net.mauhiz.irc.base.msg.Privmsg;
import net.mauhiz.irc.base.msg.Quit;
import net.mauhiz.irc.base.trigger.ITriggerManager;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;

/**
 * @author mauhiz
 */
public class IrcControl extends AbstractIrcControl implements IIrcClientControl {
    /**
     * logger.
     */
    private static final Logger LOG = Logger.getLogger(IrcControl.class);

    /**
     * key = {@link IrcServer}, value = {@link IIrcIO}.
     */
    private final Map<IrcServer, IIrcIO> serverToIo = new HashMap<IrcServer, IIrcIO>();

    /**
     * @param mtm
     */
    public IrcControl(ITriggerManager mtm) {
        super(mtm);
    }

    /**
     * @see net.mauhiz.irc.base.IIrcClientControl#connect(IrcServer)
     */
    public void connect(IrcServer server) {
        if (serverToIo.containsKey(server)) {
            /* already connected */
            return;
        }

        try {
            IrcUser myself = server.getMyself();
            if (myself == null) {
                throw new IllegalArgumentException("I have no name. Who am I?");
            }
            new IdentServer(myself).start();
            IrcIO ircio = new IrcIO(this, server);
            ircio.connect();
            serverToIo.put(server, ircio);
            ircio.waitForConnection();
        } catch (IOException e) {
            LOG.error(e, e); // cannot connect
        }
    }

    /**
     * @see net.mauhiz.irc.base.IIrcControl#exit()
     */
    public void exit() {
        Collection<IIrcIO> ios = serverToIo.values();
        for (IIrcIO io : ios) {
            io.disconnect();
        }
        serverToIo.clear();
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
        assert msg != null : "null msg";
        if (msg instanceof Privmsg) {
            if (StringUtils.isEmpty(((Privmsg) msg).getMessage())) {
                LOG.info("Not sending empty msg", new IllegalArgumentException());
                return;
            }
        } else if (msg instanceof Notice) {
            if (StringUtils.isEmpty(((Notice) msg).getMessage())) {
                LOG.info("Not sending empty msg", new IllegalArgumentException());
                return;
            }
        }

        LOG.info(msg);

        IrcServer server = msg.getServer();
        assert server != null : "no related server";

        IIrcIO io = serverToIo.get(server);
        assert io != null : "not connected to " + server.getAlias();

        io.sendMsg(msg.getIrcForm());
        if (msg instanceof Quit) {
            quit(server);
        }
    }
}

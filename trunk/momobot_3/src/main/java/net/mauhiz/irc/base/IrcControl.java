package net.mauhiz.irc.base;

import java.io.IOException;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import net.mauhiz.irc.base.IrcIO.Status;
import net.mauhiz.irc.base.data.IrcChannel;
import net.mauhiz.irc.base.data.IrcServer;
import net.mauhiz.irc.base.data.IrcUser;
import net.mauhiz.irc.base.data.Mask;
import net.mauhiz.irc.base.ident.IdentServer;
import net.mauhiz.irc.base.msg.IIrcMessage;
import net.mauhiz.irc.base.msg.Join;
import net.mauhiz.irc.base.msg.Kick;
import net.mauhiz.irc.base.msg.Mode;
import net.mauhiz.irc.base.msg.Nick;
import net.mauhiz.irc.base.msg.Notice;
import net.mauhiz.irc.base.msg.NumericReplies;
import net.mauhiz.irc.base.msg.Part;
import net.mauhiz.irc.base.msg.Ping;
import net.mauhiz.irc.base.msg.Pong;
import net.mauhiz.irc.base.msg.Quit;
import net.mauhiz.irc.base.msg.ServerMsg;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;

/**
 * @author mauhiz
 */
public class IrcControl implements IIrcControl, NumericReplies {
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
    public IrcControl(final ITriggerManager mtm) {
        manager = mtm;
    }
    
    /**
     * @see net.mauhiz.irc.base.IIrcControl#connect(net.mauhiz.irc.base.data.IrcServer)
     */
    public void connect(final IrcServer server) {
        if (serverToIo.containsKey(server)) {
            /* already connected */
            return;
        }
        
        try {
            IIrcIO ircio = new IrcIO(this, server);
            serverToIo.put(server, ircio);
            new IdentServer(server.getMyLogin()).start();
            while (ircio.getStatus() != Status.CONNECTED) {
                Thread.yield();
            }
        } catch (IOException e) {
            LOG.error(e);
        }
    }
    
    /**
     * @see net.mauhiz.irc.base.IIrcControl#decodeIrcRawMsg(java.lang.String, net.mauhiz.irc.base.IIrcIO)
     */
    public void decodeIrcRawMsg(final String raw, final IIrcIO io) {
        IrcServer server = io.getServer();
        if (server == null) {
            throw new IllegalStateException("io had no associated server");
        }
        IIrcMessage msg = server.buildFromRaw(raw);
        if (msg instanceof Ping) {
            sendMsg(new Pong(server, ((Ping) msg).getPingId()));
        } else if (msg instanceof ServerMsg) {
            processServerMsg((ServerMsg) msg);
        } else if (msg instanceof Join) {
            Join join = (Join) msg;
            IrcChannel joined = server.findChannel(join.getChan());
            IrcUser joiner = server.findUser(new Mask(join.getFrom()), true);
            joined.add(joiner);
        } else if (msg instanceof Kick) {
            Kick kick = (Kick) msg;
            IrcChannel from = server.findChannel(kick.getChan());
            IrcUser kicked = server.findUser(kick.getTarget(), true);
            from.remove(kicked);
        } else if (msg instanceof Part) {
            Part part = (Part) msg;
            IrcChannel from = server.findChannel(part.getChan());
            IrcUser leaver = server.findUser(part.getFrom(), true);
            from.remove(leaver);
        } else if (msg instanceof Quit) {
            Quit quit = (Quit) msg;
            IrcUser quitter = server.findUser(new Mask(quit.getFrom()), false);
            if (quitter != null) {
                for (IrcChannel every : server.getChannels()) {
                    every.remove(quitter);
                }
                server.remove(quitter);
            }
        } else if (msg instanceof Notice && io.getStatus() == Status.CONNECTING) {
            Notice notice = (Notice) msg;
            if (notice.getFrom() != null) {
                io.setStatus(Status.CONNECTED);
                LOG.info("connected to " + msg.getServer());
            }
            /* dont let it be processed */
            return;
        } else if (msg instanceof Nick) {
            Nick nick = (Nick) msg;
            Mask from = new Mask(nick.getFrom());
            IrcUser target = server.findUser(from, true);
            server.updateNick(target, nick.getNewNick());
        } else if (msg instanceof Mode) {
            /* TODO process mode */
        }
        getManager().processMsg(msg, this);
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
    
    /**
     * @param smsg
     */
    private void processServerMsg(final ServerMsg smsg) {
        switch (smsg.getCode()) {
            case RPL_UMODEIS :
                /* TODO process my user mode */
                LOG.debug("my mode: " + smsg.getMsg());
                break;
            case RPL_TOPIC :
                LOG.debug("topic: " + smsg.getMsg());
                /* TODO process topic */
                break;
            case RPL_TOPICINFO :
                LOG.debug("topic info: " + smsg.getMsg());
                /* TODO process topic info */
                break;
            case RPL_LUSERCLIENT :
                LOG.info("number of clients: " + smsg.getMsg());
                break;
            case RPL_LUSERCHANNELS :
                LOG.info("number of channels: " + smsg.getMsg());
                break;
            case RPL_LUSERME :
                LOG.info("server userme: " + smsg.getMsg());
                break;
            case RPL_MOTD :
                /* TODO store MOTD?? */
                LOG.info("Motd LINE: " + smsg.getMsg());
                break;
            case RPL_NAMREPLY :
                /* TODO process names */
                String names = smsg.getMsg();
                int sep = names.indexOf(" :");
                String chanName = names.substring(2, sep);
                String[] prefixedNames = StringUtils.split(names.substring(sep + 2));
                IrcChannel chan = smsg.getServer().findChannel(chanName);
                if (chan == null) {
                    return;
                }
                for (String prefixedName : prefixedNames) {
                    char prefix = prefixedName.charAt(0);
                    if (prefix == '+' || prefix == '@' || prefix == '%') {
                        prefixedName = prefixedName.substring(1);
                    }
                    IrcUser next = smsg.getServer().findUser(prefixedName, true);
                    chan.add(next);
                }
                LOG.info("Names Reply on " + chan + ": " + StringUtils.join(prefixedNames, ' '));
                break;
            case RPL_ENDOFNAMES :
                LOG.debug("End of Names Reply");
                break;
            case RPL_LUSEROP :
                LOG.info("list of operators: " + smsg.getMsg());
                break;
            case RPL_MOTDSTART :
                LOG.debug("Start of MOTD: " + smsg.getMsg());
                break;
            case ERR_NOTEXTTOSEND :
                LOG.warn("Server told me that I tried to send an empty msg");
                break;
            case RPL_ENDOFMOTD :
                LOG.debug("End of MOTD");
                break;
            case RPL_LUSERUNKNOWN :
                LOG.info("list of unknown users: " + smsg.getMsg());
                break;
            case ERR_QNETSERVICEIMMUNE :
                LOG.warn("I cannot do harm to a service! " + smsg.getMsg());
                break;
            default :
                LOG.warn("Unhandled server reply : " + smsg);
        }
    }
    
    /**
     * @see net.mauhiz.irc.base.IIrcControl#sendMsg(net.mauhiz.irc.base.msg.IIrcMessage)
     */
    public void sendMsg(final IIrcMessage msg) {
        IrcServer server = msg.getServer();
        IIrcIO io = serverToIo.get(server);
        io.sendMsg(msg.toString());
        if (msg instanceof Quit) {
            io.disconnect();
            serverToIo.remove(server);
        }
        
    }
}

package net.mauhiz.irc.base;

import java.io.IOException;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import net.mauhiz.irc.MomoStringUtils;
import net.mauhiz.irc.base.data.IIrcServerPeer;
import net.mauhiz.irc.base.data.IrcChannel;
import net.mauhiz.irc.base.data.IrcNetwork;
import net.mauhiz.irc.base.data.IrcUser;
import net.mauhiz.irc.base.data.Topic;
import net.mauhiz.irc.base.data.UserChannelMode;
import net.mauhiz.irc.base.data.WhoisRequest;
import net.mauhiz.irc.base.data.qnet.QnetServer;
import net.mauhiz.irc.base.ident.IdentServer;
import net.mauhiz.irc.base.io.IIrcIO;
import net.mauhiz.irc.base.io.IOStatus;
import net.mauhiz.irc.base.io.IrcClientIO;
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
import net.mauhiz.irc.base.msg.Privmsg;
import net.mauhiz.irc.base.msg.Quit;
import net.mauhiz.irc.base.msg.ServerError;
import net.mauhiz.irc.base.msg.ServerMsg;
import net.mauhiz.irc.base.msg.SetTopic;
import net.mauhiz.irc.base.trigger.ITriggerManager;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;

/**
 * @author mauhiz
 */
public class IrcClientControl extends AbstractIrcControl implements IIrcClientControl {
    /**
     * logger.
     */
    private static final Logger LOG = Logger.getLogger(IrcClientControl.class);

    /**
     * key = {@link IIrcServerPeer}, value = {@link IIrcIO}.
     */
    private final Map<IIrcServerPeer, IIrcIO> serverToIo = new HashMap<IIrcServerPeer, IIrcIO>();

    public IrcClientControl(ITriggerManager... managers) {
        super(managers);
    }

    public void connect(IIrcServerPeer peer) {
        if (serverToIo.containsKey(peer)) {
            /* already connected */
            return;
        }

        IrcUser myself = peer.getMyself();
        if (myself == null) {
            throw new IllegalArgumentException("I have no name. Who am I?");
        }
        new IdentServer(myself).start();
        IrcClientIO ircio = new IrcClientIO(this, peer);
        try {
            ircio.connect();
            serverToIo.put(peer, ircio);
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

    private void handleNamReply(String msg, IrcNetwork network) {
        int sep = msg.indexOf(" :");
        String chanName = msg.substring(2, sep);
        IrcChannel chan = network.findChannel(chanName);
        if (chan == null) {
            return;
        }

        String[] prefixedNames = StringUtils.split(msg.substring(sep + 2));
        for (String prefixedName : prefixedNames) {
            char prefix = prefixedName.charAt(0);
            if (prefix == '+' || prefix == '@' || prefix == '%') {
                prefixedName = prefixedName.substring(1);
            }
            IrcUser next = network.findUser(prefixedName, true);
            chan.add(next);
        }
        LOG.debug("Names Reply on " + chan + ": " + StringUtils.join(prefixedNames, ' '));
    }

    private void handleRplTopic(String msg, IrcNetwork network) {
        int sep = msg.indexOf(" :");
        String chanName = msg.substring(0, sep);
        String topic = msg.substring(sep + 2);
        LOG.debug("topic set on " + chanName + " : " + topic);
        IrcChannel chan = network.findChannel(chanName);
        if (chan != null) {
            chan.getProperties().getTopic().setValue(topic);
        }
    }

    private void handleWhoisChannels(String msg, IrcNetwork server) {
        String nick = StringUtils.substringBefore(msg, " ");
        IrcUser ircUser = server.findUser(nick, true);

        String lstChanNames = StringUtils.substringAfter(msg, " :");
        String[] chanNames = StringUtils.split(lstChanNames, " ");
        for (String chanName : chanNames) {
            if (!MomoStringUtils.isChannelName(chanName)) {
                chanName = chanName.substring(1);
            }
            IrcChannel channel = server.findChannel(chanName, false); // osef des infos qui sont pas sur mon chan
            if (channel != null) {
                channel.add(ircUser);
            }
        }
    }

    private void handleWhoisUser(String msg, IrcNetwork server) {
        String nick = StringUtils.substringBefore(msg, " ");
        IrcUser ircUser = server.findUser(nick, true);

        String remaining = StringUtils.substringAfter(msg, " ");
        String user = StringUtils.substringBefore(remaining, " ");
        ircUser.getMask().setUser(user);

        remaining = StringUtils.substringAfter(remaining, " ");
        String host = StringUtils.substringBefore(remaining, " ");
        ircUser.getMask().setHost(host);

        String fullName = StringUtils.substringAfter(remaining, " :");
        ircUser.setFullName(fullName);
    }

    @Override
    public boolean process(IIrcMessage message, IIrcIO io) {
        IIrcServerPeer serverPeer = message.getServerPeer();
        IrcNetwork network = serverPeer.getNetwork();

        if (message instanceof Ping) {
            sendMsg(new Pong(serverPeer, ((Ping) message).getPingId()));
            return true;
        } else if (message instanceof Join) {
            IrcChannel joined = (IrcChannel) message.getTo();
            IrcUser joiner = (IrcUser) message.getFrom();
            joined.add(joiner);
        } else if (message instanceof Kick) {
            IrcChannel chan = (IrcChannel) message.getTo();
            IrcUser target = ((Kick) message).getTarget();
            chan.remove(target);
            if (target.equals(serverPeer.getMyself())) {
                network.remove(chan);
            }
        } else if (message instanceof Mode) {
            processMode((Mode) message);

        } else if (message instanceof Nick) {
            IrcUser target = (IrcUser) message.getFrom();
            network.updateNick(target, ((Nick) message).getNewNick());
        } else if (message instanceof Part) {
            IrcChannel fromChan = (IrcChannel) message.getTo();
            if (fromChan != null) {
                IrcUser leaver = (IrcUser) message.getFrom();
                fromChan.remove(leaver);
            }
        } else if (message instanceof Quit) {
            IrcUser quitter = (IrcUser) message.getFrom();
            if (quitter != null) {
                for (IrcChannel every : network.getChannels()) {
                    every.remove(quitter);
                }
                network.remove(quitter);
            }
        } else if (message instanceof ServerError) {
            quit(serverPeer);
        } else if (message instanceof ServerMsg) {
            processServerMsg((ServerMsg) message);
        } else if (message instanceof SetTopic) {
            IrcChannel chan = (IrcChannel) message.getTo();
            IrcUser changer = (IrcUser) message.getFrom();
            Topic newTopic = new Topic(changer.getNick(), new Date(), ((SetTopic) message).getTopic());
            chan.getProperties().setTopic(newTopic);
        } else if (message instanceof Notice) {
            if (io != null && io.getStatus() == IOStatus.CONNECTING) {
                Notice notice = (Notice) message;
                if (notice.getFrom() != null) {
                    io.setStatus(IOStatus.CONNECTED);
                    LOG.info("connected to " + network.getAlias());
                }
                /* dont let it be processed */
                return true;
            }

        }
        return false;
    }

    private void processMode(Mode message) {
        String modeQuery = message.getMessage();
        String[] toks = StringUtils.split(modeQuery, ' ');

        if (message.isToChannel()) {
            IrcChannel chan = (IrcChannel) message.getTo();
            int argIdx = 0;
            String modeInfo = toks[argIdx++]; // consume 1st argument
            boolean set = false;

            for (int idx = 0; idx < modeInfo.length(); idx++) {
                char next = modeInfo.charAt(idx);

                if (Mode.isModifier(next)) {
                    set = next == '+';
                    continue;
                }

                char modeItem = modeInfo.charAt(idx);
                UserChannelMode newMode = UserChannelMode.fromCmd(modeItem);

                if (newMode == null) {
                    // channel mode
                    chan.getProperties().process(set, modeItem);

                } else {
                    // user mode
                    String target = toks[argIdx++];
                    IrcUser targetUser = message.getServerPeer().getNetwork().findUser(target, true);
                    Set<UserChannelMode> userModes = chan.getModes(targetUser);

                    if (set) {
                        userModes.add(newMode);

                    } else {
                        userModes.remove(newMode);
                    }
                }
            }
        } else {
            IrcUser target = (IrcUser) message.getTo();
            char modifier = modeQuery.charAt(0);
            char mode = modeQuery.charAt(1);
            if (mode == 'i') {
                target.getProperties().setInvisible(modifier == '+');

            } else {
                LOG.warn("TODO process user mode: " + modeQuery);
            }
        }
    }

    private void processServerMsg(ServerMsg message) {
        String msg = message.getMsg();
        int code = message.getCode();
        if (code <= 5) {
            LOG.info("Greeting #" + code + ": " + msg);
            return;
        }
        NumericReplies reply = NumericReplies.fromCode(code);
        if (reply == null) {
            LOG.warn("Unknown code: " + code);
            return;
        }
        IIrcServerPeer peer = message.getServerPeer();
        IrcNetwork network = message.getServerPeer().getNetwork();
        switch (reply) {
            case RPL_UMODEIS:
                Mode mode = new Mode(peer, peer.getMyself(), peer, msg);
                processMode(mode);
                break;
            case RPL_TOPIC:
                handleRplTopic(msg, network);
                break;
            case RPL_TOPICINFO:
                LOG.info("topic info: " + msg);
                break;
            case RPL_LUSERCLIENT:
                LOG.info("number of clients: " + msg);
                break;
            case RPL_LUSERCHANNELS:
                LOG.info("number of channels: " + msg);
                break;
            case RPL_LUSERME:
                LOG.info("server userme: " + msg);
                break;
            case RPL_MOTD:
                LOG.info("Motd LINE: " + msg);
                break;
            case RPL_NAMREPLY:
                handleNamReply(msg, network);
                break;
            case RPL_ENDOFNAMES:
                LOG.debug("End of Names Reply");
                break;
            case RPL_LUSEROP:
                LOG.info("list of operators: " + msg);
                break;
            case RPL_MOTDSTART:
                LOG.debug("Start of MOTD: " + msg);
                break;
            case ERR_NOTEXTTOSEND:
                LOG.warn("Server told me that I tried to send an empty msg");
                break;
            case RPL_ENDOFMOTD:
                LOG.debug("End of MOTD");
                break;
            case RPL_LUSERUNKNOWN:
                LOG.info("list of unknown users: " + msg);
                break;
            case ERR_QNETSERVICEIMMUNE:
                LOG.warn("I cannot do harm to a service! " + msg);
                break;
            case RPL_WHOISUSER:
                handleWhoisUser(msg, network);
                break;
            case ERR_NOSUCHNICK:
                String unkNick = StringUtils.substringBefore(msg, " :");
                WhoisRequest.end(unkNick, false);
                break;
            case RPL_WHOISCHANNELS:
                handleWhoisChannels(msg, network);
                break;
            case RPL_WHOISSERVER:
                LOG.warn("TODO whois server : " + msg);
                // msg = mauhiz *.quakenet.org :QuakeNet IRC Server
                break;
            case RPL_WHOISAUTH: // this message is specific to Qnet Servers
                ((QnetServer) network).handleWhois(msg);
                break;
            case RPL_ENDOFWHOIS:
                String nick = StringUtils.substringBefore(msg, " ");
                WhoisRequest.end(nick, true);
                break;
            case ERR_CHANOPRIVSNEEDED:
                LOG.warn("I am not channel operator. " + StringUtils.substringBefore(msg, " "));
                break;
            case ERR_NOTREGISTERED:
                LOG.warn("I should register before sending commands!");
                break;
            case RPL_WHOISIDLE:
                LOG.debug("User has been idle : " + StringUtils.substringAfter(msg, " "));
                break;
            case RPL_WHOISOPERATOR:
                String opNick = StringUtils.substringBefore(msg, " ");
                WhoisRequest.end(opNick, true);

                String opText = StringUtils.substringAfter(msg, ":");
                LOG.debug(opNick + " " + opText);
                break;
            case ERR_NICKNAMEINUSE:
                String nickInUse = StringUtils.substringBefore(msg, " ");
                LOG.warn(nickInUse + " is already in use");

                // TODO use alternate nicknames from config ?
                String newNick = nickInUse + "_";
                sendMsg(new Nick(message.getServerPeer(), null, newNick));

                break;
            case ERR_NOTONCHANNEL:
                LOG.warn("[TODO process] " + msg);
                break;
            case ERR_NOSUCHCHANNEL:
                LOG.warn("[TODO process] " + msg);
                break;
            default:
                // TODO 42, 265, 266, 439 on Rizon
                LOG.warn("Unhandled server reply : " + this);
                break;
        }
    }

    public void quit(IIrcServerPeer peer) {
        IIrcIO io = serverToIo.get(peer);
        if (io != null) {
            io.disconnect();
            serverToIo.remove(peer);
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

        IIrcIO io = serverToIo.get(msg.getServerPeer());
        assert io != null : "not connected to " + msg.getServerPeer().getNetwork().getAlias();

        io.sendMsg(msg.getIrcForm());
        if (msg instanceof Quit) {
            quit(msg.getServerPeer());
        }
    }
}

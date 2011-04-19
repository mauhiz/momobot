package net.mauhiz.irc.base;

import java.io.IOException;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import net.mauhiz.irc.MomoStringUtils;
import net.mauhiz.irc.base.data.ArgumentList;
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
import net.mauhiz.irc.base.msg.IPrivateIrcMessage;
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

    private void handleNamReply(ServerMsg msg, IrcNetwork network) {
        ArgumentList args = msg.getArgs();
        args.poll(); // =
        String chanName = args.poll();
        IrcChannel chan = network.findChannel(chanName);
        if (chan == null) {
            return;
        }

        ArgumentList prefixedNames = new ArgumentList(msg.getMsg());
        for (String prefixedName : prefixedNames) {
            char prefix = prefixedName.charAt(0);
            if (UserChannelMode.isDisplay(prefix)) {
                prefixedName = prefixedName.substring(1);
            }
            IrcUser next = network.findUser(prefixedName, true);
            chan.add(next);
            chan.getModes(next).add(UserChannelMode.fromDisplay(prefix));
        }
        LOG.debug("Names Reply on " + chan + ": " + prefixedNames);
    }

    private void handleRplTopic(ServerMsg rplTopic, IrcNetwork network) {
        String chanName = rplTopic.getArgs().peek();
        String topic = rplTopic.getMsg();
        LOG.debug("topic set on " + chanName + " : " + topic);
        IrcChannel chan = network.findChannel(chanName);
        if (chan != null) {
            chan.getProperties().getTopic().setValue(topic);
        }
    }

    private void handleWhoisChannels(ServerMsg whoisChannels, IrcNetwork server) {
        String nick = whoisChannels.getArgs().peek();
        IrcUser ircUser = server.findUser(nick, true);

        ArgumentList chanNames = new ArgumentList(whoisChannels.getMsg());
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

    private void handleWhoisUser(ServerMsg whoisUser, IrcNetwork server) {
        ArgumentList args = whoisUser.getArgs();
        String nick = args.poll();
        IrcUser ircUser = server.findUser(nick, true);
        ircUser.getMask().setUser(args.poll());
        ircUser.getMask().setHost(args.poll());
        ircUser.setFullName(whoisUser.getMsg());
    }

    @Override
    public boolean process(IIrcMessage message, IIrcIO io) {
        IIrcServerPeer serverPeer = message.getServerPeer();
        IrcNetwork network = serverPeer.getNetwork();

        if (message instanceof Ping) {
            sendMsg(new Pong(serverPeer, ((Ping) message).getPingId()));
            return true;
        } else if (message instanceof Join) {
            IrcChannel[] joined = ((Join) message).getChans();
            IrcUser joiner = ((Join) message).getFrom();
            for (IrcChannel channel : joined) {
                channel.add(joiner);
            }
        } else if (message instanceof Kick) {
            IrcChannel chan = ((Kick) message).getChan();
            IrcUser target = ((Kick) message).getTarget();
            chan.remove(target);
            if (target.equals(serverPeer.getMyself())) {
                network.remove(chan);
            }
        } else if (message instanceof Mode) {
            processMode((Mode) message);

        } else if (message instanceof Nick) {
            IrcUser target = ((Nick) message).getFrom();
            network.updateNick(target, ((Nick) message).getNewNick());
        } else if (message instanceof Part) {
            IrcChannel[] fromChan = ((Part) message).getChannels();
            if (fromChan != null) {
                IrcUser leaver = ((Part) message).getFrom();
                for (IrcChannel channel : fromChan) {
                    channel.remove(leaver);
                }
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
            IrcChannel chan = ((SetTopic) message).getChan();
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
        ArgumentList toks = message.getArgs();

        if (message.getModifiedObject() instanceof IrcChannel) {
            IrcChannel chan = (IrcChannel) message.getModifiedObject();
            String modeInfo = toks.poll(); // consume 1st argument
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
                    String target = toks.poll();
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
            IrcUser target = (IrcUser) message.getModifiedObject();
            String modeQuery = toks.poll();
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
        ArgumentList args = message.getArgs();
        int code = message.getCode();
        if (code <= 5) {
            LOG.info("Greeting #" + code + ": " + message.getMsg());
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
                Mode mode = new Mode(peer, peer, peer.getMyself(), args);
                processMode(mode);
                break;
            case RPL_TOPIC:
                handleRplTopic(message, network);
                break;
            case RPL_TOPICINFO:
                LOG.info("topic info: " + args);
                break;
            case RPL_LUSERCLIENT:
                LOG.info("client statistics: " + message.getMsg());
                break;
            case RPL_LUSERCHANNELS:
                LOG.info("number of channels: " + args);
                break;
            case RPL_LUSERME:
                LOG.info("server userme: " + message.getMsg());
                break;
            case RPL_MOTD:
                LOG.info("Motd LINE: " + message.getMsg());
                break;
            case RPL_NAMREPLY:
                handleNamReply(message, network);
                break;
            case RPL_ENDOFNAMES:
                LOG.debug("End of Names Reply");
                break;
            case RPL_LUSEROP:
                LOG.info("number of operators: " + args);
                break;
            case RPL_MOTDSTART:
                LOG.debug("Start of MOTD: " + args);
                break;
            case ERR_NOTEXTTOSEND:
                LOG.warn("Server told me that I tried to send an empty msg");
                break;
            case RPL_ENDOFMOTD:
                LOG.debug("End of MOTD");
                break;
            case RPL_LUSERUNKNOWN:
                LOG.info("number of unknown users: " + args);
                break;
            case ERR_QNETSERVICEIMMUNE:
                LOG.warn("I cannot do harm to a service! " + args);
                break;
            case RPL_WHOISUSER:
                handleWhoisUser(message, network);
                break;
            case ERR_NOSUCHNICK:
                WhoisRequest.end(args, false);
                break;
            case RPL_WHOISCHANNELS:
                handleWhoisChannels(message, network);
                break;
            case RPL_WHOISSERVER:
                LOG.info(args.poll() + " is on node " + args.poll());
                break;
            case RPL_WHOISAUTH: // this message is specific to Qnet Servers
                ((QnetServer) network).handleWhois(args);
                break;
            case RPL_ENDOFWHOIS:
                WhoisRequest.end(args, true);
                break;
            case ERR_CHANOPRIVSNEEDED:
                LOG.warn("I am not channel operator. " + args.peek());
                break;
            case ERR_NOTREGISTERED:
                LOG.warn("I should register before sending commands!");
                break;
            case RPL_WHOISIDLE:
                LOG.debug("User has been idle : " + args.peek());
                break;
            case RPL_WHOISOPERATOR:
                WhoisRequest.end(args, true);

                LOG.debug(args + " " + message.getMsg());
                break;
            case ERR_NICKNAMEINUSE:
                String nickInUse = args.peek();
                LOG.warn(nickInUse + " is already in use");

                // TODO do nothing if already have a valid nickname
                // TODO use alternate nicknames from config ?
                String newNick = nickInUse + "_";
                IIrcServerPeer server = message.getServerPeer();
                sendMsg(new Nick(server, null, newNick));
                IrcUser oldMyself = server.getMyself();
                message.getServerPeer()
                        .introduceMyself(newNick, oldMyself.getMask().getUser(), oldMyself.getFullName());
                break;
            case ERR_NOTONCHANNEL:
                LOG.warn("[TODO process] " + args);
                break;
            case ERR_NOSUCHCHANNEL:
                LOG.warn("[TODO process] " + args);
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
        LOG.info(msg);
        IIrcIO io = serverToIo.get(msg.getServerPeer());

        if (msg instanceof IPrivateIrcMessage) {
            if (StringUtils.isEmpty(((IPrivateIrcMessage) msg).getMessage())) {
                LOG.info("Not sending empty msg", new IllegalArgumentException());
                return;
            }
        }

        String ircForm = msg.getIrcForm();
        io.sendMsg(ircForm);

        if (msg instanceof IPrivateIrcMessage) {
            io.processMsg(ircForm); // process it back
        }

        if (msg instanceof Quit) {
            quit(msg.getServerPeer());
        }
    }
}

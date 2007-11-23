package ircbot;

import static ircbot.CtcpUtils.sendCtcpMsg;
import static java.util.concurrent.TimeUnit.MILLISECONDS;
import static java.util.concurrent.TimeUnit.MINUTES;
import static org.apache.commons.lang.StringUtils.substringAfter;
import static utils.NetUtils.byteTabIpToLong;
import ircbot.dcc.DccChat;
import ircbot.dcc.DccFileTransfer;
import ircbot.dcc.DccManager;
import ircbot.dcc.IDccSubCommands;
import ircbot.ident.IdentServer;

import java.io.File;
import java.io.IOException;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Date;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;
import java.util.concurrent.ConcurrentHashMap;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.text.StrBuilder;
import org.apache.commons.lang.text.StrTokenizer;
import org.apache.log4j.Logger;

/**
 * Tout ce qui reste du PircBot original, c'est le nom et une paire de méthodes. Owned.
 * 
 * @author mauhiz
 */
public abstract class AbstractIrcBot implements IIrcConstants, IIrcCommands, IIrcSpecialChars, ICtcpCommands,
        IDccSubCommands, IIrcBot {
    /**
     * le nom.
     */
    private static final String           DEFAULT_NAME = "PircBot";
    /**
     * logger.
     */
    private static final Logger           LOG          = Logger.getLogger(AbstractIrcBot.class);
    /**
     * Les ports pour le DCC. Osef complètement, il est pas prévu que le bot fasse des xdcc pour l'instant
     */
    public static final Set < Character > PORTS        = new TreeSet < Character >();
    /**
     * ce que je réponds sur un CTCP version.
     */
    private static final String           VERSION      = "momoBot";
    static {
        PORTS.add(Character.valueOf((char) 1025));
        PORTS.add(Character.valueOf((char) 1026));
        PORTS.add(Character.valueOf((char) 1027));
    }

    /**
     * @param ports
     * @return un serverSocket
     * @throws IOException
     */
    public static ServerSocket tryToBind(final Set < Character > ports) throws IOException {
        if (ports.isEmpty()) {
            /* Use any free port. */
            return new ServerSocket(0);
        }
        for (final int element : ports) {
            try {
                return new ServerSocket(element);
                /* Found a port number we could use. */
            } catch (final IOException ioe) {
                /* port is in use */
                continue;
            }
        }
        /* No ports could be used. */
        throw new IOException("No available port...");
    }
    /**
     * Me dit si je suis connecté.
     */
    private boolean                      connected;
    /**
     * Un manager to rule them all.
     */
    private final DccManager             dccManager = new DccManager(this);
    /**
     * Mon doigt.
     */
    private String                       finger     = "You ought to be arrested for fingering a bot!";
    /**
     * le login.
     */
    private String                       myLogin    = DEFAULT_NAME;
    /**
     * Mon nom, celui que je veux avoir comme nick.
     */
    private String                       myName     = DEFAULT_NAME;
    /**
     * le nick que le bot a conscience d'avoir. Il peut parfois différer de son nick réel en cas de désynchro.
     */
    private String                       myNick;
    /**
     * Mon thread qui me débarasse de ce que je dois dire.
     */
    private OutputQueue                  outputThread;
    /**
     * Mon serveur IRC où je vais me connecter.
     */
    private IrcServerBean                server;
    /**
     * une poubelle avec un topic temporaire dedans. Il faudrait que je trouve plus élégant.
     */
    private String                       tempTopic;
    /**
     * une poubelle avec plein d'users.
     */
    private final Map < String, String > tempUsers  = new ConcurrentHashMap < String, String >();

    /**
     * @see IIrcBot#ban(java.lang.String, java.lang.String)
     */
    public final void ban(final String channel, final String hostmask) {
        send(MODE + SPC + channel + SPC + PLUS + Mode.BAN + SPC + hostmask);
    }

    /**
     * @see IIrcBot#changeNick(java.lang.String)
     */
    public final void changeNick(final String newNick) {
        send(NICK + SPC + newNick);
    }

    /**
     * @see IIrcBot#connect(ircbot.IrcServerBean)
     */
    public final void connect(final IrcServerBean server1) {
        this.server = server1;
        final int timeout = (int) MILLISECONDS.convert(5, MINUTES);
        try {
            final Socket socket = new Socket();
            socket.connect(this.server.getAddress());
            socket.setSoTimeout(timeout);
            this.outputThread = new OutputQueue(socket);
            new InputProcessor(this, socket).execute();
            this.outputThread.execute();
            // Attempt to join the server.
            if (!StringUtils.isEmpty(this.server.getPassword())) {
                send(PASS + SPC + this.server.getPassword());
            }
            send(NICK + SPC + getMyName());
            send(USER + SPC + this.myLogin.toLowerCase(Locale.US) + SPC + '8' + SPC + '*' + SPC + COLON + VERSION);
            this.myNick = getMyName();
        } catch (final IOException ioe) {
            LOG.fatal(ioe, ioe);
        }
    }

    /**
     * @see IIrcBot#dccSendChatRequest(IrcUser, int)
     */
    public final DccChat dccSendChatRequest(final IrcUser user, final int timeout) {
        try {
            final ServerSocket socketServer = tryToBind(PORTS);
            socketServer.setSoTimeout(timeout);
            final int port = socketServer.getLocalPort();
            /* TODO : autoriser le réglage externe dans le cas d'un NAT. */
            sendCtcpMsg(DCC_CHAT + SPC + "chat" + SPC + byteTabIpToLong(InetAddress.getLocalHost().getAddress()) + SPC
                    + port, user.getNick(), this.outputThread);
            /* The client may now connect to us to chat. */
            final Socket socket = socketServer.accept();
            /* Close the server socket now that we've finished with it. */
            socketServer.close();
            return new DccChat(socket);
        } catch (final IOException ioe) {
            LOG.error(ioe, ioe);
        }
        return null;
    }

    /**
     * @see IIrcBot#dccSendFile(java.io.File, ircbot.IrcUser, int)
     */
    public final DccFileTransfer dccSendFile(final File file, final IrcUser user, final int timeout) {
        final DccFileTransfer transfer = new DccFileTransfer(this, file, user, timeout);
        transfer.doSend(true);
        return transfer;
    }

    /**
     * @see IIrcBot#deOp(ircbot.Channel, java.lang.String)
     */
    public final void deOp(final Channel channel, final String nick) {
        setMode(channel, MINUS + Mode.OP + SPC + nick);
    }

    /**
     * @see IIrcBot#deVoice(ircbot.Channel, java.lang.String)
     */
    public final void deVoice(final Channel channel, final String nick) {
        setMode(channel, MINUS + Mode.VOICE + SPC + nick);
    }

    /**
     * @return le dcc manager
     */
    public final DccManager getDccManager() {
        return this.dccManager;
    }

    /**
     * @see IIrcBot#getMyName()
     */
    public final String getMyName() {
        return this.myName;
    }

    /**
     * @see IIrcBot#getNick()
     */
    public final String getNick() {
        return this.myNick;
    }

    /**
     * @return le outputThread
     */
    public final OutputQueue getOutputThread() {
        return this.outputThread;
    }

    /**
     * @see IIrcBot#isConnected()
     */
    public final boolean isConnected() {
        return this.connected;
    }

    /**
     * @see IIrcBot#joinChannel(java.lang.String)
     */
    public final void joinChannel(final String channel) {
        send(JOIN + SPC + channel);
    }

    /**
     * @see IIrcBot#joinChannel(java.lang.String, java.lang.String)
     */
    public final void joinChannel(final String channel, final String key) {
        joinChannel(channel + SPC + key);
    }

    /**
     * @see IIrcBot#kick(Channel, IrcUser)
     */
    public final void kick(final Channel channel, final IrcUser tempNick) {
        kick(channel, tempNick, EMPTY);
    }

    /**
     * @see IIrcBot#kick(Channel, IrcUser, java.lang.String)
     */
    public final void kick(final Channel channel, final IrcUser nick, final String reason) {
        send(KICK + SPC + channel + SPC + nick + SPC + COLON + reason);
    }

    /**
     * @see IIrcBot#listChannels()
     */
    public final void listChannels() {
        send(LIST);
    }

    /**
     * @see IIrcBot#listChannels(java.lang.String)
     */
    public final void listChannels(final String parameters) {
        send(LIST + SPC + parameters);
    }

    /**
     * @see IIrcBot#onConnect()
     */
    public void onConnect() {
        setConnected(true);
    }

    /**
     * @see IIrcBot#onDeop(Channel, IrcUser, String)
     */
    @SuppressWarnings("unused")
    public void onDeop(final Channel channel, final IrcUser user, final String recipient) {
        channel.removeOp(recipient);
    }

    /**
     * @see IIrcBot#onDeVoice(ircbot.Channel, ircbot.IrcUser, java.lang.String)
     */
    @SuppressWarnings("unused")
    public void onDeVoice(final Channel channel, final IrcUser user, final String recipient) {
        channel.removeVoice(recipient);
    }

    /**
     * @see IIrcBot#onDisconnect()
     */
    public void onDisconnect() {
        this.outputThread.setRunning(false);
        Channel.removeAll();
    }

    /**
     * @see IIrcBot#onFinger(IrcUser, String)
     */
    @SuppressWarnings("unused")
    public void onFinger(final IrcUser user, final String target) {
        CtcpUtils.sendCtcpNotice(P_FINGER + SPC + this.finger, user.getNick(), this.outputThread);
    }

    /**
     * @see IIrcBot#onJoin(Channel, IrcUser)
     */
    public void onJoin(final Channel channel, final IrcUser user) {
        channel.addUser(user);
    }

    /**
     * @see IIrcBot#onKick(Channel, IrcUser, String, String)
     */
    @SuppressWarnings("unused")
    public void onKick(final Channel channel, final IrcUser user, final String recipientNick, final String reason) {
        if (recipientNick.equalsIgnoreCase(getNick())) {
            Channel.removeChannel(channel.getNom());
            return;
        }
        channel.remove(recipientNick);
    }

    /**
     * @see IIrcBot#onOp(Channel, IrcUser, String)
     */
    @SuppressWarnings("unused")
    public void onOp(final Channel channel, final IrcUser user, final String recipient) {
        channel.giveOp(user.getNick());
    }

    /**
     * @see IIrcBot#onPart(Channel, IrcUser)
     */
    public void onPart(final Channel channel, final IrcUser user) {
        channel.remove(user.getNick());
        if (user.getNick().equalsIgnoreCase(getNick())) {
            Channel.removeChannel(channel.getNom());
        }
    }

    /**
     * @see IIrcBot#onPing(ircbot.IrcUser, java.lang.String, java.lang.String)
     */
    @SuppressWarnings("unused")
    public void onPing(final IrcUser user, final String target, final String pingValue) {
        CtcpUtils.sendCtcpNotice(user.getNick(), P_PING + SPC + pingValue, this.outputThread);
    }

    /**
     * @see IIrcBot#onQuit(IrcUser, String)
     */
    @SuppressWarnings("unused")
    public void onQuit(final IrcUser user, final String reason) {
        IrcUser.removeUser(user.getNick());
        for (final Channel chan : Channel.getAll()) {
            chan.remove(user.getNick());
        }
    }

    /**
     * @see IIrcBot#onRemoveChannelKey(ircbot.Channel, ircbot.IrcUser, java.lang.String)
     */
    @SuppressWarnings("unused")
    public void onRemoveChannelKey(final Channel channel, final IrcUser user, final String key) {
        channel.unSetKey();
    }

    /**
     * @see IIrcBot#onRemoveChannelLimit(ircbot.Channel, ircbot.IrcUser)
     */
    @SuppressWarnings("unused")
    public void onRemoveChannelLimit(final Channel channel, final IrcUser user) {
        channel.setLimit(0);
    }

    /**
     * @see IIrcBot#onRemoveInviteOnly(ircbot.Channel, ircbot.IrcUser)
     */
    @SuppressWarnings("unused")
    public void onRemoveInviteOnly(final Channel channel, final IrcUser user) {
        channel.setInviteOnly(false);
    }

    /**
     * @see IIrcBot#onRemoveTopicProtection(ircbot.Channel, ircbot.IrcUser)
     */
    @SuppressWarnings("unused")
    public void onRemoveTopicProtection(final Channel channel, final IrcUser user) {
        channel.getTopic().setTopicProtection(false);
    }

    /**
     * @see IIrcBot#onServerPing(java.lang.String)
     */
    public void onServerPing(final String response) {
        send(PONG + SPC + response);
    }

    /**
     * @see IIrcBot#onSetChannelKey(ircbot.Channel, ircbot.IrcUser, java.lang.String)
     */
    @SuppressWarnings("unused")
    public void onSetChannelKey(final Channel channel, final IrcUser user, final String key) {
        channel.setKey(key);
    }

    /**
     * @see IIrcBot#onSetChannelLimit(ircbot.Channel, ircbot.IrcUser, int)
     */
    @SuppressWarnings("unused")
    public void onSetChannelLimit(final Channel channel, final IrcUser user, final int limit) {
        channel.setLimit(limit);
    }

    /**
     * @see IIrcBot#onSetInviteOnly(Channel, IrcUser)
     */
    @SuppressWarnings("unused")
    public void onSetInviteOnly(final Channel channel, final IrcUser user) {
        channel.setInviteOnly(true);
    }

    /**
     * @see IIrcBot#onSetTopicProtection(Channel, IrcUser)
     */
    @SuppressWarnings("unused")
    public void onSetTopicProtection(final Channel channel, final IrcUser user) {
        channel.getTopic().setTopicProtection(true);
    }

    /**
     * @see IIrcBot#onTime(IrcUser, String)
     */
    @SuppressWarnings("unused")
    public void onTime(final IrcUser user, final String target) {
        CtcpUtils.sendCtcpNotice(P_TIME + SPC + new Date(), user.getNick(), this.outputThread);
    }

    /**
     * @see IIrcBot#onTopic(String, String, String, long, boolean)
     */
    @SuppressWarnings("unused")
    public void onTopic(final String channel,
            final String topic,
            final String setBy,
            final long date,
            final boolean changed) {
        Channel.getChannel(channel).setTopic(new Topic(topic, date, setBy));
    }

    /**
     * @see IIrcBot#onVersion(IrcUser, String)
     */
    @SuppressWarnings("unused")
    public void onVersion(final IrcUser user, final String target) {
        CtcpUtils.sendCtcpNotice(P_VERSION + SPC + VERSION, user, this.outputThread);
    }

    /**
     * @see IIrcBot#onVoice(Channel, IrcUser, String)
     */
    @SuppressWarnings("unused")
    public void onVoice(final Channel channel, final IrcUser user, final String recipient) {
        channel.giveVoice(recipient);
    }

    /**
     * @see IIrcBot#op(Channel, String)
     */
    public final void op(final Channel channel, final String nick) {
        setMode(channel, PLUS + Mode.OP + SPC + nick);
    }

    /**
     * @see IIrcBot#partChannel(Channel)
     */
    public final void partChannel(final Channel channel) {
        send(IIrcCommands.PART + SPC + channel);
    }

    /**
     * @see IIrcBot#partChannel(Channel, String)
     */
    public final void partChannel(final Channel channel, final String reason) {
        send(IIrcCommands.PART + SPC + channel + SPC + COLON + reason);
    }

    /**
     * @see IIrcBot#processMode(String, IrcUser, String)
     */
    public final void processMode(final String target, final IrcUser user, final String mode) {
        if (Channel.isChannelName(target)) {
            /* The mode of a channel is being changed. */
            final Channel chan = Channel.getChannel(target);
            final StrTokenizer tok = new StrTokenizer(mode);
            char pOrM = SPC;
            /* All of this is very large and ugly, but it's the only way of providing what the users want :-/ */
            final String param0 = tok.nextToken();
            for (short i = 0; i < param0.length(); ++i) {
                final char atPos = param0.charAt(i);
                if (atPos == PLUS || atPos == MINUS) {
                    pOrM = atPos;
                } else if (atPos == Mode.OP) {
                    if (pOrM == PLUS) {
                        onOp(chan, user, tok.nextToken());
                    } else {
                        onDeop(chan, user, tok.nextToken());
                    }
                } else if (atPos == Mode.VOICE) {
                    if (pOrM == PLUS) {
                        onVoice(chan, user, tok.nextToken());
                    } else {
                        onDeVoice(chan, user, tok.nextToken());
                    }
                } else if (atPos == Mode.KEY) {
                    if (pOrM == PLUS) {
                        onSetChannelKey(chan, user, tok.nextToken());
                    } else {
                        onRemoveChannelKey(chan, user, tok.nextToken());
                    }
                } else if (atPos == Mode.LIMIT) {
                    if (pOrM == PLUS) {
                        onSetChannelLimit(chan, user, Integer.parseInt(tok.nextToken()));
                    } else {
                        onRemoveChannelLimit(chan, user);
                    }
                } else if (atPos == Mode.BAN) {
                    if (pOrM == PLUS) {
                        onSetChannelBan(chan, user, tok.nextToken());
                        return;
                    }
                    onRemoveChannelBan(chan, user, tok.nextToken());
                } else if (atPos == Mode.TOPIC_PROTECTED) {
                    if (pOrM == PLUS) {
                        onSetTopicProtection(chan, user);
                        return;
                    }
                    onRemoveTopicProtection(chan, user);
                } else if (atPos == Mode.NO_EXT_MSG) {
                    if (pOrM == PLUS) {
                        onSetNoExternalMessages(chan, user);
                        return;
                    }
                    onRemoveNoExternalMessages(chan, user);
                } else if (atPos == Mode.INVITE_ONLY) {
                    if (pOrM == PLUS) {
                        onSetInviteOnly(chan, user);
                        return;
                    }
                    onRemoveInviteOnly(chan, user);
                } else if (atPos == Mode.MODERATED) {
                    if (pOrM == PLUS) {
                        onSetModerated(chan, user);
                        return;
                    }
                    onRemoveModerated(chan, user);
                } else if (atPos == Mode.PRIVATE) {
                    if (pOrM == PLUS) {
                        onSetPrivate(chan, user);
                        return;
                    }
                    onRemovePrivate(chan, user);
                } else if (atPos == Mode.SECRET) {
                    if (pOrM == PLUS) {
                        onSetSecret(chan, user);
                        return;
                    }
                    onRemoveSecret(chan, user);
                }
            }
            onMode(chan, user, mode);
        } else {
            /* The mode of a user is being changed. */
            onUserMode(target, user, mode);
        }
    }

    /**
     * This method is called by the PircBot when a numeric response is received from the IRC server. We use this method
     * to allow PircBot to process various responses from the server before then passing them on to the onServerResponse
     * method.
     * <p>
     * Note that this method is private and should not appear in any of the javadoc generated documenation.
     * 
     * @param code
     *            The three-digit numerical code for the response.
     * @param response
     *            The full response from the IRC server.
     */
    protected void processServerResponse(final int code, final String response) {
        int firstSpace;
        int secondSpace;
        int thirdSpace;
        String channel;
        int colon;
        switch (code) {
            case RPL_LIST:
                /* This is a bit of information about a channel. */
                firstSpace = response.indexOf(SPC);
                secondSpace = response.indexOf(SPC, firstSpace + 1);
                thirdSpace = response.indexOf(SPC, secondSpace + 1);
                colon = response.indexOf(COLON);
                channel = response.substring(firstSpace + 1, secondSpace);
                final int userCount = Integer.parseInt(response.substring(secondSpace + 1, thirdSpace));
                final String topic = response.substring(colon + 1);
                onChannelInfo(channel, userCount, topic);
                break;
            case RPL_TOPIC:
                /* This is topic information about a channel we've just joined. */
                firstSpace = response.indexOf(SPC);
                secondSpace = response.indexOf(SPC, firstSpace + 1);
                colon = response.indexOf(COLON);
                /* TODO wtf à quoi sert ce substring ? */
                String wtf = response.substring(firstSpace + 1, secondSpace);
                LOG.info("wtf = " + wtf);
                this.tempTopic = response.substring(colon + 1);
                break;
            case RPL_TOPICINFO:
                final StrTokenizer tokenizer = new StrTokenizer(response);
                tokenizer.nextToken();
                channel = tokenizer.nextToken();
                final String setBy = tokenizer.nextToken();
                final long date = Long.parseLong(tokenizer.nextToken()) * 1000;
                onTopic(channel, this.tempTopic, setBy, date, false);
                this.tempTopic = null;
                break;
            case RPL_NAMREPLY:
                /* This is a list of nicks in a channel that we've just joined. */
                LOG.debug("namreply response = " + response);
                final int channelEndIndex = response.indexOf(EMPTY + SPC + COLON);
                LOG.debug("namreply endIndex = " + channelEndIndex);
                channel = response.substring(response.lastIndexOf(SPC, channelEndIndex - 1) + 1, channelEndIndex);
                LOG.debug("namreply wtfIsThis = " + channel);
                final StrBuilder prefix = new StrBuilder();
                for (final String tempNick : new StrTokenizer(substringAfter(response, EMPTY + SPC + COLON))
                        .getTokenArray()) {
                    LOG.debug("namreply tempnick = " + tempNick);
                    prefix.clear();
                    /* TODO foutre un while ici */
                    if (tempNick.charAt(0) == PREFIX_OP || tempNick.charAt(0) == PREFIX_VOICE) {
                        /* User is an operator or voiced in this channel. */
                        prefix.append(tempNick.charAt(0));
                    }
                    LOG.debug("prefix = " + prefix);
                    this.tempUsers.put(tempNick.substring(prefix.length()), prefix.toString());
                }
                break;
            case RPL_ENDOFNAMES:
                /*
                 * This is the end of a NAMES list, so we know that we've got the full list of users in the channel that
                 * we just joined.
                 */
                LOG.debug("endofnames response=" + response);
                channel = response.substring(response.indexOf(SPC) + 1, response.indexOf(EMPTY + SPC + COLON));
                LOG.debug("endofnames channel=" + channel);
                if (channel.indexOf(SPC) > 0) {
                    LOG.error("Reponse erronnee (channel=" + channel + ")");
                } else {
                    onUserList(Channel.getChannel(channel), this.tempUsers.entrySet());
                }
                this.tempUsers.clear();
                break;
            default:
                if (LOG.isInfoEnabled()) {
                    LOG.info(new StrBuilder().append('[').append(code).append(']').append(response));
                }
                onServerResponse(code, response);
                break;
        }
    }

    /**
     * @see IIrcBot#quitServer()
     */
    public final void quitServer() {
        quitServer(EMPTY);
    }

    /**
     * @see IIrcBot#quitServer(java.lang.String)
     */
    public final void quitServer(final String reason) {
        send(QUIT + SPC + COLON + reason);
        this.outputThread.setRunning(false);
    }

    /**
     * @see IIrcBot#reconnect()
     */
    public final void reconnect() {
        if (this.server != null) {
            connect(this.server);
            return;
        }
        LOG.warn("Cannot reconnect to an IRC server because we were never connected to one previously!");
    }

    /**
     * @param line
     *            le message à envoyer
     */
    public final void send(final String line) {
        this.outputThread.put(line);
    }

    /**
     * @see IIrcBot#sendAction(java.lang.String, java.lang.String)
     */
    public final void sendAction(final String target, final String action) {
        CtcpUtils.sendAction(action, target, this.outputThread);
    }

    /**
     * @see IIrcBot#sendInvite(java.lang.String, java.lang.String)
     */
    public final void sendInvite(final String nick1, final String channel) {
        send(INVITE + SPC + nick1 + SPC + COLON + channel);
    }

    /**
     * @param target
     * @param message
     */
    protected final void sendMessage(final String target, final String message) {
        send(PRIVMSG + SPC + target + SPC + COLON + message);
    }

    /**
     * @param user
     *            le user
     * @param target
     *            la cible
     */
    public void sendNotice(final IrcUser user, final String target) {
        sendNotice(user.getNick(), target);
    }

    /**
     * @see IIrcBot#sendNotice(java.lang.String, java.lang.String)
     */
    public final void sendNotice(final String target, final String notice) {
        send(NOTICE + SPC + target + SPC + COLON + notice);
    }

    /**
     * @param connected1
     *            booléen
     */
    public final void setConnected(final boolean connected1) {
        this.connected = connected1;
    }

    /**
     * @see IIrcBot#setFinger(String)
     */
    public final void setFinger(final String finger1) {
        this.finger = finger1;
    }

    /**
     * @see IIrcBot#setLogin(java.lang.String)
     */
    public final void setLogin(final String login) {
        this.myLogin = login;
    }

    /**
     * @see IIrcBot#setMode(Channel, java.lang.String)
     */
    public final void setMode(final Channel channel, final String mode) {
        send(MODE + SPC + channel + SPC + mode);
    }

    /**
     * @see IIrcBot#setMyName(java.lang.String)
     */
    public final void setMyName(final String name) {
        this.myName = name;
    }

    /**
     * @see IIrcBot#setNick(java.lang.String)
     */
    public final void setNick(final String nick1) {
        this.myNick = nick1;
    }

    /**
     * @see IIrcBot#setTopic(java.lang.String, java.lang.String)
     */
    public final void setTopic(final String channel, final String topic) {
        send(TOPIC + SPC + channel + SPC + COLON + topic);
    }

    /**
     * @see IIrcBot#startIdentServer()
     */
    public final void startIdentServer() {
        new IdentServer(this.myLogin).execute();
    }

    /**
     * @see IIrcBot#unBan(Channel, java.lang.String)
     */
    public final void unBan(final Channel channel, final String hostmask) {
        send(MODE + SPC + channel + SPC + MINUS + Mode.BAN + hostmask);
    }

    /**
     * @see IIrcBot#voice(Channel, java.lang.String)
     */
    public final void voice(final Channel channel, final String nick1) {
        setMode(channel, PLUS + Mode.VOICE + nick1);
    }
}

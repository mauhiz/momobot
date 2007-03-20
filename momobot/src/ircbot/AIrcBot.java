package ircbot;

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
import java.util.Iterator;
import java.util.Map;
import java.util.StringTokenizer;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.TimeUnit;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.text.StrTokenizer;
import com.sun.org.apache.bcel.internal.generic.GETSTATIC;
import sun.security.action.GetLongAction;
import utils.NetUtils;
import utils.Utils;

/**
 * Tout ce qui reste du PircBot original, c'est le nom et une paire de méthodes.
 * Owned.
 * @author viper
 */
public abstract class AIrcBot implements IIrcConstants, IIrcCommands,
        IIrcSpecialChars, ICtcpCommands, IDccSubCommands {
    /**
     * le nom.
     */
    private static final String                    DEFAULT_NAME = "PircBot";
    /**
     * Les ports pour le DCC. Osef complètement, il est pas prévu que le bot
     * fasse des xdcc pour l'instant
     */
    public static final int[]                      PORTS        = {
            1025, 1026, 1027
                                                                };
    /**
     * Me dit si je suis connecté.
     */
    private boolean                                connected    = false;
    /**
     * Un manager to rule them all.
     */
    private final DccManager                       dccManager   = new DccManager(
                                                                        this);
    /**
     * Mon doigt.
     */
    private String                                 finger       = "You ought to be arrested for fingering a bot!";
    /**
     * le login.
     */
    private String                                 myLogin      = "PircBot";
    /**
     * Mon nom, celui que je veux avoir comme nick.
     */
    private String                                 myName       = DEFAULT_NAME;
    /**
     * le nick que le bot a conscience d'avoir. Il peut parfois différer de son
     * nick réel en cas de désynchro.
     */
    private String                                 myNick       = EMPTY;
    /**
     * Mon thread qui me débarasse de ce que je dois dire.
     */
    private OutputQueue                            outputThread = null;
    /**
     * Mon serveur IRC où je vais me connecter.
     */
    private IrcServerBean                          server       = null;
    /**
     * une poubelle avec un topic temporaire dedans. Il faudrait que je trouve
     * plus élégant.
     */
    private String                                 tempTopic    = EMPTY;
    /**
     * une poubelle avec plein d'users.
     */
    private final ConcurrentMap < String, String > tempUsers    = new ConcurrentHashMap < String, String >();
    /**
     * ce que je réponds sur un CTCP version.
     */
    private final String                           version      = "momoBot";

    /**
     * Bans a user from a channel. An example of a valid hostmask is
     * "*!*compu@*.18hp.net". This may be used in conjunction with the kick
     * method to permanently remove a user from a channel. Successful use of
     * this method may require the bot to have operator status itself.
     * @param channel
     *            The channel to ban the user from.
     * @param hostmask
     *            A hostmask representing the user we're banning.
     */
    public final void ban(final String channel, final String hostmask) {
        send(MODE + SPC + channel + SPC + PLUS + Mode.BAN + SPC + hostmask);
    }

    /**
     * Attempt to change the current nick (nickname) of the bot when it is
     * connected to an IRC server. After confirmation of a successful nick
     * change, the getNick method will return the new nick.
     * @param newNick
     *            The new nick to use.
     */
    public final void changeNick(final String newNick) {
        send(NICK + SPC + newNick);
    }

    /**
     * Attempt to connect to the specified IRC server using the supplied
     * password. The onConnect method is called upon success.
     * @param server1
     *            le serveur
     */
    public final void connect(final IrcServerBean server1) {
        this.server = server1;
        final int timeout = (int) TimeUnit.MILLISECONDS.convert(5,
                TimeUnit.MINUTES);
        try {
            final Socket socket = new Socket();
            socket.connect(this.server.getAddress());
            socket.setSoTimeout(timeout);
            this.outputThread = new OutputQueue(socket);
            new InputProcessor(this, socket).execute();
            this.outputThread.execute();
            // Attempt to join the server.
            if (this.server.getPassword().length() > 0) {
                send(PASS + SPC + this.server.getPassword());
            }
            send(NICK + SPC + getMyName());
            send(USER + SPC + this.myLogin.toLowerCase() + SPC + '8' + SPC
                    + '*' + SPC + COLON + this.version);
            this.myNick = getMyName();
        } catch (final Exception e) {
            Utils.logError(getClass(), e);
        }
    }

    /**
     * Attempts to establish a DCC CHAT session with a client. This method
     * issues the connection request to the client and then waits for the client
     * to respond. If the connection is successfully made, then a DccChat object
     * is returned by this method. If the connection is not made within the time
     * limit specified by the timeout value, then null is returned.
     * <p>
     * It is <b>strongly recommended</b> that you call this method within a new
     * Thread, as it may take a long time to return.
     * <p>
     * This method may not be overridden.
     * @since PircBot 0.9.8
     * @param user
     *            The nick of the user we are trying to establish a chat with.
     * @param timeout
     *            The number of milliseconds to wait for the recipient to accept
     *            the chat connection (we recommend about 120000).
     * @return a DccChat object that can be used to send and recieve lines of
     *         text. Returns <b>null</b> if the connection could not be made.
     * @see DccChat
     */
    public final DccChat dccSendChatRequest(final IrcUser user,
            final int timeout) {
        DccChat chat = null;
        ServerSocket ss = null;
        try {
            if (PORTS == null) {
                // Use any free port.
                ss = new ServerSocket(0);
            } else {
                for (final int element : PORTS) {
                    try {
                        ss = new ServerSocket(element);
                        // Found a port number we could use.
                        break;
                    } catch (final IOException e) {
                        continue;
                    }
                }
                if (ss == null) {
                    // No ports could be used.
                    throw new IOException(
                            "All ports returned by getDccPorts() are in use.");
                }
            }
            ss.setSoTimeout(timeout);
            final int port = ss.getLocalPort();
            // TODO : autoriser le réglage externe dans le cas d'un NAT.
            ACtcp.sendCtcpMsg(DCC_CHAT
                    + SPC
                    + "chat"
                    + SPC
                    + NetUtils.byteTabIpToLong(InetAddress.getLocalHost()
                            .getAddress()) + SPC + port, user.getNick(),
                    this.outputThread);
            // The client may now connect to us to chat.
            final Socket socket = ss.accept();
            // Close the server socket now that we've finished with it.
            ss.close();
            chat = new DccChat(user, socket);
        } catch (final IOException e) {
            Utils.logError(getClass(), e);
        }
        return chat;
    }

    /**
     * Sends a file to another user. Resuming is supported. The other user must
     * be able to connect directly to your bot to be able to receive the file.
     * <p>
     * You may throttle the speed of this file transfer by calling the
     * setPacketDelay method on the DccFileTransfer that is returned.
     * <p>
     * This method may not be overridden.
     * @since 0.9c
     * @param file
     *            The file to send.
     * @param user
     *            The user to whom the file is to be sent.
     * @param timeout
     *            The number of milliseconds to wait for the recipient to
     *            acccept the file (we recommend about 120000).
     * @return The DccFileTransfer that can be used to monitor this transfer.
     * @see DccFileTransfer
     */
    public final DccFileTransfer dccSendFile(final File file,
            final IrcUser user, final int timeout) {
        final DccFileTransfer transfer = new DccFileTransfer(this, file, user,
                timeout);
        transfer.doSend(true);
        return transfer;
    }

    /**
     * Removes operator privilidges from a user on a channel. Successful use of
     * this method may require the bot to have operator status itself.
     * @param channel
     *            The channel we're deopping the user on.
     * @param nick
     *            The nick of the user we are deopping.
     */
    public final void deOp(final Channel channel, final String nick) {
        setMode(channel.getNom(), MINUS + Mode.OP + SPC + nick);
    }

    /**
     * Removes voice privilidges from a user on a channel. Successful use of
     * this method may require the bot to have operator status itself.
     * @param channel
     *            The channel we're devoicing the user on.
     * @param nick
     *            The nick of the user we are devoicing.
     */
    public final void deVoice(final Channel channel, final String nick) {
        setMode(channel.getNom(), MINUS + Mode.VOICE + SPC + nick);
    }

    /**
     * @return le dccManager
     */
    public final DccManager getDccManager() {
        return this.dccManager;
    }

    /**
     * @return le myName
     */
    public final String getMyName() {
        return this.myName;
    }

    /**
     * Returns the current nick of the bot. Note that if you have just changed
     * your nick, this method will still return the old nick until confirmation
     * of the nick change is received from the server.
     * <p>
     * The nick returned by this method is maintained only by the PircBot class
     * and is guaranteed to be correct in the context of the IRC server.
     * @since PircBot 1.0.0
     * @return The current nick of the bot.
     */
    public final String getNick() {
        return this.myNick;
    }

    /**
     * @return un outputThread
     */
    public final OutputQueue getOutputThread() {
        return this.outputThread;
    }

    /**
     * Gets the internal version of the PircBot.
     * @return The version of the PircBot.
     */
    public final String getVersion() {
        return this.version;
    }

    /**
     * Returns whether or not the PircBot is currently connected to a server.
     * The result of this method should only act as a rough guide, as the result
     * may not be valid by the time you act upon it.
     * @return True if and only if the PircBot is currently connected to a
     *         server.
     */
    public final boolean isConnected() {
        return this.connected;
    }

    /**
     * Joins a channel.
     * @param channel
     *            The name of the channel to join (eg "#cs").
     */
    public final void joinChannel(final String channel) {
        send(JOIN + SPC + channel);
    }

    /**
     * Joins a channel with a key.
     * @param channel
     *            The name of the channel to join (eg "#cs").
     * @param key
     *            The key that will be used to join the channel.
     */
    public final void joinChannel(final String channel, final String key) {
        joinChannel(channel + SPC + key);
    }

    /**
     * Kicks a user from a channel. This method attempts to kick a user from a
     * channel and may require the bot to have operator status in the channel.
     * @param channel
     *            The channel to kick the user from.
     * @param tempNick
     *            The nick of the user to kick.
     */
    public final void kick(final String channel, final String tempNick) {
        kick(channel, tempNick, EMPTY);
    }

    /**
     * Kicks a user from a channel, giving a reason. This method attempts to
     * kick a user from a channel and may require the bot to have operator
     * status in the channel.
     * @param channel
     *            The channel to kick the user from.
     * @param nick
     *            The nick of the user to kick.
     * @param reason
     *            A description of the reason for kicking a user.
     */
    public final void kick(final String channel, final String nick,
            final String reason) {
        send(KICK + SPC + channel + SPC + nick + SPC + COLON + reason);
    }

    /**
     * Issues a request for a list of all channels on the IRC server. When the
     * PircBot receives information for each channel, it will call the
     * onChannelInfo method, which you will need to override if you want it to
     * do anything useful.
     * @see #onChannelInfo(String,int,String) onChannelInfo
     */
    public final void listChannels() {
        send("LIST");
    }

    /**
     * Issues a request for a list of all channels on the IRC server. When the
     * PircBot receives information for each channel, it will call the
     * onChannelInfo method, which you will need to override if you want it to
     * do anything useful.
     * <p>
     * Some IRC servers support certain parameters for LIST requests. One
     * example is a parameter of ">10" to list only those channels that have
     * more than 10 users in them. Whether these parameters are supported or not
     * will depend on the IRC server software.
     * @param parameters
     *            The parameters to supply when requesting the list.
     * @see #onChannelInfo(String,int,String) onChannelInfo
     */
    public final void listChannels(final String parameters) {
        send("LIST" + SPC + parameters);
    }

    /**
     * This method is called whenever an ACTION is sent from a user. E.g. such
     * events generated by typing "/me goes shopping" in most IRC clients.
     * <p>
     * The implementation of this method in the PircBot abstract class performs
     * no actions and may be overridden as required.
     * @param sender
     *            The nick of the user that sent the action.
     * @param target
     *            The target of the action, be it a channel or our nick.
     * @param action
     *            The action carried out by the user.
     */
    protected abstract void onAction(IrcUser sender, String target,
            String action);

    /**
     * After calling the listChannels() method in PircBot, the server will start
     * to send us information about each channel on the server. You may override
     * this method in order to receive the information about each channel as
     * soon as it is received.
     * <p>
     * Note that certain channels, such as those marked as hidden, may not
     * appear in channel listings.
     * <p>
     * The implementation of this method in the PircBot abstract class performs
     * no actions and may be overridden as required.
     * @param channel
     *            The name of the channel.
     * @param userCount
     *            The number of users visible in this channel.
     * @param topic
     *            The topic for this channel.
     * @see #listChannels() listChannels
     */
    protected abstract void onChannelInfo(String channel, int userCount,
            String topic);

    /**
     * This method is called once the PircBot has successfully connected to the
     * IRC server.
     * <p>
     * The implementation of this method in the PircBot abstract class performs
     * no actions and may be overridden as required.
     * @since PircBot 0.9.6
     */
    protected void onConnect() {
        setConnected(true);
    }

    /**
     * Called when a user (possibly us) gets operator status taken away.
     * <p>
     * This is a type of mode change and is also passed to the onMode method in
     * the PircBot class.
     * <p>
     * The implementation of this method in the PircBot abstract class performs
     * no actions and may be overridden as required.
     * @since PircBot 0.9.5
     * @param channel
     *            The channel in which the mode change took place.
     * @param user
     *            le user
     * @param recipient
     *            The nick of the user that got 'deopped'.
     */
    protected void onDeop(Channel channel, IrcUser user, String recipient) {
        channel.removeOp(recipient);
    }

    /**
     * Called when a user (possibly us) gets voice status removed.
     * <p>
     * This is a type of mode change and is also passed to the onMode method in
     * the PircBot class.
     * <p>
     * The implementation of this method in the PircBot abstract class performs
     * no actions and may be overridden as required.
     * @since PircBot 0.9.5
     * @param channel
     *            The channel in which the mode change took place.
     * @param user
     *            le user
     * @param recipient
     *            The nick of the user that got 'devoiced'.
     */
    protected void onDeVoice(Channel channel, IrcUser user, String recipient) {
        channel.removeVoice(recipient);
    }

    /**
     * This method carries out the actions to be performed when the PircBot gets
     * disconnected. This may happen if the PircBot quits from the server, or if
     * the connection is unexpectedly lost.
     * <p>
     * Disconnection from the IRC server is detected immediately if either we or
     * the server close the connection normally. If the connection to the server
     * is lost, but neither we nor the server have explicitly closed the
     * connection, then it may take a few minutes to detect (this is commonly
     * referred to as a "ping timeout").
     * <p>
     * If you wish to get your IRC bot to automatically rejoin a server after
     * the connection has been lost, then this is probably the ideal method to
     * override to implement such functionality.
     * <p>
     * The implementation of this method in the PircBot abstract class performs
     * no actions and may be overridden as required.
     */
    protected void onDisconnect() {
        this.outputThread.setRunning(false);
        Channel.removeAll();
    }

    /**
     * This method gets called when a DccFileTransfer has finished. If there was
     * a problem, the Exception will say what went wrong. If the file was sent
     * successfully, the Exception will be null.
     * <p>
     * Both incoming and outgoing file transfers are passed to this method. You
     * can determine the type by calling the isIncoming or isOutgoing methods on
     * the DccFileTransfer object.
     * @since PircBot 1.2.0
     * @param transfer
     *            The DccFileTransfer that has finished.
     * @param e
     *            null if the file was transfered successfully, otherwise this
     *            will report what went wrong.
     * @see DccFileTransfer
     */
    public abstract void onFileTransferFinished(DccFileTransfer transfer,
            Exception e);

    /**
     * This method is called whenever we receive a FINGER request.
     * <p>
     * This abstract implementation responds correctly, so if you override this
     * method, be sure to either mimic its functionality or to call
     * super.onFinger(...);
     * @param user
     *            le user
     * @param target
     *            The target of the FINGER request, be it our nick or a channel
     *            name.
     */
    protected void onFinger(final IrcUser user, final String target) {
        ACtcp.sendCtcpNotice(P_FINGER + SPC + this.finger, user.getNick(),
                this.outputThread);
    }

    /**
     * This method will be called whenever a DCC Chat request is received. This
     * means that a client has requested to chat to us directly rather than via
     * the IRC server. This is useful for sending many lines of text to and from
     * the bot without having to worry about flooding the server or any
     * operators of the server being able to "spy" on what is being said. This
     * abstract implementation performs no action, which means that all DCC CHAT
     * requests will be ignored by default.
     * <p>
     * If you wish to accept the connection, then you may override this method
     * and call the accept() method on the DccChat object, which connects to the
     * sender of the chat request and allows lines to be sent to and from the
     * bot.
     * <p>
     * Your bot must be able to connect directly to the user that sent the
     * request.
     * <p>
     * Each time this method is called, it is called from within a new Thread so
     * that multiple DCC CHAT sessions can run concurrently.
     * <p>
     * The implementation of this method in the PircBot abstract class performs
     * no actions and may be overridden as required.
     * @since PircBot 1.2.0
     * @param chat
     *            A DccChat object that represents the incoming chat request.
     * @see DccChat
     */
    public abstract void onIncomingChatRequest(DccChat chat);

    /**
     * This method is called whenever a DCC SEND request is sent to the PircBot.
     * This means that a client has requested to send a file to us. This
     * abstract implementation performs no action, which means that all DCC SEND
     * requests will be ignored by default. If you wish to receive the file,
     * then you may override this method and call the receive method on the
     * DccFileTransfer object, which connects to the sender and downloads the
     * file.
     * <p>
     * <b>Warning:</b> Receiving an incoming file transfer will cause a file to
     * be written to disk. Please ensure that you make adequate security checks
     * so that this file does not overwrite anything important!
     * <p>
     * Each time a file is received, it happens within a new Thread in order to
     * allow multiple files to be downloaded by the PircBot at the same time.
     * <p>
     * If you allow resuming and the file already partly exists, it will be
     * appended to instead of overwritten. If resuming is not enabled, the file
     * will be overwritten if it already exists.
     * <p>
     * You can throttle the speed of the transfer by calling the setPacketDelay
     * method on the DccFileTransfer object, either before you receive the file
     * or at any moment during the transfer.
     * <p>
     * The implementation of this method in the PircBot abstract class performs
     * no actions and may be overridden as required.
     * @since PircBot 1.2.0
     * @param transfer
     *            The DcccFileTransfer that you may accept.
     * @see DccFileTransfer
     */
    public abstract void onIncomingFileTransfer(DccFileTransfer transfer);

    /**
     * Called when we are invited to a channel by a user.
     * <p>
     * The implementation of this method in the PircBot abstract class performs
     * no actions and may be overridden as required.
     * @since PircBot 0.9.5
     * @param targetNick
     *            The nick of the user being invited - should be us!
     * @param user
     *            le user
     * @param channel
     *            The channel that we're being invited to.
     */
    protected abstract void onInvite(String targetNick, IrcUser user,
            String channel);

    /**
     * This method is called whenever someone (possibly us) joins a channel
     * which we are on.
     * <p>
     * The implementation of this method in the PircBot abstract class performs
     * no actions and may be overridden as required.
     * @param channel
     *            The channel which somebody joined.
     * @param user
     *            le user
     */
    protected void onJoin(Channel channel, IrcUser user) {
        channel.addUser(user);
    }

    /**
     * This method is called whenever someone (possibly us) is kicked from any
     * of the channels that we are in.
     * <p>
     * The implementation of this method in the PircBot abstract class performs
     * no actions and may be overridden as required.
     * @param channel
     *            The channel from which the recipient was kicked.
     * @param user
     *            le user
     * @param recipientNick
     *            The unfortunate recipient of the kick.
     * @param reason
     *            The reason given by the user who performed the kick.
     */
    protected void onKick(String channel, IrcUser user, String recipientNick,
            String reason) {
        if (recipientNick.equalsIgnoreCase(getNick())) {
            Channel.removeChannel(channel);
            return;
        }
        Channel.getChannel(channel).removeUser(recipientNick);
    }

    /**
     * This method is called whenever a message is sent to a channel.
     * <p>
     * The implementation of this method in the PircBot abstract class performs
     * no actions and may be overridden as required.
     * @param channel
     *            The channel to which the message was sent.
     * @param user
     *            le user
     * @param message
     *            The actual message sent to the channel.
     */
    protected abstract void onMessage(Channel channel, IrcUser user,
            String message);

    /**
     * Called when the mode of a channel is set.
     * <p>
     * You may find it more convenient to decode the meaning of the mode string
     * by overriding the onOp, onDeOp, onVoice, onDeVoice, onChannelKey,
     * onDeChannelKey, onChannelLimit, onDeChannelLimit, onChannelBan or
     * onDeChannelBan methods as appropriate.
     * <p>
     * The implementation of this method in the PircBot abstract class performs
     * no actions and may be overridden as required.
     * @param channel
     *            The channel that the mode operation applies to.
     * @param user
     *            le user
     * @param mode
     *            The mode that has been set.
     */
    protected abstract void onMode(String channel, IrcUser user, String mode);

    /**
     * This method is called whenever someone (possibly us) changes nick on any
     * of the channels that we are on.
     * <p>
     * The implementation of this method in the PircBot abstract class performs
     * no actions and may be overridden as required.
     * @param user
     *            le user
     * @param newNick
     *            The new nick.
     */
    protected abstract void onNickChange(IrcUser user, String newNick);

    /**
     * This method is called whenever we receive a notice.
     * <p>
     * The implementation of this method in the PircBot abstract class performs
     * no actions and may be overridden as required.
     * @param user
     *            le user
     * @param target
     *            The target of the notice, be it our nick or a channel name.
     * @param notice
     *            The notice message.
     */
    protected abstract void onNotice(IrcUser user, String target, String notice);

    /**
     * Called when a user (possibly us) gets granted operator status for a
     * channel.
     * <p>
     * This is a type of mode change and is also passed to the onMode method in
     * the PircBot class.
     * <p>
     * The implementation of this method in the PircBot abstract class performs
     * no actions and may be overridden as required.
     * @since PircBot 0.9.5
     * @param channel
     *            The channel in which the mode change took place.
     * @param user
     *            le user
     * @param recipient
     *            The nick of the user that got 'opped'.
     */
    protected abstract void onOp(Channel channel, IrcUser user, String recipient);

    /**
     * This method is called whenever someone (possibly us) parts a channel
     * which we are on.
     * <p>
     * The implementation of this method in the PircBot abstract class performs
     * no actions and may be overridden as required.
     * @param channel
     *            The channel which somebody parted from.
     * @param user
     *            le user
     */
    protected abstract void onPart(Channel channel, IrcUser user);

    /**
     * This method is called whenever we receive a PING request from another
     * user.
     * <p>
     * This abstract implementation responds correctly, so if you override this
     * method, be sure to either mimic its functionality or to call
     * super.onPing(...);
     * @param user
     *            le user
     * @param target
     *            The target of the PING request, be it our nick or a channel
     *            name.
     * @param pingValue
     *            The value that was supplied as an argument to the PING
     *            command.
     */
    protected void onPing(final IrcUser user, final String target,
            final String pingValue) {
        ACtcp.sendCtcpNotice(user.getNick(), P_PING + SPC + pingValue,
                this.outputThread);
    }

    /**
     * This method is called whenever a private message is sent to the PircBot.
     * <p>
     * The implementation of this method in the PircBot abstract class performs
     * no actions and may be overridden as required.
     * @param user
     *            le user
     * @param message
     *            The actual message.
     */
    protected abstract void onPrivateMessage(IrcUser user, String message);

    /**
     * This method is called whenever someone (possibly us) quits from the
     * server. We will only observe this if the user was in one of the channels
     * to which we are connected.
     * <p>
     * The implementation of this method in the PircBot abstract class performs
     * no actions and may be overridden as required.
     * @param user
     *            le user
     * @param reason
     *            The reason given for quitting the server.
     */
    protected abstract void onQuit(IrcUser user, String reason);

    /**
     * Called when a hostmask ban is removed from a channel.
     * <p>
     * This is a type of mode change and is also passed to the onMode method in
     * the PircBot class.
     * <p>
     * The implementation of this method in the PircBot abstract class performs
     * no actions and may be overridden as required.
     * @since PircBot 0.9.5
     * @param channel
     *            The channel in which the mode change took place.
     * @param user
     *            le user
     * @param hostmask
     *            le masque de ban
     */
    protected abstract void onRemoveChannelBan(Channel channel, IrcUser user,
            String hostmask);

    /**
     * Called when a channel key is removed.
     * <p>
     * This is a type of mode change and is also passed to the onMode method in
     * the PircBot class.
     * <p>
     * The implementation of this method in the PircBot abstract class performs
     * no actions and may be overridden as required.
     * @since PircBot 0.9.5
     * @param channel
     *            The channel in which the mode change took place.
     * @param user
     *            le user
     * @param key
     *            The key that was in use before the channel key was removed.
     */
    protected abstract void onRemoveChannelKey(Channel channel, IrcUser user,
            String key);

    /**
     * Called when the user limit is removed for a channel.
     * <p>
     * This is a type of mode change and is also passed to the onMode method in
     * the PircBot class.
     * <p>
     * The implementation of this method in the PircBot abstract class performs
     * no actions and may be overridden as required.
     * @since PircBot 0.9.5
     * @param channel
     *            The channel in which the mode change took place.
     * @param user
     *            le user
     */
    protected abstract void onRemoveChannelLimit(Channel channel, IrcUser user);

    /**
     * Called when a channel has 'invite only' removed.
     * <p>
     * This is a type of mode change and is also passed to the onMode method in
     * the PircBot class.
     * <p>
     * The implementation of this method in the PircBot abstract class performs
     * no actions and may be overridden as required.
     * @since PircBot 0.9.5
     * @param channel
     *            The channel in which the mode change took place.
     * @param user
     *            le user
     */
    protected abstract void onRemoveInviteOnly(Channel channel, IrcUser user);

    /**
     * Called when a channel has moderated mode removed.
     * <p>
     * This is a type of mode change and is also passed to the onMode method in
     * the PircBot class.
     * <p>
     * The implementation of this method in the PircBot abstract class performs
     * no actions and may be overridden as required.
     * @since PircBot 0.9.5
     * @param channel
     *            The channel in which the mode change took place.
     * @param user
     *            le user
     */
    protected abstract void onRemoveModerated(Channel channel, IrcUser user);

    /**
     * Called when a channel is set to allow messages from any user, even if
     * they are not actually in the channel.
     * <p>
     * This is a type of mode change and is also passed to the onMode method in
     * the PircBot class.
     * <p>
     * The implementation of this method in the PircBot abstract class performs
     * no actions and may be overridden as required.
     * @since PircBot 0.9.5
     * @param channel
     *            The channel in which the mode change took place.
     * @param user
     *            le user
     */
    protected abstract void onRemoveNoExternalMessages(Channel channel,
            IrcUser user);

    /**
     * Called when a channel is marked as not being in private mode.
     * <p>
     * This is a type of mode change and is also passed to the onMode method in
     * the PircBot class.
     * <p>
     * The implementation of this method in the PircBot abstract class performs
     * no actions and may be overridden as required.
     * @since PircBot 0.9.5
     * @param channel
     *            The channel in which the mode change took place.
     * @param user
     *            le user
     */
    protected abstract void onRemovePrivate(Channel channel, IrcUser user);

    /**
     * Called when a channel has 'secret' mode removed.
     * <p>
     * This is a type of mode change and is also passed to the onMode method in
     * the PircBot class.
     * <p>
     * The implementation of this method in the PircBot abstract class performs
     * no actions and may be overridden as required.
     * @since PircBot 0.9.5
     * @param channel
     *            The channel in which the mode change took place.
     * @param user
     *            le user
     */
    protected abstract void onRemoveSecret(Channel channel, IrcUser user);

    /**
     * Called when topic protection is removed for a channel.
     * <p>
     * This is a type of mode change and is also passed to the onMode method in
     * the PircBot class.
     * <p>
     * The implementation of this method in the PircBot abstract class performs
     * no actions and may be overridden as required.
     * @since PircBot 0.9.5
     * @param channel
     *            The channel in which the mode change took place.
     * @param user
     *            le user
     */
    protected abstract void onRemoveTopicProtection(Channel channel,
            IrcUser user);

    /**
     * The actions to perform when a PING request comes from the server.
     * <p>
     * This sends back a correct response, so if you override this method, be
     * sure to either mimic its functionality or to call
     * super.onServerPing(response);
     * @param response
     *            The response that should be given back in your PONG.
     */
    protected void onServerPing(final String response) {
        send(PONG + SPC + response);
    }

    /**
     * This method is called when we receive a numeric response from the IRC
     * server.
     * <p>
     * Numerics in the range from 001 to 099 are used for client-server
     * connections only and should never travel between servers. Replies
     * generated in response to commands are found in the range from 200 to 399.
     * Error replies are found in the range from 400 to 599.
     * <p>
     * For example, we can use this method to discover the topic of a channel
     * when we join it. If we join the channel #test which has a topic of 'I am
     * King of Test' then the response will be '<code>PircBot #test :I Am King of Test</code>'
     * with a code of 332 to signify that this is a topic. (This is just an
     * example - note that overriding the <code>onTopic</code> method is an
     * easier way of finding the topic for a channel). Check the IRC RFC for the
     * full list of other command response codes.
     * <p>
     * PircBot implements the interface ReplyConstants, which contains
     * contstants that you may find useful here.
     * <p>
     * The implementation of this method in the PircBot abstract class performs
     * no actions and may be overridden as required.
     * @param code
     *            The three-digit numerical code for the response.
     * @param response
     *            The full response from the IRC server.
     * @see IIrcConstants
     */
    protected abstract void onServerResponse(final int code,
            final String response);

    /**
     * Called when a user (possibly us) gets banned from a channel. Being banned
     * from a channel prevents any user with a matching hostmask from joining
     * the channel. For this reason, most bans are usually directly followed by
     * the user being kicked :-)
     * <p>
     * This is a type of mode change and is also passed to the onMode method in
     * the PircBot class.
     * <p>
     * The implementation of this method in the PircBot abstract class performs
     * no actions and may be overridden as required.
     * @since PircBot 0.9.5
     * @param channel
     *            The channel in which the mode change took place.
     * @param user
     *            le user
     * @param hostmask
     *            The hostmask of the user that has been banned.
     */
    protected abstract void onSetChannelBan(Channel channel, IrcUser user,
            String hostmask);

    /**
     * Called when a channel key is set. When the channel key has been set,
     * other users may only join that channel if they know the key. Channel keys
     * are sometimes referred to as passwords.
     * <p>
     * This is a type of mode change and is also passed to the onMode method in
     * the PircBot class.
     * <p>
     * The implementation of this method in the PircBot abstract class performs
     * no actions and may be overridden as required.
     * @since PircBot 0.9.5
     * @param channel
     *            The channel in which the mode change took place.
     * @param user
     *            le user
     * @param key
     *            The new key for the channel.
     */
    protected abstract void onSetChannelKey(Channel channel, IrcUser user,
            String key);

    /**
     * Called when a user limit is set for a channel. The number of users in the
     * channel cannot exceed this limit.
     * <p>
     * This is a type of mode change and is also passed to the onMode method in
     * the PircBot class.
     * <p>
     * The implementation of this method in the PircBot abstract class performs
     * no actions and may be overridden as required.
     * @since PircBot 0.9.5
     * @param channel
     *            The channel in which the mode change took place.
     * @param user
     *            le user
     * @param limit
     *            The maximum number of users that may be in this channel at the
     *            same time.
     */
    protected abstract void onSetChannelLimit(Channel channel, IrcUser user,
            int limit);

    /**
     * Called when a channel is set to 'invite only' mode. A user may only join
     * the channel if they are invited by someone who is already in the channel.
     * <p>
     * This is a type of mode change and is also passed to the onMode method in
     * the PircBot class.
     * <p>
     * The implementation of this method in the PircBot abstract class performs
     * no actions and may be overridden as required.
     * @since PircBot 0.9.5
     * @param channel
     *            The channel in which the mode change took place.
     * @param user
     *            le user
     */
    protected abstract void onSetInviteOnly(Channel channel, IrcUser user);

    /**
     * Called when a channel is set to 'moderated' mode. If a channel is
     * moderated, then only users who have been 'voiced' or 'opped' may speak or
     * change their nicks.
     * <p>
     * This is a type of mode change and is also passed to the onMode method in
     * the PircBot class.
     * <p>
     * The implementation of this method in the PircBot abstract class performs
     * no actions and may be overridden as required.
     * @since PircBot 0.9.5
     * @param channel
     *            The channel in which the mode change took place.
     * @param user
     *            le user
     */
    protected abstract void onSetModerated(Channel channel, IrcUser user);

    /**
     * Called when a channel is set to only allow messages from users that are
     * in the channel.
     * <p>
     * This is a type of mode change and is also passed to the onMode method in
     * the PircBot class.
     * <p>
     * The implementation of this method in the PircBot abstract class performs
     * no actions and may be overridden as required.
     * @since PircBot 0.9.5
     * @param channel
     *            The channel in which the mode change took place.
     * @param user
     *            le user
     */
    protected abstract void onSetNoExternalMessages(Channel channel,
            IrcUser user);

    /**
     * Called when a channel is marked as being in private mode.
     * <p>
     * This is a type of mode change and is also passed to the onMode method in
     * the PircBot class.
     * <p>
     * The implementation of this method in the PircBot abstract class performs
     * no actions and may be overridden as required.
     * @since PircBot 0.9.5
     * @param channel
     *            The channel in which the mode change took place.
     * @param user
     *            le user
     */
    protected abstract void onSetPrivate(Channel channel, IrcUser user);

    /**
     * Called when a channel is set to be in 'secret' mode. Such channels
     * typically do not appear on a server's channel listing.
     * <p>
     * This is a type of mode change and is also passed to the onMode method in
     * the PircBot class.
     * <p>
     * The implementation of this method in the PircBot abstract class performs
     * no actions and may be overridden as required.
     * @since PircBot 0.9.5
     * @param channel
     *            The channel in which the mode change took place.
     * @param user
     *            le user
     */
    protected abstract void onSetSecret(Channel channel, IrcUser user);

    /**
     * Called when topic protection is enabled for a channel. Topic protection
     * means that only operators in a channel may change the topic.
     * <p>
     * This is a type of mode change and is also passed to the onMode method in
     * the PircBot class.
     * <p>
     * The implementation of this method in the PircBot abstract class performs
     * no actions and may be overridden as required.
     * @since PircBot 0.9.5
     * @param channel
     *            The channel in which the mode change took place.
     * @param user
     *            le user
     */
    protected abstract void onSetTopicProtection(Channel channel, IrcUser user);

    /**
     * This method is called whenever we receive a TIME request.
     * <p>
     * This abstract implementation responds correctly, so if you override this
     * method, be sure to either mimic its functionality or to call
     * super.onTime(...);
     * @param target
     *            The target of the TIME request, be it our nick or a channel
     *            name.
     * @param user
     *            le user
     */
    protected void onTime(final IrcUser user, final String target) {
        ACtcp.sendCtcpNotice(P_TIME + SPC + new Date(), user.getNick(),
                this.outputThread);
    }

    /**
     * This method is called whenever a user sets the topic, or when PircBot
     * joins a new channel and discovers its topic.
     * <p>
     * The implementation of this method in the PircBot abstract class performs
     * no actions and may be overridden as required.
     * @param channel
     *            The channel that the topic belongs to.
     * @param topic
     *            The topic for the channel.
     * @param setBy
     *            The nick of the user that set the topic.
     * @param date
     *            When the topic was set (milliseconds since the epoch).
     * @param changed
     *            True if the topic has just been changed, false if the topic
     *            was already there.
     */
    protected abstract void onTopic(String channel, String topic, String setBy,
            long date, boolean changed);

    /**
     * This method is called whenever we receive a line from the server that the
     * PircBot has not been programmed to recognise.
     * <p>
     * The implementation of this method in the PircBot abstract class performs
     * no actions and may be overridden as required.
     * @param line
     *            The raw line that was received from the server.
     */
    protected abstract void onUnknown(String line);

    /**
     * This method is called when we receive a user list from the server after
     * joining a channel.
     * <p>
     * Shortly after joining a channel, the IRC server sends a list of all users
     * in that channel. The PircBot collects this information and calls this
     * method as soon as it has the full list.
     * <p>
     * To obtain the nick of each user in the channel, call the getNick() method
     * on each User object in the array.
     * <p>
     * At a later time, you may call the getUsers method to obtain an up to date
     * list of the users in the channel.
     * <p>
     * The implementation of this method in the PircBot abstract class performs
     * no actions and may be overridden as required.
     * @since PircBot 1.0.0
     * @param channel
     *            The name of the channel.
     * @param users
     *            An array of User objects belonging to this channel.
     */
    protected abstract void onUserList(String channel,
            Iterator < Map.Entry < String, String > > users);

    /**
     * Called when the mode of a user is set.
     * <p>
     * The implementation of this method in the PircBot abstract class performs
     * no actions and may be overridden as required.
     * @since PircBot 1.2.0
     * @param targetNick
     *            The nick that the mode operation applies to.
     * @param user
     *            le user
     * @param mode
     *            The mode that has been set.
     */
    protected abstract void onUserMode(String targetNick, IrcUser user,
            String mode);

    /**
     * This method is called whenever we receive a VERSION request. This
     * abstract implementation responds with the PircBot's _version string, so
     * if you override this method, be sure to either mimic its functionality or
     * to call super.onVersion(...);
     * @param user
     *            celui qui m'a versionné
     * @param target
     *            The target of the VERSION request, be it our nick or a channel
     *            name.
     */
    protected void onVersion(final IrcUser user, final String target) {
        ACtcp.sendCtcpNotice(P_VERSION + SPC + this.version, user.getNick(),
                this.outputThread);
    }

    /**
     * Called when a user (possibly us) gets voice status granted in a channel.
     * <p>
     * This is a type of mode change and is also passed to the onMode method in
     * the PircBot class.
     * <p>
     * The implementation of this method in the PircBot abstract class performs
     * no actions and may be overridden as required.
     * @since PircBot 0.9.5
     * @param channel
     *            The channel in which the mode change took place.
     * @param user
     *            le user
     * @param recipient
     *            The nick of the user that got 'voiced'.
     */
    protected abstract void onVoice(Channel channel, IrcUser user,
            String recipient);

    /**
     * Grants operator privilidges to a user on a channel. Successful use of
     * this method may require the bot to have operator status itself.
     * @param channel
     *            The channel we're opping the user on.
     * @param nick
     *            The nick of the user we are opping.
     */
    public final void op(final String channel, final String nick) {
        setMode(channel, PLUS + Mode.OP + SPC + nick);
    }

    /**
     * Parts a channel.
     * @param channel
     *            The name of the channel to leave.
     */
    public final void partChannel(final String channel) {
        send(IIrcCommands.PART + SPC + channel);
    }

    /**
     * Parts a channel, giving a reason.
     * @param channel
     *            The name of the channel to leave.
     * @param reason
     *            The reason for parting the channel.
     */
    public final void partChannel(final String channel, final String reason) {
        send(IIrcCommands.PART + SPC + channel + SPC + COLON + reason);
    }

    /**
     * Called when the mode of a channel is set. We process this in order to
     * call the appropriate onOp, onDeop, etc method before finally calling the
     * override-able onMode method.
     * <p>
     * Note that this method is private and is not intended to appear in the
     * javadoc generated documentation.
     * @param target
     *            The channel or nick that the mode operation applies to.
     * @param user
     *            le user
     * @param mode
     *            The mode that has been set.
     */
    protected final void processMode(final String target, final IrcUser user,
            final String mode) {
        if (Channel.isChannelName(target)) {
            // The mode of a channel is being changed.
            final String channel = target;
            final StrTokenizer tok = new StrTokenizer(mode);
            char pn = SPC;
            // All of this is very large and ugly, but it's the only way of
            // providing what the users want :-/
            final String param0 = tok.nextToken();
            for (int i = 0; i < param0.length(); i++) {
                final char atPos = param0.charAt(i);
                final Channel chan = Channel.getChannel(channel);
                if (atPos == PLUS || atPos == MINUS) {
                    pn = atPos;
                } else if (atPos == Mode.OP) {
                    if (pn == PLUS) {
                        onOp(chan, user, tok.nextToken());
                    } else {
                        onDeop(chan, user, tok.nextToken());
                    }
                } else if (atPos == Mode.VOICE) {
                    if (pn == PLUS) {
                        onVoice(chan, user, tok.nextToken());
                    } else {
                        onDeVoice(chan, user, tok.nextToken());
                    }
                } else if (atPos == Mode.KEY) {
                    if (pn == PLUS) {
                        onSetChannelKey(chan, user, tok.nextToken());
                    } else {
                        onRemoveChannelKey(chan, user, tok.nextToken());
                    }
                } else if (atPos == Mode.LIMIT) {
                    if (pn == PLUS) {
                        onSetChannelLimit(chan, user, Integer.parseInt(tok
                                .nextToken()));
                    } else {
                        onRemoveChannelLimit(chan, user);
                    }
                } else if (atPos == Mode.BAN) {
                    if (pn == PLUS) {
                        onSetChannelBan(chan, user, tok.nextToken());
                        return;
                    }
                    onRemoveChannelBan(chan, user, tok.nextToken());
                } else if (atPos == Mode.TOPIC_PROTECTED) {
                    if (pn == PLUS) {
                        onSetTopicProtection(chan, user);
                        return;
                    }
                    onRemoveTopicProtection(chan, user);
                } else if (atPos == Mode.NO_EXT_MSG) {
                    if (pn == PLUS) {
                        onSetNoExternalMessages(chan, user);
                        return;
                    }
                    onRemoveNoExternalMessages(chan, user);
                } else if (atPos == Mode.INVITE_ONLY) {
                    if (pn == PLUS) {
                        onSetInviteOnly(chan, user);
                        return;
                    }
                    onRemoveInviteOnly(chan, user);
                } else if (atPos == Mode.MODERATED) {
                    if (pn == PLUS) {
                        onSetModerated(chan, user);
                        return;
                    }
                    onRemoveModerated(chan, user);
                } else if (atPos == Mode.PRIVATE) {
                    if (pn == PLUS) {
                        onSetPrivate(chan, user);
                        return;
                    }
                    onRemovePrivate(chan, user);
                } else if (atPos == Mode.SECRET) {
                    if (pn == PLUS) {
                        onSetSecret(chan, user);
                        return;
                    }
                    onRemoveSecret(chan, user);
                }
            }
            onMode(channel, user, mode);
        } else {
            // The mode of a user is being changed.
            onUserMode(target, user, mode);
        }
    }

    /**
     * This method is called by the PircBot when a numeric response is received
     * from the IRC server. We use this method to allow PircBot to process
     * various responses from the server before then passing them on to the
     * onServerResponse method.
     * <p>
     * Note that this method is private and should not appear in any of the
     * javadoc generated documenation.
     * @param code
     *            The three-digit numerical code for the response.
     * @param response
     *            The full response from the IRC server.
     */
    void processServerResponse(final int code, final String response) {
        int firstSpace, secondSpace, thirdSpace;
        StringTokenizer tokenizer;
        String channel;
        switch (code) {
            case RPL_LIST:
                // This is a bit of information about a channel.
                firstSpace = response.indexOf(SPC);
                secondSpace = response.indexOf(SPC, firstSpace + 1);
                thirdSpace = response.indexOf(SPC, secondSpace + 1);
                int colon = response.indexOf(COLON);
                channel = response.substring(firstSpace + 1, secondSpace);
                final int userCount = Integer.parseInt(response.substring(
                        secondSpace + 1, thirdSpace));
                final String topic = response.substring(colon + 1);
                onChannelInfo(channel, userCount, topic);
                break;
            case RPL_TOPIC:
                // This is topic information about a channel we've just joined.
                firstSpace = response.indexOf(SPC);
                secondSpace = response.indexOf(SPC, firstSpace + 1);
                colon = response.indexOf(COLON);
                response.substring(firstSpace + 1, secondSpace);
                this.tempTopic = response.substring(colon + 1);
                break;
            case RPL_TOPICINFO:
                tokenizer = new StringTokenizer(response);
                tokenizer.nextToken();
                channel = tokenizer.nextToken();
                final String setBy = tokenizer.nextToken();
                final long date = Long.parseLong(tokenizer.nextToken()) * 1000;
                onTopic(channel, this.tempTopic, setBy, date, false);
                this.tempTopic = EMPTY;
                break;
            case RPL_NAMREPLY:
                // This is a list of nicks in a channel that we've just joined.
                final int channelEndIndex = response.indexOf(EMPTY + SPC
                        + COLON);
                response.substring(response.lastIndexOf(SPC,
                        channelEndIndex - 1) + 1, channelEndIndex);
                tokenizer = new StringTokenizer(StringUtils.substringAfter(
                        response, EMPTY + SPC + COLON));
                while (tokenizer.hasMoreTokens()) {
                    String tempNick = tokenizer.nextToken();
                    String prefix = EMPTY;
                    if (tempNick.charAt(0) == PREFIX_OP) {
                        // User is an operator in this channel.
                        prefix += PREFIX_OP;
                    } else if (tempNick.charAt(0) == PREFIX_VOICE) {
                        // User is voiced in this channel.
                        prefix += PREFIX_VOICE;
                    }
                    tempNick = tempNick.substring(prefix.length());
                    this.tempUsers.put(tempNick, prefix);
                }
                break;
            case RPL_ENDOFNAMES:
                // This is the end of a NAMES list, so we know that we've got
                // the full list of users in the channel that we just joined.
                channel = response.substring(response.indexOf(SPC) + 1,
                        response.indexOf(EMPTY + SPC + COLON));
                onUserList(channel, this.tempUsers.entrySet().iterator());
                this.tempUsers.clear();
                break;
            default:
                Utils.log(getClass(), EMPTY + '[' + code + ']' + response);
                return;
        }
        onServerResponse(code, response);
    }

    /**
     * Quits from the IRC server. Providing we are actually connected to an IRC
     * server, the onDisconnect() method will be called as soon as the IRC
     * server disconnects us.
     */
    public final void quitServer() {
        quitServer(EMPTY);
    }

    /**
     * Quits from the IRC server with a reason. Providing we are actually
     * connected to an IRC server, the onDisconnect() method will be called as
     * soon as the IRC server disconnects us.
     * @param reason
     *            The reason for quitting the server.
     */
    public final void quitServer(final String reason) {
        send(QUIT + SPC + COLON + reason);
        this.outputThread.setRunning(false);
    }

    /**
     * Reconnects to the IRC server that we were previously connected to. If
     * necessary, the appropriate port number and password will be used. This
     * method will throw an IrcException if we have never connected to an IRC
     * server previously.
     * @since PircBot 0.9.9
     */
    public final void reconnect() {
        if (this.server != null) {
            connect(this.server);
            return;
        }
        Utils
                .logError(
                        getClass(),
                        new Exception(
                                "Cannot reconnect to an IRC server because we were never connected to one previously!"));
    }

    /**
     * Sends a raw line through the outgoing message queue.
     * @param line
     *            The raw line to send to the IRC server.
     */
    public final void send(final String line) {
        this.outputThread.put(line);
    }

    /**
     * Sends an action to the channel or to a user.
     * @param target
     *            The name of the channel or user nick to send to.
     * @param action
     *            The action to send.
     * @see AColors
     */
    public final void sendAction(final String target, final String action) {
        ACtcp.sendAction(action, target, this.outputThread);
    }

    /**
     * Sends an invitation to join a channel. Some channels can be marked as
     * "invite-only", so it may be useful to allow a bot to invite people into
     * it.
     * @param nick1
     *            The nick of the user to invite
     * @param channel
     *            The channel you are inviting the user to join.
     */
    public final void sendInvite(final String nick1, final String channel) {
        send(INVITE + SPC + nick1 + SPC + COLON + channel);
    }

    /**
     * Sends a message to a channel or a private message to a user. These
     * messages are added to the outgoing message queue and sent at the earliest
     * possible opportunity.
     * <p>
     * You may optionally apply colours, boldness, underlining, etc to the
     * message by using the <code>Colors</code> class.
     * @param target
     *            The name of the channel or user nick to send to.
     * @param message
     *            The message to send.
     * @see AColors
     */
    public final void sendMessage(final String target, final String message) {
        send(PRIVMSG + SPC + target + SPC + COLON + message);
    }

    /**
     * Sends a notice to the channel or to a user.
     * @param target
     *            The name of the channel or user nick to send to.
     * @param notice
     *            The notice to send.
     */
    public final void sendNotice(final String target, final String notice) {
        send(NOTICE + SPC + target + SPC + COLON + notice);
    }

    /**
     * @param connected1
     *            The connected to set.
     */
    public final void setConnected(final boolean connected1) {
        this.connected = connected1;
    }

    /**
     * Sets the interal finger message. This should be set before joining any
     * servers.
     * @param finger1
     *            The new finger message for the Bot.
     */
    protected final void setFinger(final String finger1) {
        this.finger = finger1;
    }

    /**
     * Sets the internal login of the Bot. This should be set before joining any
     * servers.
     * @param login1
     *            The new login of the Bot.
     */
    public final void setLogin(final String login1) {
        this.myLogin = login1;
    }

    /**
     * Set the mode of a channel. This method attempts to set the mode of a
     * channel. This may require the bot to have operator status on the channel.
     * For example, if the bot has operator status, we can grant operator status
     * to "Dave" on the #cs channel by calling setMode("#cs", "+o Dave"); An
     * alternative way of doing this would be to use the op method.
     * @param channel
     *            The channel on which to perform the mode change.
     * @param mode
     *            The new mode to apply to the channel. This may include zero or
     *            more arguments if necessary.
     * @see #op(String,String) op
     */
    public final void setMode(final String channel, final String mode) {
        send(MODE + SPC + channel + SPC + mode);
    }

    /**
     * @param name
     *            le myName à régler
     */
    public final void setMyName(final String name) {
        this.myName = name;
    }

    /**
     * Sets the internal nick of the bot. This is only to be called by the
     * PircBot class in response to notification of nick changes that apply to
     * us.
     * @param nick1
     *            The new nick.
     */
    protected final void setNick(final String nick1) {
        this.myNick = nick1;
    }

    /**
     * Set the topic for a channel. This method attempts to set the topic of a
     * channel. This may require the bot to have operator status if the topic is
     * protected.
     * @param channel
     *            The channel on which to perform the mode change.
     * @param topic
     *            The new topic for the channel.
     */
    public final void setTopic(final String channel, final String topic) {
        send(TOPIC + SPC + channel + SPC + COLON + topic);
    }

    /**
     * Starts an ident server (Identification Protocol Server, RFC 1413).
     * <p>
     * Most IRC servers attempt to contact the ident server on connecting hosts
     * in order to determine the user's identity. A few IRC servers will not
     * allow you to connect unless this information is provided.
     * <p>
     * So when a PircBot is run on a machine that does not run an ident server,
     * it may be necessary to call this method to start one up.
     * <p>
     * Calling this method starts up an ident server which will respond with the
     * login provided by calling getLogin() and then shut down immediately. It
     * will also be shut down if it has not been contacted within 60 seconds of
     * creation.
     * <p>
     * If you require an ident response, then the correct procedure is to start
     * the ident server and then connect to the IRC server. The IRC server may
     * then contact the ident server to get the information it needs.
     * <p>
     * The ident server will fail to start if there is already an ident server
     * running on port 113, or if you are running as an unprivileged user who is
     * unable to create a server socket on that port number.
     * <p>
     * If it is essential for you to use an ident server when connecting to an
     * IRC server, then make sure that port 113 on your machine is visible to
     * the IRC server so that it may contact the ident server.
     * @since PircBot 0.9c
     */
    public final void startIdentServer() {
        new IdentServer(this.myLogin).execute();
    }

    /**
     * Unbans a user from a channel. An example of a valid hostmask is
     * "*!*compu@*.18hp.net". Successful use of this method may require the bot
     * to have operator status itself.
     * @param channel
     *            The channel to unban the user from.
     * @param hostmask
     *            A hostmask representing the user we're unbanning.
     */
    public final void unBan(final String channel, final String hostmask) {
        send(MODE + SPC + channel + SPC + MINUS + Mode.BAN + hostmask);
    }

    /**
     * Grants voice privilidges to a user on a channel. Successful use of this
     * method may require the bot to have operator status itself.
     * @param channel
     *            The channel we're voicing the user on.
     * @param nick1
     *            The nick of the user we are voicing.
     */
    public final void voice(final String channel, final String nick1) {
        setMode(channel, PLUS + Mode.VOICE + nick1);
    }
}

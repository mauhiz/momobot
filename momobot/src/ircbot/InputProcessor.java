package ircbot;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.InterruptedIOException;
import java.net.Socket;
import java.util.StringTokenizer;
import java.util.concurrent.TimeUnit;
import org.apache.commons.lang.StringUtils;
import utils.MyRunnable;
import utils.Utils;

/**
 * A Thread which reads lines from the IRC server. It then passes these lines to
 * the PircBot without changing them. This running Thread also detects
 * disconnection from the server and is thus used by the OutputThread to send
 * lines to the server.
 * @author Paul James Mutton, <a
 *         href="http://www.jibble.org/">http://www.jibble.org/</a>
 * @version 1.4.4 (Build time: Tue Mar 29 20:58:46 2005)
 */
public class InputProcessor extends MyRunnable implements IIrcConstants,
        IIrcCommands, ICtcpCommands {
    /**
     * mon mastah.
     */
    private AIrcBot        bot    = null;
    /**
     * si je suis loggé.
     */
    private boolean        logged = false;
    /**
     * mon lecteur.
     */
    private BufferedReader reader = null;
    /**
     * le nombre d'essais.
     */
    private int            tries  = 0;

    /**
     * The InputThread reads lines from the IRC server and allows the PircBot to
     * handle them.
     * @param bot1
     *            le bot
     * @param socket
     *            le socket
     */
    InputProcessor(final AIrcBot bot1, final Socket socket) {
        this.bot = bot1;
        try {
            this.reader = new BufferedReader(new InputStreamReader(socket
                    .getInputStream()));
            setRunning(true);
            Utils.log(getClass(), "started");
        } catch (final IOException e) {
            Utils.logError(this.getClass(), e);
        }
    }

    /**
     * This method handles events when any line of text arrives from the server,
     * then calling the appropriate method in the PircBot. This method is
     * protected and only called by the InputThread for this instance.
     * <p>
     * This method may not be overridden!
     * @param line
     *            The raw line of text from the server.
     */
    protected final void handleLine(final String line) {
        if (line.startsWith(PING)) {
            // Respond to the ping and return immediately.
            this.bot.onServerPing(line.substring(PING.length() + 1));
            return;
        }
        StringTokenizer tokenizer = new StringTokenizer(line);
        String senderInfo = tokenizer.nextToken();
        IrcUser sender = null;
        String command = tokenizer.nextToken();
        String target = null;
        if (senderInfo.charAt(0) == COLON) {
            senderInfo = senderInfo.substring(1);
            HostMask hm = new HostMask(senderInfo);
            try {
                sender = hm.createUser();
            } catch (Exception e) {
                if (tokenizer.hasMoreTokens()) {
                    final String token = command;
                    int code = -1;
                    try {
                        code = Integer.parseInt(token);
                        final String response = line.substring(line.indexOf(
                                token, senderInfo.length()) + 4, line.length());
                        this.bot.processServerResponse(code, response);
                        return;
                    } catch (final NumberFormatException e1) {
                        // This is not a server response.
                        // It must be a nick without login and hostname.
                        // (or maybe a NOTICE or suchlike from the server)
                        target = token;
                    }
                } else {
                    // We don't know what this line means.
                    this.bot.onUnknown(line);
                    return;
                }
            }
        }
        command = command.toUpperCase();
        if (target == null) {
            target = tokenizer.nextToken();
        }
        if (target.charAt(0) == COLON) {
            target = target.substring(1);
        }
        if (command.equals(PRIVMSG)) {
            final int ctcp = line.indexOf(EMPTY + COLON + STX);
            if (ctcp > 0 && line.endsWith(EMPTY + STX)) {
                // begin CTCP
                final String request = line.substring(ctcp + 2,
                        line.length() - 1);
                if (request.equals(P_VERSION)) {
                    this.bot.onVersion(sender, target);
                } else if (request.startsWith(E_ACTION)) {
                    this.bot.onAction(sender, target, request
                            .substring(E_ACTION.length() + 1));
                } else if (request.startsWith(P_PING)) {
                    this.bot.onPing(sender, target, request.substring(P_PING
                            .length() + 1));
                } else if (request.equals(P_TIME)) {
                    this.bot.onTime(sender, target);
                } else if (request.equals(P_FINGER)) {
                    this.bot.onFinger(sender, target);
                } else {
                    tokenizer = new StringTokenizer(request);
                    if (tokenizer.countTokens() >= 5
                            && tokenizer.nextToken().equals(E_DCC)) {
                        if (!this.bot.getDccManager().process(sender, request)) {
                            this.bot.onUnknown(line);
                        }
                        return;
                    }
                    this.bot.onUnknown(line);
                }
                // end CTCP
                return;
            }
            if (Channel.isChannelName(target)) {
                this.bot.onMessage(Channel.getChannel(target), sender,
                        StringUtils.substringAfter(line, EMPTY + SPC + COLON));
                return;
            }
            this.bot.onPrivateMessage(sender, StringUtils.substringAfter(line,
                    EMPTY + SPC + COLON));
        } else if (command.equals(JOIN)) {
            // Someone is joining a channel.
            final Channel channel = Channel.getChannel(target);
            this.bot.onJoin(channel, sender);
        } else if (command.equals(PART)) {
            final Channel channel = Channel.getChannel(target);
            this.bot.onPart(channel, sender);
        } else if (command.equals(NICK)) {
            // Somebody is changing their nick.
            if (senderInfo.equals(this.bot.getNick())) {
                // Update our nick if it was us that changed nick.
                this.bot.setNick(target);
            }
            IrcUser.updateUser(sender, target);
            this.bot.onNickChange(sender, target);
        } else if (command.equals(NOTICE)) {
            // Someone is sending a notice.
            this.bot.onNotice(sender, target, StringUtils.substringAfter(line,
                    EMPTY + SPC + COLON));
        } else if (command.equals(QUIT)) {
            // Someone has quit from the IRC server.
            if (senderInfo.equals(this.bot.getNick())) {
                this.bot.onDisconnect();
            } else {
                this.bot.onQuit(sender, StringUtils.substringAfter(line, EMPTY
                        + SPC + COLON));
            }
        } else if (command.equals(KICK)) {
            // Somebody has been kicked from a channel.
            final String recipient = tokenizer.nextToken();
            this.bot.onKick(target.toLowerCase(), sender, recipient,
                    StringUtils.substringAfter(line, EMPTY + SPC + COLON));
        } else if (command.equals(MODE)) {
            // Somebody is changing the mode on a channel or user.
            String mode = line.substring(line.indexOf(target, 2)
                    + target.length() + 1);
            if (mode.charAt(0) == COLON) {
                mode = mode.substring(1);
            }
            this.bot.processMode(target.toLowerCase(), sender, mode);
        } else if (command.equals(TOPIC)) {
            // Someone is changing the topic.
            this.bot.onTopic(target.toLowerCase(), StringUtils.substringAfter(
                    line, EMPTY + SPC + COLON), senderInfo, System
                    .currentTimeMillis(), true);
        } else if (command.equals(INVITE)) {
            // Somebody is inviting somebody else into a channel.
            this.bot.onInvite(target.toLowerCase(), sender, StringUtils
                    .substringAfter(line, EMPTY + SPC + COLON));
        } else {
            // If we reach this point, then we've found something that the
            // PircBot doesn't currently deal with.
            this.bot.onUnknown(line);
        }
    }

    /**
     * Returns true if this InputThread is connected to an IRC server. The
     * result of this method should only act as a rough guide, as the result may
     * not be valid by the time you act upon it.
     * @return True if still connected.
     */
    final boolean isLogged() {
        return this.logged;
    }

    /**
     * Called to start this Thread reading lines from the IRC server. When a
     * line is read, this method calls the handleLine method in the PircBot,
     * which may subsequently call an 'onXxx' method in the PircBot subclass. If
     * any subclass of Throwable (i.e. any Exception or Error) is thrown by your
     * method, then this method will print the stack trace to the standard
     * output. It is probable that the PircBot may still be functioning normally
     * after such a problem, but the existance of any uncaught exceptions in
     * your code is something you should really fix.
     */
    @Override
    public final void run() {
        try {
            String line;
            while (isRunning()) {
                line = null;
                try {
                    line = this.reader.readLine();
                } catch (final InterruptedIOException iioe) {
                    // This will happen if we haven't received anything from the
                    // server for a while.
                    // So we shall send it a ping to check that we are still
                    // connected.
                    this.bot.send(PING
                            + SPC
                            + TimeUnit.SECONDS
                                    .convert(System.currentTimeMillis(),
                                            TimeUnit.MILLISECONDS));
                    // Now we go back to listening for stuff from the server...
                    continue;
                }
                if (line == null) {
                    // The server must have disconnected us.
                    setRunning(false);
                    break;
                }
                try {
                    handleLine(line);
                    if (isLogged()) {
                        continue;
                    }
                    final int firstSpace = line.indexOf(SPC);
                    final int secondSpace = line.indexOf(SPC, firstSpace + 1);
                    if (secondSpace >= 0) {
                        final String code = line.substring(firstSpace + 1,
                                secondSpace);
                        if (code.equals("AUTH")) {
                            Utils.log(getClass(), line);
                        } else if (code.equals("004")) {
                            this.logged = true;
                            Utils.log(getClass(), "Logged");
                            this.bot.onConnect();
                            continue;
                        } else if (code.startsWith("5") || code.startsWith("4")) {
                            Utils.logError(getClass(), new Exception(EMPTY
                                    + '[' + code + "] Could not log in : "
                                    + line));
                            setRunning(false);
                            break;
                        } else if (Integer.parseInt(code) == ERR_NICKNAMEINUSE) {
                            this.tries++;
                            this.bot.setNick(this.bot.getMyName() + this.tries);
                            this.bot.send(NICK + this.bot.getNick());
                        }
                    }
                } catch (final Throwable t) {
                    // ce catch encapsule les erreurs dues à l'implémentation de
                    // PircBot sans le killer.
                    Utils.logError(getClass(), t);
                    t.printStackTrace();
                }
            }
        } catch (final IOException e) {
            Utils.logError(getClass(), e);
        }
        // If we reach this point, then we must have disconnected.
        Utils.log(getClass(), "Disconnected.");
        this.logged = false;
        this.bot.onDisconnect();
    }
}

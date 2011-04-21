package net.mauhiz.irc.base.data;

import net.mauhiz.irc.MomoStringUtils;
import net.mauhiz.irc.base.IrcSpecialChars;
import net.mauhiz.irc.base.msg.IIrcMessage;
import net.mauhiz.irc.base.msg.Join;
import net.mauhiz.irc.base.msg.Kick;
import net.mauhiz.irc.base.msg.Mode;
import net.mauhiz.irc.base.msg.Nick;
import net.mauhiz.irc.base.msg.Notice;
import net.mauhiz.irc.base.msg.Part;
import net.mauhiz.irc.base.msg.Ping;
import net.mauhiz.irc.base.msg.Privmsg;
import net.mauhiz.irc.base.msg.Quit;
import net.mauhiz.irc.base.msg.ServerError;
import net.mauhiz.irc.base.msg.ServerMsg;
import net.mauhiz.irc.base.msg.SetTopic;

import org.apache.commons.lang.NotImplementedException;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;

/**
 * @author mauhiz
 */
public class IrcDecoder implements IrcSpecialChars, IIrcDecoder {

    private static final IIrcDecoder INSTANCE = new IrcDecoder();
    private static final Logger LOG = Logger.getLogger(IrcDecoder.class);

    public static IIrcDecoder getInstance() {
        return INSTANCE;
    }

    public static ArgumentList tokenizeArgs(String argumentStr) {
        return new ArgumentList(argumentStr);
    }

    private IrcDecoder() {
        super();
    }

    private IIrcMessage buildFromCommand(IrcCommands command, IIrcServerPeer server, ArgumentList args, Target from,
            String msg) {

        switch (command) {
            case NOTICE:
                Target to = decodeTarget(server, args.poll());
                return new Notice(server, from, to, msg);

            case PING:
                return new Ping(server, from, msg);

            case MODE:
                Target modifiedObject = decodeTarget(server, args.poll());
                return new Mode(server, from, modifiedObject, args);

            case JOIN:
                String[] chans = StringUtils.split(args.poll(), ',');
                String[] keys = StringUtils.split(args.poll(), ',');
                IrcChannel[] channels = new IrcChannel[chans.length];
                for (int i = 0; i < chans.length; i++) {
                    channels[i] = server.getNetwork().findChannel(chans[i]);
                }
                return new Join(server, (IrcUser) from, channels, keys);

            case PART:
                chans = StringUtils.split(args.poll(), ',');
                channels = new IrcChannel[chans.length];
                for (int i = 0; i < chans.length; i++) {
                    channels[i] = server.getNetwork().findChannel(chans[i]);
                }
                return new Part(server, (IrcUser) from, msg, channels);

            case PRIVMSG:
                to = decodeTarget(server, args.poll());
                if (msg.charAt(0) == QUOTE_STX) {
                    String ctcpContent = StringUtils.strip(msg, Character.toString(QUOTE_STX));
                    return CtcpFactory.decode(server, from, to, ctcpContent);
                }
                return new Privmsg(server, from, to, msg);

            case QUIT:
                return new Quit(server, from, msg);

            case NICK:
                return new Nick(server, (IrcUser) from, msg);

            case KICK:
                to = decodeTarget(server, args.poll());
                String nick = args.poll();
                IrcUser target = server.getNetwork().findUser(nick, false);
                return new Kick(server, from, (IrcChannel) to, target, msg);

            case ERROR:
                return new ServerError(server, msg);

            case TOPIC:
                to = decodeTarget(server, args.poll());
                return new SetTopic(server, from, (IrcChannel) to, msg);

            default:
                return null;
        }
    }

    /**
     * @param raw
     * @return raw IRC msg
     */
    public IIrcMessage buildFromRaw(IIrcServerPeer server, String raw) {
        String argumentStr = StringUtils.substringBefore(raw, " :");
        ArgumentList args = tokenizeArgs(argumentStr);

        if (args.isEmpty()) {
            throw new IllegalArgumentException("Malformed IRC message: " + raw);
        }

        String next = args.peek();
        String fromStr = next != null && next.charAt(0) == ':' ? args.poll().substring(1) : null;
        String cmd = args.poll();

        if (cmd == null) {
            throw new IllegalArgumentException("Malformed IRC message: " + raw);
        }

        String msg = StringUtils.substringAfter(raw, " :");
        Target from = decodeTarget(server, fromStr);

        if (StringUtils.isNumeric(cmd)) {
            // skip the 'to'
            args.poll();
            return new ServerMsg(server, from, Integer.parseInt(cmd), args, msg);
        }

        IrcCommands command = IrcCommands.valueOf(cmd);
        IIrcMessage ircMessage = buildFromCommand(command, server, args, from, msg);
        if (ircMessage == null) {
            throw new NotImplementedException("Unknown message on network " + server.getNetwork() + ": " + raw);
        }

        return ircMessage;
    }

    protected Target decodeTarget(IIrcServerPeer peer, String fromStr) {
        if (fromStr == null) {
            return null;
        }
        IrcNetwork server = peer.getNetwork();
        if (MomoStringUtils.isChannelName(fromStr)) {
            return server.findChannel(fromStr);
        }

        HostMask mask = HostMask.getInstance(fromStr);

        if (mask == null) { // not a host mask
            if (StringUtils.contains(fromStr, '.')) {
                peer.setIrcForm(fromStr);
                return peer;
            }

            return server.findUser(fromStr, true);
        }

        return server.findUser(mask, true);
    }
}
